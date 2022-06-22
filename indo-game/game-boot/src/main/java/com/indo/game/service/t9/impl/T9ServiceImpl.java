package com.indo.game.service.t9.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.t9.T9ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.t9.T9Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public Result t9Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("t9log {} t9Game account:{},t9CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if (0==gameParentPlatform.getIsStart()) {
            return Result.failed("g100101", "游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
        if (null == gamePlatform) {
            return Result.failed("(" + platform + ")平台游戏不存在");
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
            logger.info("站点t9余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                createMemberGame(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                Map<String, Object> params = new HashMap<>();
                params.put("playerID", loginUser.getAccount());
                params.put("actionType", "0");
                params.put("checkValue", getCheckValue("0"));

                // 退出游戏
                commonRequest(getLogOutT9PlayerUrl(), params, loginUser.getId());
            }

            // 启动游戏
            return getPlayerGameUrl(loginUser.getAccount(), platform, "1".equals(isMobileLogin));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("t9logout {} t9Game account:{},t9CodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("playerID", loginUser.getAccount());
            params.put("actionType", "0");
            params.put("checkValue", getCheckValue("0"));

            // 退出游戏
            T9ApiResponseData t9ApiResponseData = commonRequest(getLogOutT9PlayerUrl(), params, loginUser.getId());

            if ("200".equals(t9ApiResponseData.getStatusCode())) {
                return Result.success(t9ApiResponseData);
            } else {
                return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    /**
     * 启动游戏获取游戏URL
     *
     * @return
     */
    private Result getPlayerGameUrl(String playerID, String gameCode, boolean isAPP) {
        T9ApiResponseData t9ApiResponseData = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("playerID", playerID);
            params.put("gameCode", gameCode);
            params.put("isAPP", isAPP);
            params.put("hasLogo", true);
            params.put("walletType", 2);
            params.put("checkValue", getCheckValue(playerID, gameCode));
            t9ApiResponseData = commonRequest(getStartGameUrl(), params, playerID);
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
            return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage());
        }
    }

    /**
     * 创建游戏账号
     *
     * @param cptOpenMember
     * @return
     */
    private Result createMemberGame(CptOpenMember cptOpenMember) {
        // 创建T9账号
        T9ApiResponseData t9ApiResponseData = createT9Member(cptOpenMember);
        if (null == t9ApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }

        if ("200".equals(t9ApiResponseData.getStatusCode())) {
            externalService.saveCptOpenMember(cptOpenMember);
            return Result.success();
        } else {
            return errorCode(t9ApiResponseData.getStatusCode(), t9ApiResponseData.getErrorMessage());
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
            t9ApiResponseData = commonRequest(getCreateT9PlayerUrl(), params, cptOpenMember.getUserId());
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

    public Result errorCode(String errorCode, String errorMessage) {
//        200 成功。                                                Succeed.
        switch (errorCode) {
//        300 会员账号不合法
            case "300":
                return Result.failed("g100002", errorMessage);
//        500 系统错误                                              No authorized to access
            case "500":
                return Result.failed("g100104", errorMessage);

//        600 API参数错误                            Domain is null or the length of domain less than 2.
            case "600":
                return Result.failed("g000007", errorMessage);

//        700 json 格式错误。                                          Failed to pass the domain validation.
            case "700":
                return Result.failed("g009999", errorMessage);

//        800 系统维护中。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "800":
                return Result.failed("g000001", errorMessage);

//        111050 不允许IP            Assertion(SAML) didn't pass the timestamp validation.
            case "111050":
                return Result.failed("g100105", errorMessage);

//        111090 玩家已被锁定。                      Failed to extract the SAML parameters from the encrypted data.
            case "111090":
                return Result.failed("g200003", errorMessage);

//        111120 未知动作。                                            Unknow action.
            case "111120":
                return Result.failed("g300004", errorMessage);

//        111160 Game On Maintenance。                                      The same value as before.
            case "111160":
                return Result.failed("g000001", errorMessage);

//        111170 游戏不存在。                                                Time out.
            case "111170":
                return Result.failed("g100101", errorMessage);

//        111180 游戏无法开启。                                            Read time out.
            case "111180":
                return Result.failed("g100102", errorMessage);

//        111040 商户被冻结。                                            Duplicate transactions.
            case "111040":
                return Result.failed("g200003", errorMessage);

//        191080 玩家已存在。                                          Please try again later.
            case "191080":
                return Result.failed("g100003", errorMessage);

//        191030 玩家不存在。                                            System is maintained.
            case "191030":
                return Result.failed("g010001", errorMessage);

//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
