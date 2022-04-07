package com.indo.game.service.jili.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.JiliAESEncrypt;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.jili.JiliApiResponse;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.jili.JiliService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JiliServiceImpl implements JiliService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;

    @Override
    public Result jiliGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("jililog {} jiliGame account:{},jiliCodeId:{}", parentName, loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if (gameParentPlatform.getIsStart().equals(0)) {
            return Result.failed("g100101", "游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        if (!platform.equals(parentName)) {
            GamePlatform gamePlatform;
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if (gamePlatform.getIsStart().equals(0)) {
                return Result.failed("g100102", "游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
            }
        }

        BigDecimal balance = loginUser.getBalance();
        //验证站点余额
        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("站点jili余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                return createMemberGame(cptOpenMember, platform, gameParentPlatform.getLanguageType());
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);

                Map<String, Object> params = new HashMap<>();
                params.put("Account", loginUser.getAccount());
                GameUtil.postForm4PP(getLoginOutUrl(loginUser.getAccount()), params, null);
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(getStartGame(cptOpenMember, platform, gameParentPlatform.getLanguageType()));
                return Result.success(responseData);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("jililogout {} jiliGame account:{},jiliCodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("Account", loginUser.getAccount());
            String json = GameUtil.postForm4PP(getLoginOutUrl(loginUser.getAccount()), params, null);
            JiliApiResponse jiliApiResponse = JSONObject.parseObject(json, JiliApiResponse.class);
            if (0 == jiliApiResponse.getErrorCode()) {
                return Result.success();
            } else {
                return errorCode(jiliApiResponse.getErrorCode().toString(), jiliApiResponse.getMessage());
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
    private Result createMemberGame(CptOpenMember cptOpenMember, String platform, String lang) {
        //
        JiliApiResponse jiliApiResponse = createJiliMember(cptOpenMember, platform, lang);
        if (null == jiliApiResponse) {
            return Result.failed("g091087", "第三方请求异常！");
        }

        if (0 == jiliApiResponse.getErrorCode()) {
            // 请求URL
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(getStartGame(cptOpenMember, platform, lang));
            return Result.success(responseData);
        } else {
            return errorCode(jiliApiResponse.getErrorCode().toString(), jiliApiResponse.getMessage());
        }
    }

    /**
     * 创建jili 账号
     * 、
     *
     * @param cptOpenMember、 cptOpenMember
     * @return JiliApiResponse
     */
    private JiliApiResponse createJiliMember(CptOpenMember cptOpenMember, String platform, String lang) {

        JiliApiResponse jiliApiResponse = null;
        try {
            String result = GameUtil.httpGetWithCookies(getLoginUrl(cptOpenMember.getPassword(), platform, lang), null, null);
            jiliApiResponse = JSONObject.parseObject(result, JiliApiResponse.class);
        } catch (Exception e) {
            logger.error("jililog createJiliMember:{}", e);
            e.printStackTrace();
        }
        return jiliApiResponse;
    }

    /**
     * 获取登录URL 账号
     * 、
     *
     * @param cptOpenMember、 cptOpenMember
     * @return url
     */
    private String getStartGame(CptOpenMember cptOpenMember, String platform, String lang) {

        try {
            String result = GameUtil.httpGetWithCookies(getStartGameUrl(cptOpenMember.getPassword(), platform, lang), null, null);
            JiliApiResponse jiliApiResponse = JSONObject.parseObject(result, JiliApiResponse.class);
            if (0 == jiliApiResponse.getErrorCode()) {
                return jiliApiResponse.getData().toString();
            }
        } catch (Exception e) {
            logger.error("jililog getStartGame:{}", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 登录游戏，如果米有账号自动创建
     *
     * @return String
     */
    private String getLoginUrl(String userAccount, String gameId, String lang) {
        lang = StringUtils.isEmpty(lang) ? "zh-CN" : lang;
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.JILI_API_URL).append("singleWallet/Login?");
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("Token=").append(userAccount);
        urlParams.append("&GameId=").append(gameId);
        urlParams.append("&Lang=").append(lang);
        urlParams.append("&AgentId=").append(OpenAPIProperties.JILI_AGENT_ID);
        url.append(urlParams);
        url.append("&Key=");
        url.append(JiliAESEncrypt.encrypt(urlParams.toString(), OpenAPIProperties.JILI_AGENT_ID, OpenAPIProperties.JILI_AGENT_KEY));
        return url.toString();
    }

    /**
     * 启动游戏
     *
     * @param userAccount
     * @param gameId
     * @param lang
     * @return
     */
    private String getStartGameUrl(String userAccount, String gameId, String lang) {
        lang = StringUtils.isEmpty(lang) ? "zh-CN" : lang;
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.JILI_API_URL).append("singleWallet/LoginWithoutRedirect?");
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("Token=").append(userAccount);
        urlParams.append("&GameId=").append(gameId);
        urlParams.append("&Lang=").append(lang);
        urlParams.append("&AgentId=").append(OpenAPIProperties.JILI_AGENT_ID);
        url.append(urlParams);
        url.append("&Key=");
        url.append(JiliAESEncrypt.encrypt(urlParams.toString(), OpenAPIProperties.JILI_AGENT_ID, OpenAPIProperties.JILI_AGENT_KEY));
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param account account
     * @return String
     */
    private String getLoginOutUrl(String account) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.JILI_API_URL).append("api1/KickMember");
        StringBuilder urlParams = new StringBuilder();
        urlParams.append("Account=").append(account);
        urlParams.append("&AgentId=").append(OpenAPIProperties.JILI_AGENT_ID);
        url.append(urlParams);
        url.append("&Key=");
        url.append(JiliAESEncrypt.encrypt(urlParams.toString(), OpenAPIProperties.JILI_AGENT_ID, OpenAPIProperties.JILI_AGENT_KEY));
        return url.toString();
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
