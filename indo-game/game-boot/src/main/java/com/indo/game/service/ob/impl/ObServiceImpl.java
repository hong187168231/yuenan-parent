package com.indo.game.service.ob.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.vo.callback.ob.ObApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ob.ObService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * OB电子
 *
 * @author
 */
@Service
public class ObServiceImpl implements ObService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;


    /**
     * 登录游戏AE电子
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result obGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("OB体育log  obGame loginUser:{}, ip:{}, platform:{}, parentName:{}, isMobileLogin:{}", loginUser,ip,platform,parentName,isMobileLogin);
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
//        //验证站点棋牌余额
//        if (null == balance || BigDecimal.ZERO == balance) {
//            logger.info("站点AE余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }

        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(gameParentPlatform, gamePlatform, ip, cptOpenMember, isMobileLogin, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                //先登出
                logout(loginUser, platform, ip, countryCode);
            }
            //登录
            return initGame(gameParentPlatform, gamePlatform, cptOpenMember, isMobileLogin, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 登录逻辑
     */
    private Result initGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                            CptOpenMember cptOpenMember, String isMobileLogin,String countryCode) {
        ObApiResponseData obApiResponseData = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
        if (null == obApiResponseData) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (("0000").equals(obApiResponseData.getCode())) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonObject = JSONObject.parseObject(obApiResponseData.getData());
            responseData.setPathUrl(jsonObject.getString("loginUrl"));
            return Result.success(responseData);
        } else {
            return errorCode(obApiResponseData.getCode(), obApiResponseData.getMsg(), countryCode);
        }
    }

    /**
     * 调用API登录
     */
    private ObApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMember, String isMobileLogin) {
        long currentTime = System.currentTimeMillis();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", cptOpenMember.getUserName());
        params.put("merchantCode", OpenAPIProperties.OB_MERCHANT_CODE);
        //设备。0: 行动装置 1: 网页
        if ("1".equals(isMobileLogin)) {
            params.put("terminal", "mobile");
        } else {
//            false 桌面设备登入
            params.put("terminal", "pc");
        }
        params.put("timestamp", currentTime + "");
        // 加密
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.OB_MERCHANT_CODE).append("&").append(cptOpenMember.getUserName());
        builder.append("&").append(params.get("terminal")).append("&").append(currentTime);
        String signKey = MD5.md5(builder.toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(signKey).append("&").append(OpenAPIProperties.OB_MERCHANT_KEY);
        String sign = MD5.md5(stringBuilder.toString());
        params.put("signature", sign);
        ObApiResponseData obApiResponseData = null;
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.OB_API_URL).append("/api/user/login");
            logger.info("OB体育log  登录gameLogin输入 apiUrl:{}, platform:{}", apiUrl.toString(), params);
            obApiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMember.getUserId(), "gameLogin");
            logger.info("OB体育log  登录gameLogin返回 obApiResponseData:{}", obApiResponseData);
        } catch (Exception e) {
            logger.error("OBlog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return obApiResponseData;
    }

    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin,String countryCode) {
        ObApiResponseData obApiResponseData = createMember(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
        if (null == obApiResponseData) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (("0000").equals(obApiResponseData.getCode())||("2003").equals(obApiResponseData.getCode())) {
            externalService.saveCptOpenMember(cptOpenMember);
            return initGame(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, countryCode);
        } else {
            return errorCode(obApiResponseData.getCode(), obApiResponseData.getMsg(), countryCode);
        }
    }

    /**
     * 调用API创建账号
     */
    private ObApiResponseData createMember(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {

        long currentTime = System.currentTimeMillis();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", cptOpenMember.getUserName());
        params.put("nickname", cptOpenMember.getUserName());
        params.put("merchantCode", OpenAPIProperties.OB_MERCHANT_CODE);
        params.put("timestamp", currentTime + "");
        params.put("currency", platformGameParent.getCurrencyType());
        StringBuilder builder = new StringBuilder();
        builder.append(cptOpenMember.getUserName()).append("&").append(OpenAPIProperties.OB_MERCHANT_CODE).append("&").append(currentTime);
        String signKey = MD5.md5(builder.toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(signKey).append("&").append(OpenAPIProperties.OB_MERCHANT_KEY);
        String sign = MD5.md5(stringBuilder.toString());
        params.put("signature", sign);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.OB_API_URL).append("/api/user/create");
        ObApiResponseData obApiResponseData = null;
        try {
            logger.info("OB体育log  创建账号createMember输入 apiUrl:{}, platform:{}", apiUrl.toString(), params);
            obApiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMember.getUserId(), "obCreateMember");
            logger.info("OB体育log  创建账号createMember返回 obApiResponseData:{}", obApiResponseData);
        } catch (Exception e) {
            logger.error("OBlog aeCeateMember:{}", e);
            e.printStackTrace();
        }
        return obApiResponseData;
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip,String countryCode) {
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(platform);
            if (null == platformGameParent) {
                return Result.failed();
            }
            long currentTime = System.currentTimeMillis();
            StringBuilder builder = new StringBuilder();
            Map<String, String> params = new HashMap<String, String>();
            params.put("userName", loginUser.getAccount());
            params.put("merchantCode", OpenAPIProperties.OB_MERCHANT_CODE);
            params.put("timestamp", currentTime + "");
            builder.append(OpenAPIProperties.OB_MERCHANT_CODE).append("&").append(loginUser.getAccount()).append("&").append(currentTime);
            String signKey = MD5.md5(builder.toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(signKey).append("&").append(OpenAPIProperties.OB_MERCHANT_KEY);
            String sign = MD5.md5(stringBuilder.toString());
            params.put("signature", sign);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.OB_API_URL).append("/api/user/kickOutUser");
            logger.info("OB体育log  登出玩家logout输入 apiUrl:{}, platform:{}", apiUrl.toString(), params);
            ObApiResponseData obApiResponseData = commonRequest(apiUrl.toString(), params, loginUser.getId().intValue(), "gameLogout");
            logger.info("OB体育log  登出玩家logout返回 obApiResponseData:{}", obApiResponseData);
            if (null == obApiResponseData) {
                return Result.failed();
            }
            if ("0000".equals(obApiResponseData.getCode())) {
                return Result.success(obApiResponseData);
            } else {
                return errorCode(obApiResponseData.getCode(), obApiResponseData.getMsg(), countryCode);
            }
        } catch (Exception e) {
            logger.error("OBlog OBlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public ObApiResponseData commonRequest(String apiUrl, Map<String, String> jsonStr, Integer userId, String type) throws Exception {
        logger.info("OBlog {} commonRequest userId:{},paramsMap:{}", userId, jsonStr);
        ObApiResponseData obApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, apiUrl, jsonStr, type, userId);
        logger.info("OBlog apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            obApiResponseData = JSONObject.parseObject(resultString, ObApiResponseData.class);
            logger.info("OBlog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, jsonStr, resultString, JSONObject.toJSONString(obApiResponseData));
        }
        return obApiResponseData;
    }

    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
        if ("2200".equals(errorCode)) {
            return Result.failed("g091088", MessageUtils.get("g091088",countryCode));
        } else if ("2201".equals(errorCode)) {
            return Result.failed("g091089", MessageUtils.get("g091089",countryCode));
        } else if ("2202".equals(errorCode)) {
            return Result.failed("g091090", MessageUtils.get("g091090",countryCode));
        } else if ("2203".equals(errorCode)) {
            return Result.failed("g091091", MessageUtils.get("g091091",countryCode));
        } else if ("2204".equals(errorCode)) {
            return Result.failed("g091092", MessageUtils.get("g091092",countryCode));
        } else if ("2205".equals(errorCode)) {
            return Result.failed("g091093", MessageUtils.get("g091093",countryCode));
        } else if ("2206".equals(errorCode)) {
            return Result.failed("g091094", MessageUtils.get("g091094",countryCode));
        } else if ("2207".equals(errorCode)) {
            return Result.failed("g091095", MessageUtils.get("g091095",countryCode));
        } else if ("2208".equals(errorCode)) {
            return Result.failed("g091096", MessageUtils.get("g091096",countryCode));
        } else if ("2209".equals(errorCode)) {
            return Result.failed("g091097", MessageUtils.get("g091097",countryCode));
        } else if ("2210".equals(errorCode)) {
            return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
        } else if ("2211".equals(errorCode)) {
            return Result.failed("g300007", MessageUtils.get("g300007",countryCode));
        } else if ("2212".equals(errorCode)) {
            return Result.failed("g091098", MessageUtils.get("g091098",countryCode));
        } else if ("2213".equals(errorCode)) {
            return Result.failed("g091099", MessageUtils.get("g091099",countryCode));
        } else if ("2214".equals(errorCode)) {
            return Result.failed("g091100", MessageUtils.get("g091100",countryCode));
        } else if ("2215".equals(errorCode)) {
            return Result.failed("g091101", MessageUtils.get("g091101",countryCode));
        } else if ("2216".equals(errorCode)) {
            return Result.failed("g091102", MessageUtils.get("g091102",countryCode));
        } else if ("2217".equals(errorCode)) {
            return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
        } else if ("2218".equals(errorCode)) {
            return Result.failed("g091103", MessageUtils.get("g091103",countryCode));
        } else if ("2219".equals(errorCode)) {
            return Result.failed("g091104", MessageUtils.get("g091104",countryCode));
        } else if ("2220".equals(errorCode)) {
            return Result.failed("g091105", MessageUtils.get("g091105",countryCode));
        } else if ("2221".equals(errorCode)) {
            return Result.failed("g091106", MessageUtils.get("g091106",countryCode));
        } else if ("2222".equals(errorCode)) {
            return Result.failed("g091107", MessageUtils.get("g091107",countryCode));
        } else if ("2223".equals(errorCode)) {
            return Result.failed("g091108", MessageUtils.get("g091108",countryCode));
        } else if ("2224".equals(errorCode)) {
            return Result.failed("g091109", MessageUtils.get("g091109",countryCode));
        } else if ("2225".equals(errorCode)) {
            return Result.failed("g091110", MessageUtils.get("g091110",countryCode));
        } else if ("2226".equals(errorCode)) {
            return Result.failed("g091111", MessageUtils.get("g091111",countryCode));
        } else if ("2227".equals(errorCode)) {
            return Result.failed("g091112", MessageUtils.get("g091112",countryCode));
        } else if ("2228".equals(errorCode)) {
            return Result.failed("g091113", MessageUtils.get("g091113",countryCode));
        } else if ("2300".equals(errorCode)) {
            return Result.failed("g091114", MessageUtils.get("g091114",countryCode));
        } else if ("2301".equals(errorCode)) {
            return Result.failed("g091115", MessageUtils.get("g091115",countryCode));
        } else if ("2302".equals(errorCode)) {
            return Result.failed("g091116", MessageUtils.get("g091116",countryCode));
        } else if ("2303".equals(errorCode)) {
            return Result.failed("g091117", MessageUtils.get("g091117",countryCode));
        } else if ("2304".equals(errorCode)) {
            return Result.failed("g091118", MessageUtils.get("g091118",countryCode));
        } else if ("2305".equals(errorCode)) {
            return Result.failed("g091119", MessageUtils.get("g091119",countryCode));
        } else if ("2306".equals(errorCode)) {
            return Result.failed("g091120", MessageUtils.get("g091120",countryCode));
        } else if ("2307".equals(errorCode)) {
            return Result.failed("g091121", MessageUtils.get("g091121",countryCode));
        } else if ("2308".equals(errorCode)) {
            return Result.failed("g091122", MessageUtils.get("g091122",countryCode));
        } else {
            return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }

}