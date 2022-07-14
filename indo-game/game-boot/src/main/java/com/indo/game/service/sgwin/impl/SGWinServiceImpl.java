//package com.indo.game.service.sgwin.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.indo.common.config.OpenAPIProperties;
//import com.indo.common.pojo.bo.LoginInfo;
//import com.indo.common.redis.utils.GeneratorIdUtil;
//import com.indo.common.result.Result;
//import com.indo.common.utils.DateUtils;
//import com.indo.common.utils.GameUtil;
//import com.indo.common.utils.RandomUtil;
//import com.indo.common.utils.SignMd5Utils;
//import com.indo.common.utils.encrypt.MD5;
//import com.indo.game.common.util.FCHashAESEncrypt;
//import com.indo.game.common.util.SAJEncryption;
//import com.indo.game.common.util.XmlUtil;
//import com.indo.game.pojo.dto.ae.AeApiResponseData;
//import com.indo.game.pojo.dto.comm.ApiResponseData;
//import com.indo.game.pojo.dto.sa.SaKickUserResp;
//import com.indo.game.pojo.dto.sa.SaLoginResp;
//import com.indo.game.pojo.entity.CptOpenMember;
//import com.indo.game.pojo.entity.manage.GameParentPlatform;
//import com.indo.game.pojo.entity.manage.GamePlatform;
//import com.indo.game.service.common.GameCommonService;
//import com.indo.game.service.cptopenmember.CptOpenMemberService;
//import com.indo.game.service.sgwin.SGWinService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.util.*;
//
//@Service
//public class SGWinServiceImpl implements SGWinService {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Resource
//    private CptOpenMemberService externalService;
//    @Resource
//    private GameCommonService gameCommonService;
//
//
//    @Override
//    public Result sgwinGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
//        logger.info("SGWinlog  {} SGWinGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
//        // 是否开售校验
//        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(parentName);
//        if (null == platformGameParent) {
//            return Result.failed("(" + parentName + ")游戏平台不存在");
//        }
//        if ("0".equals(platformGameParent.getIsStart())) {
//            return Result.failed("g" + "100101", "游戏平台未启用");
//        }
//        if ("1".equals(platformGameParent.getIsOpenMaintenance())) {
//            return Result.failed("g000001", platformGameParent.getMaintenanceContent());
//        }
//        GamePlatform gamePlatform = new GamePlatform();
//        if (!platform.equals(parentName)) {
//            // 是否开售校验
//            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
//            if (null == gamePlatform) {
//                return Result.failed("(" + platform + ")平台游戏不存在");
//            }
//            if ("0".equals(gamePlatform.getIsStart())) {
//                return Result.failed("g" + "100102", "游戏未启用");
//            }
//            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
//                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
//            }
//        }
//        BigDecimal balance = loginUser.getBalance();
//        //验证站点棋牌余额
//        if (null == balance || BigDecimal.ZERO.equals(balance)) {
//            logger.info("站点PG余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", "会员余额不足");
//        }
//        try {
//
//            // 验证且绑定（AE-CPT第三方会员关系）
//            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
//            if (cptOpenMember == null) {
//                cptOpenMember = new CptOpenMember();
//                cptOpenMember.setUserName(loginUser.getAccount());
//                cptOpenMember.setUserId(loginUser.getId().intValue());
//                cptOpenMember.setCreateTime(new Date());
//                cptOpenMember.setLoginTime(new Date());
//                cptOpenMember.setType(parentName);
//                //创建玩家
//                externalService.saveCptOpenMember(cptOpenMember);
//            } else {
//                CptOpenMember updateCptOpenMember = new CptOpenMember();
//                updateCptOpenMember.setId(cptOpenMember.getId());
//                updateCptOpenMember.setLoginTime(new Date());
//                externalService.updateCptOpenMember(updateCptOpenMember);
//                logout(loginUser, platform, ip);
//            }
//
//            JSONObject apiResponseData = gameLogin(platformGameParent, cptOpenMember);
//            if (null == apiResponseData || !"200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
//                return Result.failed("g091087", "第三方请求异常！");
//            }
//            //登录
//            ApiResponseData responseData = new ApiResponseData();
//            responseData.setPathUrl(apiResponseData.getJSONObject("resp_data").getString("url"));
//            return Result.success(responseData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failed("g100104", "网络繁忙，请稍后重试！");
//        }
//    }
//
//    /**
//     * {
//     *    "defaultGame": "HK6",
//     *    "defaultBgColor":"black",
//     *    "defaultColor":"pink",
//     *    "backButton": true,
//     *    "backUrl": "https://www.xxxx.com",
//     *    "range":"A",
//     *    "editRange":false,
//     *    "tesing":1
//     * }
//     * @param platformGameParent
//     * @param cptOpenMember
//     * @return
//     */
//    /**
//     * 调用API登录
//     */
//    private AeApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
//                                        CptOpenMember cptOpenMember, String isMobileLogin) {
//        StringBuilder urlParams = new StringBuilder();
//        urlParams.append("agentID").append("=").append(OpenAPIProperties.SGWIN_AGENT_ID);
//        urlParams.append("root").append("=").append(OpenAPIProperties.SGWIN_AGENT);
//        urlParams.append("username").append("=").append(cptOpenMember.getUserName().toUpperCase(Locale.ROOT));
//        urlParams.append("hash").append(MD5.md5("agentID="));
//
//        // 加密
//        StringBuilder builder = new StringBuilder();
//        builder.append(OpenAPIProperties.AE_MERCHANT_ID).append(platformGameParent.getCurrencyType()).append(currentTime);
//        builder.append(cptOpenMemberm.getUserName()).append("0").append(params.get("device")).append(gamePlatform.getPlatformCode());
//        builder.append(platformGameParent.getLanguageType()).append(Base64.getEncoder().encodeToString(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
//
//        String sign = MD5.md5(builder.toString());
//        params.put("gameId", gamePlatform.getPlatformCode());
//        params.put("hash", sign);
//        params.put("language", platformGameParent.getLanguageType());
//        String jsonStr = JSON.toJSONString(params);
//        AeApiResponseData aeApiResponseData = null;
//        try {
//            StringBuilder apiUrl = new StringBuilder();
//            apiUrl.append(OpenAPIProperties.SGWIN_API_URL).append("/api/login").append("?");
//            aeApiResponseData = commonRequest(apiUrl.toString(), jsonStr, cptOpenMemberm.getUserId(), "gameLogin");
//        } catch (Exception e) {
//            logger.error("aelog aeGameLogin:{}", e);
//            e.printStackTrace();
//        }
//        return aeApiResponseData;
//    }
//
//
//
//    /**
//     * 强迫登出玩家
//     */
//    @Override
//    public Result logout(LoginInfo loginUser, String platform, String ip) {
//        try {
//            Map<String, String> map = new HashMap<String, String>();
//            Long dataTime =  System.currentTimeMillis() / 1000;
//            Integer random = RandomUtil.getRandomOne(7);
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(OpenAPIProperties.BL_KEY_SECRET).append(random).append(dataTime);
//            String sign = SignMd5Utils.getSha1(stringBuilder.toString()).toLowerCase();
//            map.put("player_account", loginUser.getAccount());
//            map.put("AccessKeyId", OpenAPIProperties.BL_KEY_ID);
//            map.put("Nonce", random + "");
//            map.put("Sign", sign);
//            StringBuilder apiUrl = new StringBuilder();
//            apiUrl.append(OpenAPIProperties.BL_API_URL).append("/v1/player/logout");
//            JSONObject apiResponseData = commonRequest(apiUrl.toString(), map, loginUser.getId().intValue(), "BLlogout");
//            if (null == apiResponseData || !"200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
//                return Result.failed("g091087", "第三方请求异常！");
//            }
//            if ("200".equals(apiResponseData.getJSONObject("resp_msg").getString("code"))) {
//                return Result.success();
//            }
//        } catch (Exception e) {
//            logger.error("BLlog  BLlogout:{}", e);
//            e.printStackTrace();
//            return Result.failed();
//        }
//        return Result.failed();
//    }
//
//    /**
//     * 公共请求
//     */
//    public AeApiResponseData commonRequest(String apiUrl, String jsonStr, Integer userId, String type) throws Exception {
//        logger.info("sgwinlog {} commonRequest userId:{},paramsMap:{}", userId, jsonStr);
//        AeApiResponseData aeApiResponseData = null;
//        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
//            apiUrl, jsonStr, type, userId);
//        logger.info("aelog apiResponse:" + resultString);
//        if (StringUtils.isNotEmpty(resultString)) {
//            aeApiResponseData = JSONObject.parseObject(resultString, AeApiResponseData.class);
//            logger.info("aelog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
//                userId, type, null, jsonStr, resultString, JSONObject.toJSONString(aeApiResponseData));
//        }
//        return aeApiResponseData;
//    }
//}
