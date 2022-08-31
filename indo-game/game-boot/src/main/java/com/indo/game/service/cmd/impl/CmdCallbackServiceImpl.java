package com.indo.game.service.cmd.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.CMDAESDecrypt;
import com.indo.game.common.util.CMDAESEncrypt;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.cmd.CmdCallbackService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class CmdCallbackServiceImpl implements CmdCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object check(String token, String secretKey, String ip) {
        logger.info("cmd_check cmdGame paramJson:{}, ip:{}", token, ip);
        try {
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initCheckResponseXml(null, -102, "非信任來源IP");
            }

            CptOpenMember cptOpenMember = externalService.quertCptOpenMember(token, OpenAPIProperties.CMD_PLATFORM_CODE);
            if (null == cptOpenMember) {
                return initCheckResponseXml(null, -97, "用户不存在");
            }

            return initCheckResponseXml(cptOpenMember.getUserName(), 0, "Success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-999, e.getMessage());
        }

    }

    @Override
    public Object getBalance(String balancePackage, String packageId, String dateSent, String ip) {
        logger.info("cmd_getBalance cmdGame paramJson:{}, ip:{}", balancePackage, ip);
        try {

            String params = decryptParams(balancePackage);
            if (StringUtils.isBlank(params)) {
                return initFailureResponse(-100, "请求参数解密失败");
            }

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(-102, "非信任來源IP");
            }

            JSONObject paramJson = JSONObject.parseObject(params);
            String account = paramJson.getString("SourceName");
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
            if (null == memBaseinfo) {
                return initFailureResponse(-97, "用户不存在");
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("StatusCode", 100);
            jsonObject.put("StatusMessage", "Success");
            jsonObject.put("DateSent", System.currentTimeMillis());
            jsonObject.put("PackageId", packageId);
            jsonObject.put("Balance", memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            jsonObject.put("DateReceived", Long.valueOf(dateSent));
            logger.info("cmdCallback getBalance 回调查询余额返回数据 params:{}", jsonObject);
            return encryptResp(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-999, e.getMessage());
        }

    }

    @Override
    public Object deductBalance(JSONObject params, String ip) {
        logger.info("cmd_deductBalance cmdGame paramJson:{}, ip:{}", params, ip);

        try {
            String decryptParams = decryptParams(params.getString("balancePackage"));
            if (StringUtils.isBlank(decryptParams)) {
                return initFailureResponse(-100, "请求参数解密失败");
            }

            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(-102, "非信任來源IP");
            }

            JSONObject paramJson = JSONObject.parseObject(decryptParams);
            String paySerialno = paramJson.getString("ReferenceNo");
            String userName = paramJson.getString("SourceName");
            BigDecimal betAmount = null!=paramJson.getBigDecimal("TransactionAmount")?paramJson.getBigDecimal("TransactionAmount").multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            Long dateSent = params.getLong("dateSent");


            // 查询订单
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(-97, "用户不存在");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            if (balance.compareTo(betAmount.abs()) < 0) {
                return initFailureResponse(-95, "玩家余额不足");
            }
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.CMD_PLATFORM_CODE,OpenAPIProperties.CMD_PLATFORM_CODE);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            Txns oldTxns = getTxns(gameParentPlatform, paySerialno);
            // 重复订单
            if (null != oldTxns) {
                return initFailureResponse(-96, "该注单已承认");
            }


            balance = balance.subtract(betAmount.abs());
            // 更新余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(paySerialno);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
            //平台代码
            txns.setPlatform(gameParentPlatform.getPlatformCode());
            //平台英文名称
            txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
            //平台中文名称
            txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
            //平台游戏类型
            txns.setGameType(gameCategory.getGameType());
            //游戏分类ID
            txns.setCategoryId(gameCategory.getId());
            //游戏分类名称
            txns.setCategoryName(gameCategory.getGameName());
            //平台游戏代码
            txns.setGameCode(gamePlatform.getPlatformCode());
            //游戏名称
            txns.setGameName(gamePlatform.getPlatformEnName());
            //下注金额
            txns.setBetAmount(betAmount.abs());
            //游戏平台的下注项目
//            txns.setBetType(cmdCallbackBetReq.getGame().toString());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            txns.setWinAmount(betAmount.abs());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(dateSent, DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount.abs());
            //真实返还金额,游戏赢分
//            txns.setRealWinAmount(winloseAmount);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //返还金额 (包含下注金额)
//            txns.setWinAmount(winloseAmount);
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(dateSent, DateUtils.newFormat));
            //操作名称
            txns.setMethod("Place Bet");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(-999, "订单写入失败");
            }

            JSONObject respJson = new JSONObject();
            respJson.put("ActionId", 1003);
            respJson.put("SourceName", userName);
            respJson.put("TransactionAmount", betAmount.divide(gameParentPlatform.getCurrencyPro()));
            respJson.put("ReferenceNo", paySerialno);

            JSONObject jsonObject = initSuccessResponse(respJson);
            jsonObject.put("DateReceived", dateSent);
            jsonObject.put("Balance", balance.divide(gameParentPlatform.getCurrencyPro()));
            return encryptResp(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(-999, e.getMessage());
        }
    }

    @Override
    public Object updateBalance(JSONObject params, String ip) {
        logger.info("cmd_updateBalance cmdGame paramJson:{}, ip:{}", params, ip);

        try {
            String decryptParams = decryptParams(params.getString("balancePackage"));
            if (StringUtils.isBlank(decryptParams)) {
                return initFailureResponse(-100, "请求参数解密失败");
            }

            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(-102, "非信任來源IP");
            }

            Long dateSent = System.currentTimeMillis();
            JSONObject paramJson = JSONObject.parseObject(decryptParams);
            String matchId = paramJson.getString("MatchID");
            JSONArray ticketDetails = paramJson.getJSONArray("TicketDetails");

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.CMD_PLATFORM_CODE,OpenAPIProperties.CMD_PLATFORM_CODE);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            // 返回
            JSONArray respArr = new JSONArray();
            for (int i = 0; i < ticketDetails.size(); i++) {
                JSONObject resp = new JSONObject();
                JSONObject json = ticketDetails.getJSONObject(i);
                String paySerialno = json.getString("ReferenceNo");
                String userName = json.getString("SourceName");
                BigDecimal betAmount = null!=json.getBigDecimal("TransactionAmount")?json.getBigDecimal("TransactionAmount").multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

                // 查询订单
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
                if (null == memBaseinfo) {
                    return initFailureResponse(-97, "用户不存在");
                }
                BigDecimal balance = memBaseinfo.getBalance();

                // 查询用户请求订单
                Txns oldTxns = getTxns(platformGameParent, paySerialno);
                if (null != oldTxns&&"Settle".equals(oldTxns.getMethod())) {
                    return initFailureResponse(-96, "该注单已承认");
                }

                if (betAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // 会员余额
                    balance = balance.add(betAmount);
                    // 更新玩家余额
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                } else {
                    // 会员余额
                    balance = balance.subtract(betAmount.abs());
                    // 更新玩家余额
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                }
                //更新时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                if(null!=oldTxns) {
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    oldTxns.setStatus("Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }else {

                    //玩家货币代码
                    txns.setCurrency(platformGameParent.getCurrencyType());
                    //平台代码
                    txns.setPlatform(platformGameParent.getPlatformCode());
                    //平台英文名称
                    txns.setPlatformEnName(platformGameParent.getPlatformEnName());
                    //平台中文名称
                    txns.setPlatformCnName(platformGameParent.getPlatformCnName());
                    //平台游戏类型
                    txns.setGameType(gameCategory.getGameType());
                    //游戏分类ID
                    txns.setCategoryId(gameCategory.getId());
                    //游戏分类名称
                    txns.setCategoryName(gameCategory.getGameName());
                    //平台游戏代码
                    txns.setGameCode(gamePlatform.getPlatformCode());
                    //游戏名称
                    txns.setGameName(gamePlatform.getPlatformEnName());
                    //下注金额
                    txns.setBetAmount(betAmount);

                }
                //游戏商注单号
                txns.setPlatformTxId(paySerialno);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(betAmount);
                txns.setWinAmount(betAmount);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
                txns.setUpdateTime(DateUtils.formatByLong(System.currentTimeMillis(), DateUtils.newFormat));
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                if (betAmount.compareTo(BigDecimal.ZERO) == 0) {
                    resultTyep = 2;
                } else if (betAmount.compareTo(BigDecimal.ZERO) > 0) {
                    resultTyep = 0;
                } else {
                    resultTyep = 1;
                }
                txns.setResultType(resultTyep);
                //操作名称
                txns.setMethod("Settle");
                txns.setStatus("Running");
                //余额
                txns.setBalance(balance);

                txns.setUpdateTime(dateStr);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                txnsMapper.insert(txns);

                resp.put("SourceName", userName);
                resp.put("TransactionAmount", balance);
                resp.put("ReferenceNo", paySerialno);
                respArr.add(resp);
            }

            JSONObject respJson = new JSONObject();
            respJson.put("ActionId", 4002);
            respJson.put("MatchID", matchId);
            respJson.put("TicketDetails", respArr);

            JSONObject jsonObject = initSuccessResponse(respJson);
            jsonObject.put("DateReceived", dateSent);
            return encryptResp(jsonObject);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1001, e.getMessage());
        }
    }

    /**
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询IP是否被封
     *
     * @param ip ip
     * @return boolean
     */
    private boolean checkIp(String ip, GameParentPlatform platformGameParent) {
        if (null == platformGameParent) {
            return true;
        } else if (null == platformGameParent.getIpAddr() || "".equals(platformGameParent.getIpAddr())) {
            return false;
        }
        return !platformGameParent.getIpAddr().equals(ip);

    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse(JSONObject resp) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("StatusCode", 100);
        jsonObject.put("StatusMessage", "Success");
        jsonObject.put("DateSent", System.currentTimeMillis());
        jsonObject.put("PackageId", encryptResp(resp));
        return jsonObject;
    }

    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private String initFailureResponse(Integer error, String description) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("StatusCode", error);
        jsonObject.put("StatusMessage", description);
        jsonObject.put("DateSent", System.currentTimeMillis());
        return encryptResp(jsonObject);
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.CMD_PLATFORM_CODE);
    }

    /**
     * 解密
     *
     * @param balancePackage 加密串参数
     * @return String
     */
    private String decryptParams(String balancePackage) {
        return CMDAESDecrypt.decrypt(balancePackage, OpenAPIProperties.CMD_PARTNER_KEY);
    }


    /**
     * 加密返回參數
     *
     * @param jsonObject jsonObject
     * @return String
     */
    private String encryptResp(JSONObject jsonObject) {
        return CMDAESEncrypt.encrypt(jsonObject.toJSONString(), OpenAPIProperties.CMD_PARTNER_KEY);
    }

    private String initCheckResponseXml(String memberId, Integer statusCode, String message) {

        String retString = null;

        try {

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element responseElement = document.createElement("authenticate");

            Element member_idEl = document.createElement("member_id");
            if (StringUtils.isNotEmpty(memberId)) {
                member_idEl.setTextContent(memberId);
            }
            Element status_codeEl = document.createElement("status_code");
            status_codeEl.setTextContent(statusCode.toString());
            Element messageEl = document.createElement("message");
            messageEl.setTextContent(message);
            responseElement.appendChild(status_codeEl);
            responseElement.appendChild(messageEl);
            responseElement.appendChild(member_idEl);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("version", "1.0");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(responseElement);
            StringWriter strReturn = new StringWriter();
            transformer.transform(source, new StreamResult(strReturn));

            retString = strReturn.toString();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return retString;
    }

}
