package com.indo.game.service.mt.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.mt.MtService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class MtServiceImpl implements MtService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Result mtGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("mtlog mtGame account:{},mtCodeId:{}", loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")平台不存在");
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", "平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
            // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("(" + platform + ")游戏不存在");
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", "游戏未启用");
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", gamePlatform.getMaintenanceContent());
        }

        BigDecimal balance = loginUser.getBalance();
        //验证站点余额
        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("站点mt余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }

        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(GeneratorIdUtil.generateId());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(cptOpenMember, gameParentPlatform, gamePlatform, loginUser);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                // 退出游戏
                commonRequest(getLoginOutUrl(loginUser.getAccount()));
                // 启动游戏
                String startUrl = getStartGame(cptOpenMember, gameParentPlatform,gamePlatform);
                logger.info("天美log启动游戏 startUrl:{}", startUrl);
                JSONObject jsonObject = commonRequest(startUrl);
                if (null == jsonObject) {
                    return Result.failed("g091087", "第三方请求异常！");
                }
                logger.info("天美log getStartGame启动游戏返回paramsMap:{}", jsonObject.toJSONString());
                if (jsonObject.getInteger("resultCode")==1) {
                    // 请求URL
                    ApiResponseData responseData = new ApiResponseData();
                    responseData.setPathUrl(jsonObject.getString("url"));
                    logger.info("天美log getStartGame启动游戏返回成功:resultCode:{},PathUrl:{}",jsonObject.getInteger("resultCode"),jsonObject.getString("url"));
                    return Result.success(responseData);
                } else {
                    logger.info("天美log getStartGame启动游戏返回失败:resultCode:{}}",jsonObject.getInteger("resultCode"));
                    return errorCode(jsonObject.getString("resultCode"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed("g100104", "网络繁忙，请稍后重试！");

    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("mtlogout mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            // 退出
            JSONObject jsonObject = commonRequest(getLoginOutUrl(loginUser.getAccount()));
            logger.info("天美log logout退出游戏返回:JSONObject:{}}",jsonObject.toJSONString());
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if (jsonObject.getInteger("resultCode")==1) {
                logger.info("天美log logout退出游戏返回成功:resultCode:{}}",jsonObject.getInteger("resultCode"));
                return Result.success();
            } else {
                logger.info("天美log logout退出游戏返回失败:resultCode:{}}",jsonObject.getInteger("resultCode"));
                return errorCode(jsonObject.getString("resultCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result allWithdraw(LoginInfo loginUser, String platform, String ip) {
        logger.info("mt_allWithdraw mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            // 全部提取
            JSONObject jsonObject = commonRequest(getAllWithdrawUrl(loginUser.getAccount(), transactionId));
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出金额
                BigDecimal transCoins = jsonObject.getBigDecimal("transCoins");
                // 提出时间
                Date date = jsonObject.getDate("date");

                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);

                BigDecimal balance = memBaseinfo.getBalance().add(transCoins);
                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(transactionId);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(false);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(platformGameParent.getCurrencyType());
                //平台代码
                txns.setPlatform(platformGameParent.getPlatformCode());
                //平台名称
                txns.setPlatformEnName(platformGameParent.getPlatformEnName());
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
                txns.setBetAmount(BigDecimal.ZERO);
                // PP交易ID
                txns.setRePlatformTxId(transId);
                //游戏平台的下注项目
//            txns.setBetType(ppApiTransferReq.getGameId());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(transCoins);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(new Date(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(BigDecimal.ZERO);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(transCoins);
                // 返还金额 (包含下注金额)
                txns.setWinAmount(transCoins);
                //有效投注金额 或 投注面值
                txns.setTurnover(BigDecimal.ZERO);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.format(date, DateUtils.newFormat));
                //操作名称
                txns.setMethod("Settle");
                txns.setStatus("Running");
                //余额
                txns.setBalance(balance);
                //创建时间
                String dateStr = DateUtils.format(date, DateUtils.newFormat);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                int num = txnsMapper.insert(txns);
                if (num <= 0) {
                    int count = 0;
                    // 失败重试
                    while (count < 5) {
                        num = txnsMapper.insert(txns);
                        if (num > 0) break;
                        count++;
                    }
                }
                gameCommonService.updateUserBalance(memBaseinfo, transCoins, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }

    }

    @Override
    public Result getPlayerBalance(LoginInfo loginUser, String platform, String ip) {
        logger.info("mt_getPlayerBalance mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            // 余额查询
            JSONObject jsonObject = commonRequest(getBalanceUrl(loginUser.getAccount()));
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            if (jsonObject.getInteger("resultCode").equals(1)) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("balance", jsonObject.getBigDecimal("coinBalance"));
                return Result.success(jsonObject1);
            } else {
                return errorCode(jsonObject.getString("resultCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result deposit(LoginInfo loginUser, String platform, String ip, BigDecimal coins) {
        logger.info("mt_deposit mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
            if (memBaseinfo.getBalance().compareTo(coins) < 0) {
                return Result.failed("g300004", "会员余额不足！");
            }

            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();

            // 充值
            JSONObject jsonObject = commonRequest(getDepositUrl(loginUser.getAccount(), transactionId, coins));
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出时间
                Date date = jsonObject.getDate("date");

                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);

                // 会员平台扣款
                gameCommonService.updateUserBalance(memBaseinfo, coins, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
                BigDecimal balance = memBaseinfo.getBalance().subtract(coins);
                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(transactionId);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(false);
                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(platformGameParent.getCurrencyType());
                //平台代码
                txns.setPlatform(platformGameParent.getPlatformCode());
                //平台名称
                txns.setPlatformEnName(platformGameParent.getPlatformEnName());
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
                txns.setBetAmount(coins);
                // PP交易ID
                txns.setRePlatformTxId(transId);
                //游戏平台的下注项目
//            txns.setBetType(ppApiTransferReq.getGameId());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(BigDecimal.ZERO);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(new Date(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(coins);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(BigDecimal.ZERO);
                // 返还金额 (包含下注金额)
                txns.setWinAmount(BigDecimal.ZERO);
                //有效投注金额 或 投注面值
                txns.setTurnover(coins);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.format(date, DateUtils.newFormat));
                //操作名称
                txns.setMethod("Settle");
                txns.setStatus("Running");
                //余额
                txns.setBalance(balance);
                //创建时间
                String dateStr = DateUtils.format(date, DateUtils.newFormat);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                int num = txnsMapper.insert(txns);
                if (num <= 0) {
                    int count = 0;
                    // 失败重试
                    while (count < 5) {
                        num = txnsMapper.insert(txns);
                        if (num > 0) break;
                        count++;
                    }
                }
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result withdraw(LoginInfo loginUser, String platform, String ip, BigDecimal coins) {
        logger.info("mt_withdraw mtGame account:{},code:{}", loginUser.getAccount(), platform);
        try {
            String transactionId = GoldchangeEnum.DSFYXZZ.name() + GeneratorIdUtil.generateId();
            // 提取
            JSONObject jsonObject = commonRequest(getWithdrawUrl(loginUser.getAccount(), transactionId, coins));
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if (jsonObject.getInteger("resultCode").equals(1)) {
                // 返回交易编码
                String transId = jsonObject.getString("transId");
                // 提出时间
                Date date = jsonObject.getDate("date");

                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
                GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MT_PLATFORM_CODE);

                // 会员平台加款
                gameCommonService.updateUserBalance(memBaseinfo, coins, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
                BigDecimal balance = memBaseinfo.getBalance().add(coins);
                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(transactionId);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(false);
                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(platformGameParent.getCurrencyType());
                //平台代码
                txns.setPlatform(platformGameParent.getPlatformCode());
                //平台名称
                txns.setPlatformEnName(platformGameParent.getPlatformEnName());
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
                txns.setBetAmount(BigDecimal.ZERO);
                // PP交易ID
                txns.setRePlatformTxId(transId);
                //游戏平台的下注项目
//            txns.setBetType(ppApiTransferReq.getGameId());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(coins);
                //玩家下注时间
                txns.setBetTime(DateUtils.format(new Date(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(BigDecimal.ZERO);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(coins);
                // 返还金额 (包含下注金额)
                txns.setWinAmount(coins);
                //有效投注金额 或 投注面值
                txns.setTurnover(BigDecimal.ZERO);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.format(date, DateUtils.newFormat));
                //操作名称
                txns.setMethod("Settle");
                txns.setStatus("Running");
                //余额
                txns.setBalance(balance);
                //创建时间
                String dateStr = DateUtils.format(date, DateUtils.newFormat);
                txns.setCreateTime(dateStr);
                //投注 IP
                txns.setBetIp(ip);//  string 是 投注 IP
                int num = txnsMapper.insert(txns);
                if (num <= 0) {
                    int count = 0;
                    // 失败重试
                    while (count < 5) {
                        num = txnsMapper.insert(txns);
                        if (num > 0) break;
                        count++;
                    }
                }
                return Result.success();
            } else {
                return errorCode(jsonObject.getString("resultCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform,
                                    GamePlatform gamePlatform, LoginInfo loginUser) {

        // 用户注册
        JSONObject result = commonRequest(getCreateUrl(cptOpenMember, loginUser));
        if (null == result) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        logger.info("createMemberGame 用户注册返回JSONObject:{}", result.toJSONString());
        if (1 == result.getInteger("resultCode")||5==result.getInteger("resultCode")) {
            externalService.saveCptOpenMember(cptOpenMember);
            // 启动游戏
            String startUrl = getStartGame(cptOpenMember, gameParentPlatform,gamePlatform);
            logger.info("createMemberGame 启动游戏startUrl:{}", startUrl);
            JSONObject jsonObject = commonRequest(startUrl);
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            logger.info("createMemberGame 启动游戏返回JSONObject:{}", jsonObject.toJSONString());
            if (jsonObject.getInteger("resultCode")==1) {
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jsonObject.getString("url"));
                logger.info("天美log createMemberGame启动游戏返回成功:resultCode:{},PathUrl:{}",jsonObject.getInteger("resultCode"),jsonObject.getString("url"));
                return Result.success(responseData);
            } else {
                logger.info("天美log createMemberGame启动游戏返回失败:resultCode:{}",jsonObject.getInteger("resultCode"));
                return errorCode(jsonObject.getString("resultCode"));
            }
        } else {
            return errorCode(result.getString("resultCode"));
        }
    }

    /**
     * 公共请求
     */
    private JSONObject commonRequest(String apiUrl) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accept", "application/json");
        Map<String, Object> params = new HashMap<>();
        String returnResult = GameUtil.postJson(apiUrl, params, header);
        return JSONObject.parseObject(returnResult);
    }

    /**
     * 获取登录URL 账号
     * https://socket.zbfulipai.com/services/dg/player/playerPlatformUrl/{merchantId}/{playerName}/{pwd}/{code}/{data}
     *
     * @param cptOpenMember、 cptOpenMember
     * @return url
     */
    private String getStartGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, GamePlatform gamePlatform) {

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        if(!gamePlatform.getPlatformCode().equals(gameParentPlatform.getPlatformCode())){
            jsonObject.put("gameCode", gamePlatform.getPlatformCode());
        }

        jsonObject.put("lang", gameParentPlatform.getLanguageType());
        String data = jsonObject.toJSONString();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/playerPlatformUrl/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(cptOpenMember.getUserName()).append("/");
        url.append(MD5.md5(cptOpenMember.getPassword())).append("/");
        url.append(MD5.md5(OpenAPIProperties.MT_KEY + data)).append("/");
        url.append(getEncode(data));
        return url.toString();
    }

    /**
     * 創建用戶wm請求地址
     * /services/dg/player/playerCreate2/{playerName}/{merchantId}/{pwd}/{code}/{data}
     *
     * @return String
     */
    private String getCreateUrl(CptOpenMember cptOpenMember, LoginInfo loginUser) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/playerCreate2/");
        url.append(cptOpenMember.getUserName()).append("/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(MD5.md5(cptOpenMember.getPassword())).append("/");

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("nickname", loginUser.getNickName());
        jsonObject.put("playerLevel", loginUser.getMemLevel());
        url.append(MD5.md5(OpenAPIProperties.MT_KEY + jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getLoginOutUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/logOutGame/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount);
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getAllWithdrawUrl(String userAccount, String extTransId) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/allWithdraw/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/").append(extTransId);
        return url.toString();
    }

    /**
     * 玩家MT余额查询
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getBalanceUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/getPlayerBalance/");
        url.append(userAccount).append("/");
        url.append(OpenAPIProperties.MT_VENDOR_ID);
        return url.toString();
    }

    /**
     * 玩家充值
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getDepositUrl(String userAccount, String extTransId, BigDecimal coins) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/deposit2/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/");
        url.append(coins.setScale(4, RoundingMode.HALF_UP)).append("/");
        url.append(extTransId).append("/");
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("merchantId", OpenAPIProperties.MT_VENDOR_ID);
        jsonObject.put("playerName", userAccount);
        jsonObject.put("extTransId", extTransId);
        jsonObject.put("coins", coins.setScale(4, RoundingMode.HALF_UP).toString());
        url.append(MD5.md5(OpenAPIProperties.MT_KEY+jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));

        return url.toString();
    }

    /**
     * 玩家提现
     *
     * @param userAccount userAccount
     * @param extTransId  转出编号
     * @return String
     */
    private String getWithdrawUrl(String userAccount, String extTransId, BigDecimal coins) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.MT_API_URL);
        url.append("/services/dg/player/withdraw2/");
        url.append(OpenAPIProperties.MT_VENDOR_ID).append("/");
        url.append(userAccount).append("/");
        url.append(coins.setScale(4, RoundingMode.HALF_UP)).append("/");
        url.append(extTransId).append("/");

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("merchantId", OpenAPIProperties.MT_VENDOR_ID);
        jsonObject.put("playerName", userAccount);
        jsonObject.put("extTransId", extTransId);
        jsonObject.put("coins", coins.setScale(4, RoundingMode.HALF_UP).toString());

        url.append(MD5.md5(OpenAPIProperties.MT_KEY+jsonObject.toJSONString())).append("/");
        url.append(getEncode(jsonObject.toJSONString()));
        return url.toString();
    }

    /**
     * data base64code
     *
     * @param data data
     * @return String
     */
    private String getEncode(String data) {
        logger.info("getEncode.data {}", data);
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }


    public Result errorCode(String errorCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        0 创建异常
            case "0":
                return Result.failed("g009999", "创建异常");
//        2 商户不存在
            case "2":
                return Result.failed("g091116", "商户不存在");

//        3 商户无效
            case "3":
                return Result.failed("g091104", "商户无效");

//        5 商户用户已注册
            case "5":
                return Result.failed("g100003", "商户用户已注册");
            //        6 商户用户系统禁用
            case "6":
                return Result.failed("g200003", "商户用户系统禁用");
            //        7 商户用户系统禁用
            case "7":
                return Result.failed("g000004", "密码错误");
// 12  商户用户游戏在线
            case "12":
                return Result.failed("g100104", "商户用户游戏在线");
// 15  IP被限制
            case "15":
                return Result.failed("g000003", "IP被限制");
// 21 解密错误
            case "21":
                return Result.failed("g000007", "解密错误");
            // 22 商户用户登录禁用
            case "22":
                return Result.failed("g200003", "商户用户登录禁用");
// 32  可选参数错误
            case "32":
                return Result.failed("g000007", "可选参数错误");
            // 33  交易编码已存在
            case "33":
                return Result.failed("g091016", "交易编码已存在");
// 40  维护模式
            case "40":
                return Result.failed("g000001", "维护模式");
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", "系统繁忙");
        }
    }
}
