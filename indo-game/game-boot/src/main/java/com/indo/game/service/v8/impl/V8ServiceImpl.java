package com.indo.game.service.v8.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5Encoder;
import com.indo.game.common.util.V8Encrypt;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.v8.V8Service;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class V8ServiceImpl implements V8Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Result v8Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("v8_log {} v8Game account:{},v8CodeId:{}", parentName, loginUser.getAccount(), platform);
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
            logger.info("站点v8余额不足，当前用户memid: {},nickName: {},balance: {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                externalService.saveCptOpenMember(cptOpenMember);

                // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
                return createMemberGame(cptOpenMember, platform, ip, true,balance);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                // 先退出游戏
                GameUtil.httpGetWithCookies(getLoginOutUrl(loginUser.getAccount()), null, null);

                // 请求地址
                return createMemberGame(cptOpenMember, platform, ip, false,balance);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("v8_logout {} v8Game account:{},v8CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        try {

            String result = GameUtil.httpGetWithCookies(getLoginOutUrl(loginUser.getAccount()), null, null);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if (null == jsonObject || null == jsonObject.getJSONObject("d")) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            JSONObject jsonData = jsonObject.getJSONObject("d");
            if (0 == jsonData.getInteger("code")) {
                return Result.success();
            } else {
                return errorCode(jsonData.getInteger("code").toString(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }


    @Override
    public Result crebit(LoginInfo loginUser, String platform, BigDecimal money, String ip) {
        logger.info("v8_crebit {} v8Game account:{},v8CodeId:{}", money, loginUser.getAccount(), platform);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            String paySerialno = GeneratorIdUtil.generateId();
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(loginUser.getAccount());
            // V8 游戏下分
            String result = GameUtil.httpGetWithCookies(getCreditUrl(loginUser.getAccount(), money, paySerialno), null, null);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if (null == jsonObject || null == jsonObject.getJSONObject("d")) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            // 查询下分订单是否成功
            if (checkCreditIsSuccess(getOrderStatusUrl(paySerialno))) {
                // 生成订单数据
                Txns txns = getInitTxns(platformGameParent, paySerialno, memBaseinfo.getAccount(), money, memBaseinfo.getBalance(), ip, platform);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return Result.failed("g100104", "网络繁忙，请稍后重试！");
    }

    @Override
    public Result balance(LoginInfo loginUser, String platform, String ip) {
        logger.info("v8_balance {} v8Game account:{},v8CodeId:{}", ip, loginUser.getAccount(), platform);
        try {
            // V8 游戏玩家情况
            String result = GameUtil.httpGetWithCookies(getUserBaseUrl(loginUser.getAccount()), null, null);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if (null == jsonObject || null == jsonObject.getJSONObject("d")) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            JSONObject jsonData = jsonObject.getJSONObject("d");
            if (0 == jsonData.getInteger("code")) {
                return Result.success(jsonData);
            } else {
                return errorCode(jsonData.getInteger("code").toString(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return Result.failed("g100104", "网络繁忙，请稍后重试！");
    }

    /**
     * 查询V8订单状态是否成功
     *
     * @param url url
     * @return boolean
     */
    private boolean checkCreditIsSuccess(String url) {
        JSONObject creditJson = JSONObject.parseObject(GameUtil.httpGetWithCookies(url, null, null));
        if (null == creditJson || null == creditJson.getJSONObject("d")) {
            return false;
        }
        // 成功 状态码（-1：不存在、0：成功、2: 失败、3:正在处理中）
        return creditJson.getJSONObject("d").getInteger("status") == 3
                || creditJson.getJSONObject("d").getInteger("status") == 0;
    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, String platform, String ip, boolean isCreateUser,BigDecimal balance) {
        try {
            //
            String result = GameUtil.httpGetWithCookies(getLoginUrl(cptOpenMember.getUserName(), platform, ip,balance), null, null);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if (null == jsonObject || null == jsonObject.getJSONObject("d")) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            JSONObject jsonData = jsonObject.getJSONObject("d");
            if (0 == jsonData.getInteger("code")) {
                if (isCreateUser) {
                    externalService.saveCptOpenMember(cptOpenMember);
                }
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jsonData.getString("url"));
                return Result.success(responseData);
            } else {
                return errorCode(jsonData.getInteger("code").toString(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }

    }

    /**
     * 登录游戏，如果米有账号自动创建
     * s=0&account=111111&money=100&or
     * derid=1000120170306143036949111111
     * &ip=127.0.0.1& lineCode
     * =text11&KindID=0
     *
     * @return String
     */
    private String getLoginUrl(String userAccount, String gameId, String ip,BigDecimal balance) throws Exception {
        long timestamp = System.currentTimeMillis();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.V8_API_URL);
        url.append("?agent=").append(OpenAPIProperties.V8_AGENT);
        url.append("&timestamp=").append(timestamp);

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("s=0&account=").append(userAccount);
        urlParams.append("&money=").append(balance);
        urlParams.append("&orderid=").append(getOrderid(userAccount));
        urlParams.append("&ip=").append(ip);
        urlParams.append("&lineCode=").append(OpenAPIProperties.V8_LINE_CODE);
        logger.info("v8Game getLoginUrl登录请求:lineCode:{},isPlatformLogin:{}",OpenAPIProperties.V8_LINE_CODE, OpenAPIProperties.V8_IS_PLATFORM_LOGIN);
        if(OpenAPIProperties.V8_IS_PLATFORM_LOGIN.equals("Y")){
            urlParams.append("&KindID=0");
        }else {
            urlParams.append("&KindID=").append(gameId);
        }
        logger.info("v8Game getLoginUrl登录请求加密前:urlParams:{}", urlParams.toString());
        url.append("&param=").append(V8Encrypt.AESEncrypt(urlParams.toString(), OpenAPIProperties.V8_DESKEY));
        url.append("&key=");
        url.append(getKey(timestamp));
        logger.info("v8Game getLoginUrl登录请求:userAccount:{},gameId:{},ip:{},url:{}", userAccount, gameId, ip, url.toString());
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param account account
     * @return String
     */
    private String getLoginOutUrl(String account) throws Exception {
        long timestamp = System.currentTimeMillis();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.V8_API_URL);
        url.append("?agent=").append(OpenAPIProperties.V8_AGENT);
        url.append("&timestamp=").append(timestamp);

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("s=8&account=").append(account);

        url.append("&param=").append(V8Encrypt.AESEncrypt(urlParams.toString(), OpenAPIProperties.V8_DESKEY));
        url.append("&key=");
        url.append(getKey(timestamp));
        logger.info("v8Game getLoginUrl玩家退出游戏:userAccount:{},url:{}", account,  url.toString());
        return url.toString();
    }

    /**
     * 玩家下分请求地址
     *
     * @param account account
     * @return String
     */
    private String getCreditUrl(String account, BigDecimal money, String orderid) throws Exception {
        long timestamp = System.currentTimeMillis();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.V8_API_URL);
        url.append("?agent=").append(OpenAPIProperties.V8_AGENT);
        url.append("&timestamp=").append(timestamp);

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("s=3&account=").append(account);
        urlParams.append("&money=").append(money);
        urlParams.append("&orderid=").append(orderid);
        url.append("&param=").append(V8Encrypt.AESEncrypt(urlParams.toString(), OpenAPIProperties.V8_DESKEY));
        url.append("&key=");
        url.append(getKey(timestamp));
        logger.info("v8Game getLoginUrl玩家下分请求:userAccount:{},money:{},orderid:{},url:{}", account, money, orderid, url.toString());
        return url.toString();
    }

    /**
     * 玩家游戏余额和在线状态地址
     *
     * @param account account
     * @return String
     */
    private String getUserBaseUrl(String account) throws Exception {
        long timestamp = System.currentTimeMillis();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.V8_API_URL);
        url.append("?agent=").append(OpenAPIProperties.V8_AGENT);
        url.append("&timestamp=").append(timestamp);

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("s=7&account=").append(account);
        url.append("&param=").append(V8Encrypt.AESEncrypt(urlParams.toString(), OpenAPIProperties.V8_DESKEY));
        url.append("&key=");
        url.append(getKey(timestamp));
        logger.info("v8Game getLoginUrl玩家游戏余额和在线状态地址请求:userAccount:{},url:{}", account, url.toString());
        return url.toString();
    }

    /**
     * 玩家上下分订单查询
     *
     * @param orderid orderid
     * @return String
     */
    private String getOrderStatusUrl(String orderid) throws Exception {
        long timestamp = System.currentTimeMillis();
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.V8_API_URL);
        url.append("?agent=").append(OpenAPIProperties.V8_AGENT);
        url.append("&timestamp=").append(timestamp);

        StringBuilder urlParams = new StringBuilder();
        urlParams.append("s=4&orderid=").append(orderid);
        url.append("&param=").append(V8Encrypt.AESEncrypt(urlParams.toString(), OpenAPIProperties.V8_DESKEY));
        url.append("&key=");
        url.append(getKey(timestamp));
        logger.info("v8Game getLoginUrl玩家上下分订单查询请求:orderid:{},url:{}", orderid, url.toString());
        return url.toString();
    }


    /**
     * 生成请求订单流水号
     * ：流水号（格 式：代理编 号+yyyyMMddHHmmssSSS+account）
     *
     * @param account account
     * @return String
     */
    private String getOrderid(String account) {
        return OpenAPIProperties.V8_AGENT + DateUtils.format(new Date(), DateUtils.longFormat1) + account;
    }

    /**
     * 生成请求参数key
     *
     * @param timestamp 13位时间戳
     * @return key
     */
    private String getKey(long timestamp) {
        return MD5Encoder.encode(OpenAPIProperties.V8_AGENT + timestamp + OpenAPIProperties.V8_MD5KEY);
    }

    /**
     * 初始化第三方游戏交互订单数据
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @param playerID           playerID
     * @param pointAmount        pointAmount
     * @param balance            balance
     * @param ip                 ip
     * @return Txns
     */
    private Txns getInitTxns(GameParentPlatform gameParentPlatform, String paySerialno, String playerID,
                             BigDecimal pointAmount, BigDecimal balance, String ip, String platform) {
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(paySerialno);
        //此交易是否是投注 true是投注 false 否
        txns.setBet(false);
        //玩家 ID
        txns.setUserId(playerID);
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
        txns.setBetAmount(pointAmount);
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        //玩家下注时间
        txns.setBetTime(dateStr);
        //有效投注金额 或 投注面值
        txns.setTurnover(pointAmount);
        //辨认交易时间依据
        txns.setTxTime(dateStr);
        //操作名称
        txns.setMethod("Settle");
        txns.setStatus("Running");
        //余额
        txns.setBalance(balance);
        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(ip);
        return txns;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.V8_PLATFORM_CODE);
    }

    public Result errorCode(String errorCode, String errorMessage) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        2 Key 验证失败
            case "2":
                return Result.failed("g100107", errorMessage);
//        9 AgentId 不存在 玩家账号与 AgentId 不匹配 (详见 Chapter             No authorized to access
            case "9":
                return Result.failed("g000007", errorMessage);

//        104 執⾏营运商于 auth 回传错误或禁用的货币                            Domain is null or the length of domain less than 2.
            case "104":
                return Result.failed("g100001", errorMessage);

//        101 会员账号不存在/不在在线                                   Failed to pass the domain validation.
            case "101":
                return Result.failed("g010001", errorMessage);

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
