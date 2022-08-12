package com.indo.core.service.game.s128;

import com.indo.common.config.OpenAPIProperties;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.service.game.common.GameCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import sun.reflect.generics.tree.VoidDescriptor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * PG
 *
 * @author
 */
@Slf4j
@Service
public class S128TaskServiceImpl implements IS128TaskService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    public static void main(String[] args) {
        new S128TaskServiceImpl().is128SettleTask();
    }
    /**
     * 获取结算
     */
    public void is128SettleTask() {
        try {
            StringBuilder apiUrl = new StringBuilder();
            Map<String, String> params = new HashMap<String, String>();
//            params.put("api_key", OpenAPIProperties.DJ_API_KEY);
//            params.put("agent_code", OpenAPIProperties.DJ_AGENT_CODE);
            params.put("api_key", "7F3C78F8ECC04798893262105631A0F2");
            params.put("agent_code", "VRJ00101");
            params.put("start_datetime", "2022-08-11 18:00");
            params.put("end_datetime", "2022-08-11 23:30");
//            apiUrl.append(OpenAPIProperties.DJ_API_URL).append("/get_cockfight_processed_ticket_by_bet_time.aspx");
            apiUrl.append("https://api8745.cfb2.net").append("/get_cockfight_processed_ticket_by_bet_time.aspx");
            String result = commonRequest(apiUrl.toString(), params, 0, "gameLogin");
            System.out.println("测试结果："+result);
//            if (!StringUtils.isEmpty(result)) {
//                Document doc = commonXml(result);
//                String errorCode = doc.getElementsByTagName("status_code").item(0).getTextContent();
//                if (StringUtils.isNotEmpty(result) && errorCode.equals("00")) {
////                    MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
////                    BigDecimal balance = memBaseinfo.getBalance();
////                    BigDecimal betAmount = djCallBackParentReq.getStake_money();
////                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
////                    wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
////                    wrapper.eq(Txns::getStatus, "Running");
////                    wrapper.eq(Txns::getPlatformTxId, djCallBackParentReq.getTicket_id());
////                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.DJ_PLATFORM_CODE);
////
////                    Txns oldTxns = txnsMapper.selectOne(wrapper);
////                    if (betAmount.compareTo(BigDecimal.ZERO) != 0) {
////                        if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
////                            balance = balance.subtract(betAmount.abs());
////                            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
////                        } else {
////                            balance = balance.add(betAmount);
////                            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
////                        }
////                    }
////
////                    Txns txns = new Txns();
////                    //游戏商注单号
////                    txns.setPlatformTxId(djCallBackParentReq.getTicket_id());
////                    //混合码
////                    txns.setRoundId(djCallBackParentReq.getTicket_id());
////                    //此交易是否是投注 true是投注 false 否
////                    //玩家 ID
////                    txns.setUserId(memBaseinfo.getAccount());
////                    //玩家货币代码
////                    txns.setCurrency(gameParentPlatform.getCurrencyType());
////                    //平台代码
////                    txns.setPlatform(gameParentPlatform.getPlatformCode());
////                    //平台英文名称
////                    txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
////                    //平台中文名称
////                    txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
////                    //平台游戏类型
////                    txns.setGameType(gameCategory.getGameType());
////                    //游戏分类ID
////                    txns.setCategoryId(gameCategory.getId());
////                    //游戏分类名称
////                    txns.setCategoryName(gameCategory.getGameName());
////                    //平台游戏代码
////                    txns.setGameCode(gamePlatform.getPlatformCode());
////                    //游戏名称
////                    txns.setGameName(gamePlatform.getPlatformEnName());
////                    //下注金额
////                    txns.setBetAmount(betAmount);
////                    if (null != oldTxns) {
////                        txns.setWinningAmount(betAmount);
////                    }else {
////                        if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
////                            txns.setWinningAmount(betAmount);
////                        }else {
////                            txns.setWinningAmount(betAmount.negate());
////                        }
////                    }
////                    txns.setWinAmount(betAmount);
////                    //玩家下注时间
////                    txns.setBetTime(DateUtils.formatByString(djCallBackParentReq.getCreated_datetime(), DateUtils.newFormat));
////                    //真实下注金额,需增加在玩家的金额
////                    txns.setRealBetAmount(betAmount);
////                    //返还金额 (包含下注金额)
////                    //有效投注金额 或 投注面值
////                    txns.setTurnover(betAmount);
////                    //辨认交易时间依据
////                    txns.setTxTime(null != djCallBackParentReq.getCreated_datetime() ? DateUtils.formatByString(djCallBackParentReq.getCreated_datetime(), DateUtils.newFormat) : "");
////                    //操作名称
////                    txns.setMethod("Place Bet");
////                    txns.setStatus("Running");
////                    //余额
////                    txns.setBalance(balance);
////                    //创建时间
////                    String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
////                    txns.setCreateTime(dateStr);
////                    //投注 IP
////                    txns.setBetIp(ip);//  string 是 投注 IP
////                    if (oldTxns != null) {
////                        oldTxns.setStatus("Settle");
////                        txnsMapper.updateById(oldTxns);
////                        txns.setMethod("Settle");
////                    }
////
////                    int num = txnsMapper.insert(txns);
//                }
//            }
        } catch (Exception e) {
            logger.error("djlog  djlog out:{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 公共请求
     */
    public String commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, apiUrl, params, type, userId);
        logger.info("djLog commonRequest resultString:{}",resultString);
        return resultString;
    }

    public static Document commonXml(String repXML) {
        Document doc = null;
        try {
            StringReader sr = new StringReader(repXML);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            return doc;
        } catch (Exception e) {
            log.error("djlog commonXml error {}", e);
            return doc;
        }
    }

    public static String replaceAllBlank(String str) {
        String s = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\*|");
            /*\n 回车(\u000a)
            \t 水平制表符(\u0009)
            \s 空格(\u0008)
            \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }
}
