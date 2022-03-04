package com.indo.game.service.ae.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.Base64;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.pojo.dto.ae.AeApiResponseData;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.ae.AeService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;

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
 * AE电子
 *
 * @author
 */
@Service
public class AeServiceImpl implements AeService {

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
    public Result aeGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("aelog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == platformGameParent) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if ("0".equals(platformGameParent.getIsStart())) {
            return Result.failed("g" + "100101", "游戏平台未启用");
        }
        if ("1".equals(platformGameParent.getIsOpenMaintenance())) {
            return Result.failed("g000001", platformGameParent.getMaintenanceContent());
        }
        GamePlatform gamePlatform = new GamePlatform();
        if (!platform.equals(parentName)) {
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if ("0".equals(gamePlatform.getIsStart())) {
                return Result.failed("g" + "100102", "游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
            }
        }
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点AE余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }

        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                //第三方账号指定格式 运营商代码(英文大写) + 下划线 + 玩家账号（大写英数）
                String name = OpenAPIProperties.AE_MERCHANT_ID + "_" + loginUser.getAccount();
                cptOpenMember.setUserName(name);
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return createMemberGame(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }
            Result result = this.logout(loginUser, parentName, ip);
            if (null != result && "00000".equals(result.getCode())) {
                //登录
                return initGame(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    /**
     * 登录逻辑
     */
    private Result initGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform,
                            CptOpenMember cptOpenMember, String isMobileLogin) {
        AeApiResponseData aeApiResponseData = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
        if (null == aeApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (("0").equals(aeApiResponseData.getCode())) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonObject = JSON.parseObject(aeApiResponseData.getData().get(0).toString());
            responseData.setPathUrl(jsonObject.getString("gameUrl"));
            return Result.success(aeApiResponseData);
        } else {
            return Result.failed("g091088", "第三方响应异常！");
        }
    }

    /**
     * 调用API登录
     */
    private AeApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMemberm, String isMobileLogin) {
        long currentTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", OpenAPIProperties.AE_MERCHANT_ID);
        params.put("currency", platformGameParent.getCurrencyType());
        params.put("currentTime", currentTime);
        params.put("username", cptOpenMemberm.getUserName());
        params.put("playmode", "0"); //游玩模式。0: 正式
        //设备。0: 行动装置 1: 网页
        if ("1".equals(isMobileLogin)) {
            params.put("device", "0");
        } else {
//            false 桌面设备登入
            params.put("device", "1");
        }
        // 加密
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.AE_MERCHANT_ID).append(platformGameParent.getCurrencyType()).append(currentTime);
        builder.append(cptOpenMemberm.getUserName()).append("0").append(params.get("device")).append(gamePlatform.getPlatformCode());
        builder.append(platformGameParent.getLanguageType()).append(Base64.encode(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
        String sign = MD5.md5(builder.toString());
        params.put("gameId", gamePlatform.getPlatformCode());
        params.put("sign", sign);
        params.put("language", platformGameParent.getLanguageType());
        String jsonStr = JSON.toJSONString(params);
        AeApiResponseData aeApiResponseData = null;
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.AE_API_URL).append("/api/login");
            aeApiResponseData = commonRequest(apiUrl.toString(), jsonStr, cptOpenMemberm.getUserId(), "gameLogin");
        } catch (Exception e) {
            logger.error("aelog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return aeApiResponseData;
    }

    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {
        AeApiResponseData aeApiResponseData = createMember(platformGameParent, gamePlatform, ip, cptOpenMember, isMobileLogin);
        if (null == aeApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (("0").equals(aeApiResponseData.getCode())) {
            externalService.saveCptOpenMember(cptOpenMember);
            return initGame(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
        } else {
            return Result.failed("g091088", "第三方响应异常！");
        }
    }

    /**
     * 调用API创建账号
     */
    private AeApiResponseData createMember(GameParentPlatform platformGameParent, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember, String isMobileLogin) {

        long currentTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        builder.append(OpenAPIProperties.AE_MERCHANT_ID).append(platformGameParent.getCurrencyType()).append(currentTime);
        builder.append(cptOpenMember.getUserName()).append(Base64.encode(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
        String sign = MD5.md5(builder.toString());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", OpenAPIProperties.AE_MERCHANT_ID);
        params.put("currentTime", currentTime);
        params.put("currency", platformGameParent.getCurrencyType());
        params.put("username", cptOpenMember.getUserName());
        params.put("sign", sign);
        params.put("language", platformGameParent.getLanguageType());
        params.put("brandCode", platformGameParent.getPlatformCode());
        String jsonStr = JSON.toJSONString(params);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.AE_API_URL).append("/api/register");
        AeApiResponseData aeApiResponseData = null;
        try {
            aeApiResponseData = commonRequest(apiUrl.toString(), jsonStr, cptOpenMember.getUserId(), "createMember");
        } catch (Exception e) {
            logger.error("aelog aeCeateMember:{}", e);
            e.printStackTrace();
        }
        return aeApiResponseData;
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(platform);
            if (null == platformGameParent) {
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
            long currentTime = System.currentTimeMillis();
            StringBuilder builder = new StringBuilder();
            String name = OpenAPIProperties.AE_MERCHANT_ID + "_" + loginUser.getAccount();
            builder.append(OpenAPIProperties.AE_MERCHANT_ID).append(platformGameParent.getCurrencyType()).append(currentTime);
            builder.append(name).append(Base64.encode(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
            String sign = MD5.md5(builder.toString());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchantId", OpenAPIProperties.AE_MERCHANT_ID);
            params.put("currency", platformGameParent.getCurrencyType());  //币种必填项
            params.put("username", name);
            params.put("currentTime", currentTime);
            params.put("sign", sign);
            String jsonStr = JSON.toJSONString(params);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.AE_API_URL).append("/api/logout");
            AeApiResponseData aeApiResponseData = commonRequest(apiUrl.toString(), jsonStr, loginUser.getId().intValue(), "gameLogin");
            if (null == aeApiResponseData) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if ("0".equals(aeApiResponseData.getCode())) {
                return Result.success(aeApiResponseData);
            } else {
                return Result.failed("g091088", "第三方响应异常！");
            }
        } catch (Exception e) {
            logger.error("aelog aeLogout:{}", e);
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }

    }


    /**
     * 公共请求
     */
    public AeApiResponseData commonRequest(String apiUrl, String jsonStr, Integer userId, String type) throws Exception {
        logger.info("aelog {} commonRequest userId:{},paramsMap:{}", userId, jsonStr);
        AeApiResponseData aeApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, apiUrl, jsonStr, type, userId);
        logger.info("aelog apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            aeApiResponseData = JSONObject.parseObject(resultString, AeApiResponseData.class);
            logger.info("aelog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, jsonStr, resultString, JSONObject.toJSONString(aeApiResponseData));
        }
        return aeApiResponseData;
    }
}
