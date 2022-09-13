package com.indo.game.service.t9.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.t9.T9ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.t9.T9Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * T9游戏
 */
@Service("t9Service")
public class T9ServiceImpl implements T9Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public Result t9Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("t9log {} t9Game account:{},t9CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
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
//            logger.info("站点t9余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                return createMemberGame(cptOpenMember, platform, "1".equals(isMobileLogin), countryCode, gameParentPlatform);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

//                Map<String, Object> params = new HashMap<>();
//                params.put("playerID", loginUser.getAccount());
//                params.put("actionType", "0");
//                params.put("checkValue", getCheckValue("0"));
//
//                // 退出游戏
//                commonRequest(getLogOutT9PlayerUrl(), params, loginUser.getId());
            }

            // 启动游戏
            return getPlayerGameUrl(loginUser.getAccount(), platform, "1".equals(isMobileLogin), countryCode, gameParentPlatform);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("t9logout  t9Game account:{},platform:{}",  account, platform);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("playerID", account);
            params.put("actionType", "0");
            params.put("checkValue", getCheckValue("0"));
            logger.info("T9log  logout退出登录 urlapi:{},paramsMap:{},loginUser:{}", getLogOutT9PlayerUrl(), params,account);
            // 退出游戏
            T9ApiResponseData t9ApiResponseData = commonRequest(getLogOutT9PlayerUrl(), params, 0);
            logger.info("T9log  logout退出登录返回 t9ApiResponseData:{}", JSONObject.toJSONString(t9ApiResponseData));
            if (null == t9ApiResponseData) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if ("200".equals(t9ApiResponseData.getStatusCode())) {
                return Result.success(t9ApiResponseData);
            } else {
                return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage(),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 启动游戏获取游戏URL
     *
     * @return
     */
    private Result getPlayerGameUrl(String playerID, String gameCode, boolean isAPP,String countryCode,GameParentPlatform gameParentPlatform) {
        T9ApiResponseData t9ApiResponseData = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("playerID", playerID);
            params.put("gameCode", gameCode);
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
                        lang = "vi_VN";
                        break;
                    case "TW":
                        lang = "zh_TW";
                        break;
                    case "TH":
                        lang = "th_TH";
                        break;
                    case "ID":
                        lang = "in_ID";
                        break;
                    case "MY":
                        lang = "ms_MY";
                        break;
                    case "KR":
                        lang = "ko_KR";
                        break;
                    case "JP":
                        lang = "ja_JP";
                        break;
                    default:
                        lang = gameParentPlatform.getLanguageType();
                        break;
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            params.put("lang", lang);
            params.put("isAPP", isAPP);
            params.put("hasLogo", true);
            params.put("walletType", 2);
            params.put("checkValue", getCheckValue(playerID, gameCode));
            logger.info("T9log  getPlayerGameUrl启动游戏获取游戏URL urlapi:{},paramsMap:{},loginUser:{}", getStartGameUrl(), params,playerID);
            t9ApiResponseData = commonRequest(getStartGameUrl(), params, playerID);
            logger.info("T9log  getPlayerGameUrl启动游戏获取游戏URL返回 t9ApiResponseData:{}", JSONObject.toJSONString(t9ApiResponseData));
        } catch (Exception e) {
            logger.error("t9log getPlayerGameUrl:{}", e);
            e.printStackTrace();
        }
        if (null == t9ApiResponseData) {
            return Result.failed("g200001", "登录操作失败");
        }

        if ("200".equals(t9ApiResponseData.getStatusCode())) {
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(t9ApiResponseData.getData().toString());
            return Result.success(responseData);
        } else {
            return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage(),countryCode);
        }
    }

    /**
     * 创建游戏账号
     *
     * @param cptOpenMember
     * @return
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, String platform, boolean isAPP,String countryCode,GameParentPlatform gameParentPlatform) {
        // 创建T9账号
        T9ApiResponseData t9ApiResponseData = createT9Member(cptOpenMember);
        if (null == t9ApiResponseData) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }

        if ("200".equals(t9ApiResponseData.getStatusCode())) {
            externalService.saveCptOpenMember(cptOpenMember);
            return getPlayerGameUrl(cptOpenMember.getUserName(), platform, isAPP, countryCode, gameParentPlatform);
        } else {
            return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage(),countryCode);
        }
    }

    /**
     * 创建T9账号
     * 、
     *
     * @param cptOpenMember、
     * @return
     */
    private T9ApiResponseData createT9Member(CptOpenMember cptOpenMember) {

        T9ApiResponseData t9ApiResponseData = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("playerID", cptOpenMember.getUserName());
            params.put("checkValue", getCheckValue(cptOpenMember.getUserName()));
            logger.info("T9log  createT9Member创建玩家 urlapi:{},paramsMap:{},loginUser:{}", getCreateT9PlayerUrl(), params,cptOpenMember);
            t9ApiResponseData = commonRequest(getCreateT9PlayerUrl(), params, cptOpenMember.getUserId());
            logger.info("T9log  createT9Member创建玩家返回 t9ApiResponseData:{}", JSONObject.toJSONString(t9ApiResponseData));
        } catch (Exception e) {
            logger.error("t9log createT9Member:{}", e);
            e.printStackTrace();
        }
        return t9ApiResponseData;

    }

    /**
     * 公共请求
     */
    private T9ApiResponseData commonRequest(String apiUrl, Map<String, Object> params, Object userId) throws Exception {
        logger.info("t9log  commonRequest userId:{},paramsMap:{}", userId, params.toString());
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("agent", OpenAPIProperties.T9_AGENT);
        header.put("domain", OpenAPIProperties.T9_DOMAIN);

        String json = GameUtil.postJson(apiUrl, params, header);
        T9ApiResponseData t9ApiResponseData = JSONObject.parseObject(json, T9ApiResponseData.class);
        return t9ApiResponseData;
    }

    /**
     * 获取请求 header HashKey
     *
     * @param params
     * @return
     */
    private String getCheckValue(String... params) throws UnsupportedEncodingException {
        StringBuilder checkValue = new StringBuilder();
        checkValue.append(OpenAPIProperties.T9_MERCHANT_CODE);
        checkValue.append(OpenAPIProperties.T9_AGENT);
        for (String value : params) {
            checkValue.append(value);
        }

        return DigestUtils.md5DigestAsHex(checkValue.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建玩家API地址
     *
     * @return
     */
    private String getCreateT9PlayerUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.T9_API_URL).append("/party/createplayer");
        return builder.toString();
    }

    /**
     * 启动游戏地址
     *
     * @return
     */
    private String getStartGameUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.T9_API_URL).append("/party/playgame");
        return builder.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @return
     */
    private String getLogOutT9PlayerUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.T9_API_URL).append("/party/kickplayer");
        return builder.toString();
    }

    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        200 成功。                                                Succeed.
        switch (errorCode) {
//        300 会员账号不合法
            case "300":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
//        500 系统错误                                              No authorized to access
            case "500":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));

//        600 API参数错误                            Domain is null or the length of domain less than 2.
            case "600":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));

//        700 json 格式错误。                                          Failed to pass the domain validation.
            case "700":
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));

//        800 系统维护中。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "800":
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));

//        111050 不允许IP            Assertion(SAML) didn't pass the timestamp validation.
            case "111050":
                return Result.failed("g100105", MessageUtils.get("g100105",countryCode));

//        111090 玩家已被锁定。                      Failed to extract the SAML parameters from the encrypted data.
            case "111090":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));

//        111120 未知动作。                                            Unknow action.
            case "111120":
                return Result.failed("g300004", MessageUtils.get("g300004",countryCode));

//        111160 Game On Maintenance。                                      The same value as before.
            case "111160":
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));

//        111170 游戏不存在。                                                Time out.
            case "111170":
                return Result.failed("g100101", MessageUtils.get("g100101",countryCode));

//        111180 游戏无法开启。                                            Read time out.
            case "111180":
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));

//        111040 商户被冻结。                                            Duplicate transactions.
            case "111040":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));

//        191080 玩家已存在。                                          Please try again later.
            case "191080":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));

//        191030 玩家不存在。                                            System is maintained.
            case "191030":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
