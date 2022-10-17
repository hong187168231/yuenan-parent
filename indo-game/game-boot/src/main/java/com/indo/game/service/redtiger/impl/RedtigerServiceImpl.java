package com.indo.game.service.redtiger.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.redtiger.RedtigerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class RedtigerServiceImpl implements RedtigerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;

    @Override
    public Result redtigerGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("RTlog RTGame   userId:{},account:{},platform:{},parentName:{}", loginUser.getId(), loginUser.getAccount(), platform,parentName);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if (0==gamePlatform.getIsStart()) {
            return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
        }
        if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
            return Result.failed("g091047", MessageUtils.get("g091047",countryCode));
        }

//        BigDecimal balance = loginUser.getBalance();
//        //验证站点余额
//        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
//            logger.info("站点RT余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
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
                externalService.saveCptOpenMember(cptOpenMember);
//                createMemberGame(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
            }

            // 启动游戏
            return getInitUserJson(gameParentPlatform, ip, loginUser, cptOpenMember, platform, "1".equals(isMobileLogin), countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        return Result.success();
    }


    /**
     * 初始化进入游戏参数
     *
     * @param gameParentPlatform
     * @param ip
     * @param loginUser
     * @param cptOpenMember
     * @param platform
     * @param isApp
     * @return
     */
    private Result getInitUserJson(GameParentPlatform gameParentPlatform, String ip,
                                       LoginInfo loginUser, CptOpenMember cptOpenMember, String platform, boolean isApp,String countryCode) {
        JSONObject json = new JSONObject();
        JSONObject config = new JSONObject();
        JSONObject player = new JSONObject();
        json.put("uuid", cptOpenMember.getPassword());
        json.put("player", player);
        json.put("config", config);

        JSONObject session = new JSONObject();
        JSONObject group = new JSONObject();

        player.put("id", loginUser.getAccount());
        player.put("update", true);
        player.put("nickname", StringUtils.isEmpty(loginUser.getNickName()) ? loginUser.getAccount() : loginUser.getNickName());
        player.put("country", countryCode);
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "en_US";
                    break;
                case "EN":
                    lang = "en_US";
                    break;
                case "CN":
                    lang = "zh_CN";
                    break;
                case "VN":
                    lang = "vi";
                    break;
                case "TW":
                    lang = "zh_TW";
                    break;
                case "TH":
                    lang = "th";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
        player.put("language", lang);
        player.put("currency", gameParentPlatform.getCurrencyType());
        player.put("session", session);
        player.put("group", group);

        session.put("id", cptOpenMember.getPassword());
        session.put("ip", OpenAPIProperties.PROXY_HOST_NAME);
        group.put("action", "clear");
        group.put("id", "");

        JSONObject game = new JSONObject();
        JSONObject table = new JSONObject();
        JSONObject channel = new JSONObject();

        config.put("game", game);
        config.put("channel", channel);

//        game.put("category", platform);
        game.put("table", table);

        table.put("id", platform);
        channel.put("wrapped", isApp);
        channel.put("mobile", isApp);

        JSONObject returnJson = JSONObject.parseObject(commonRequest(getLoginUrl(), json, "login_redtiger"));
        if (null==returnJson) {
            return Result.failed();
        }
        if (returnJson.containsKey("entry")) {
            ApiResponseData responseData = new ApiResponseData();
            String url = returnJson.getString("entry");
            if (url.indexOf("/") == 0) {
                url = url.substring(1);
            }
            responseData.setPathUrl(OpenAPIProperties.REDTIGER_API_URL + url);
            return Result.success(responseData);
        } else {
            JSONObject errors = returnJson.getJSONArray("errors").getJSONObject(0);
            return errorCode(errors.getString("code"), errors.getString("message"), countryCode);
        }
    }


    /**
     * 公共请求
     */
    private String commonRequest(String apiUrl, JSONObject params, String methodType) {
        Base64.Encoder encoder = Base64.getEncoder();
        String key = OpenAPIProperties.REDTIGER_CASINO_KEY+":"+OpenAPIProperties.REDTIGER_API_TOKEN;
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", encoder.encodeToString(key.getBytes(StandardCharsets.UTF_8)));

        return GameUtil.doProxyPostJson(apiUrl, params.toJSONString(), header, methodType);
    }

    private String getLoginUrl() {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.REDTIGER_API_URL).append("ua/v1/");
        url.append(OpenAPIProperties.REDTIGER_CASINO_KEY).append("/").append(OpenAPIProperties.REDTIGER_API_TOKEN);
        return url.toString();
    }

    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        200 成功。                                                Succeed.
        switch (errorCode) {
//         系统错误，应重试，如不断发生应向 Evolution 报告
            case "G.0":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
//        G.1 未知赌场 $casinoKey                                              No authorized to access
            case "G.1":
                return Result.failed("g100107", MessageUtils.get("g100107",countryCode));

//        600 提供赌场 $casinoKey 的 $apiToken 不正确	                            Domain is null or the length of domain less than 2.
            case "G.2":
                return Result.failed("g100107", MessageUtils.get("g100107",countryCode));

//        700 json 没有为赌场 $casinoKey 配置玩家会话创建	。                                          Failed to pass the domain validation.
            case "G.3":
                return Result.failed("g100107", MessageUtils.get("g100107",countryCode));

//        800 无法发行令牌	。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "G.4":
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));

//        111050 无法验证用户            Assertion(SAML) didn't pass the timestamp validation.
            case "G.5":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));

//        111090 玩家已被锁定。                      Failed to extract the SAML parameters from the encrypted data.
            case "G.6":
                return Result.failed("g100004", MessageUtils.get("g100004",countryCode));

//        111120 无法保存玩家数据。                                            Unknow action.
            case "G.7":
                return Result.failed("g100103", MessageUtils.get("g100103",countryCode));

//        111160 由于以下原因无法验证用户身份：$status                             The same value as before.
            case "G.8":
                return Result.failed("g100103", MessageUtils.get("g100103",countryCode));

//        111170 游戏不存在。                                                Time out.
            case "G.9":
                return Result.failed("g000003", MessageUtils.get("g000003",countryCode));

//        111180 游戏无法开启。                                            Read time out.
            case "G.10":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));

//        111040 商户被冻结。                                            Duplicate transactions.
            case "G.11":
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
