package com.indo.game.service.dg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.RandomUtil;
import com.indo.game.common.util.RandomGUID;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.pg.PgApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.dg.DgService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.json.JSONArray;


/**
 * DG
 *
 * @author
 */
@Service
public class DgServiceImpl implements DgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;


    /**
     * 登录游戏CQ9游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result dgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("Dglog  {} dgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
        if (!platform.equals(parentName)) {
            GamePlatform gamePlatform;
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if (0==gamePlatform.getIsStart()) {
                return Result.failed("g100102", "游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
            }
        }
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点PG余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                String sign = DigestUtils.md5Hex(loginUser.getAccount());
                cptOpenMember.setPassword(sign);
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                Result result = createMemberGame(gameParentPlatform, cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                logout(loginUser, platform, ip);
            }

            JSONObject apiResponseData = gameLogin(gameParentPlatform, cptOpenMember);
            if (null == apiResponseData || !"0".equals(apiResponseData.getString("codeId"))) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            StringBuilder builder = new StringBuilder();
            builder.append(apiResponseData.getJSONArray("list").get(0)).append(apiResponseData.getString("token"));
            builder.append("&language=").append(gameParentPlatform.getLanguageType());
            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(builder.toString());
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private JSONObject gameLogin(GameParentPlatform platformGameParent, CptOpenMember cptOpenMember) {
        JSONObject map = new JSONObject();
        Integer random = RandomUtil.getRandomOne(6);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OpenAPIProperties.DG_AGENT_NAME).append(OpenAPIProperties.DG_API_KEY).append(random);
        String sign = DigestUtils.md5Hex(stringBuilder.toString());
        map.put("random", random);
        map.put("token", sign);
        map.put("lang", platformGameParent.getLanguageType());
        map.put("domains", "1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", cptOpenMember.getUserName());
        map.put("member", jsonObject);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.DG_API_URL).append("/user/login/").append(OpenAPIProperties.DG_AGENT_NAME);
        JSONObject dgApiResponseData = null;
        try {
            dgApiResponseData = commonRequest(apiUrl.toString(), map, cptOpenMember.getUserId(), "createPgMember");
        } catch (Exception e) {
            logger.error("Dglog dgGameLoginr:{}", e);
            e.printStackTrace();
        }
        return dgApiResponseData;
    }

    private JSONObject createDgMemberGame(CptOpenMember cptOpenMember, GameParentPlatform platformGameParent) {
        JSONObject map = new JSONObject();
        Integer random = RandomUtil.getRandomOne(6);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OpenAPIProperties.DG_AGENT_NAME).append(OpenAPIProperties.DG_API_KEY).append(random);
        String sign = DigestUtils.md5Hex(stringBuilder.toString());
        map.put("token", sign);
        map.put("random", random);
        map.put("data", "J");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", cptOpenMember.getUserName());
        jsonObject.put("password", cptOpenMember.getPassword());
        jsonObject.put("currencyName", platformGameParent.getCurrencyType());
        jsonObject.put("winLimit", "0");
        map.put("member", jsonObject);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.DG_API_URL).append("/user/signup/").append(OpenAPIProperties.DG_AGENT_NAME);
        JSONObject dgApiResponseData = null;
        try {
            dgApiResponseData = commonRequest(apiUrl.toString(), map, cptOpenMember.getUserId(), "createPgMember");
        } catch (Exception e) {
            logger.error("Dglog createDgMemberGame:{}", e);
            e.printStackTrace();
        }
        return dgApiResponseData;

    }


    /**
     * 创建账户并登录逻辑
     */
    private Result createMemberGame(GameParentPlatform platformGameParent, CptOpenMember cptOpenMember) {
        JSONObject apiResponseData = createDgMemberGame(cptOpenMember, platformGameParent);
        if (null == apiResponseData || !"0".equals(apiResponseData.getString("codeId"))) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (("0").equals(apiResponseData.getString("codeId"))) {
            externalService.saveCptOpenMember(cptOpenMember);
        }
        return Result.success();
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            JSONObject map = new JSONObject();
            Integer random = RandomUtil.getRandomOne(6);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(OpenAPIProperties.DG_AGENT_NAME).append(OpenAPIProperties.DG_API_KEY).append(random);
            String sign = DigestUtils.md5Hex(stringBuilder.toString());
            map.put("token", sign);
            map.put("random", random);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(loginUser.getAccount());
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.DG_API_URL).append("/user/offline/").append(OpenAPIProperties.DG_AGENT_NAME);
            JSONObject apiResponseData = commonRequest(apiUrl.toString(), map, loginUser.getId().intValue(), "dgLogout");
            if (null == apiResponseData || !"0".equals(apiResponseData.getString("codeId"))) {
                return Result.failed();
            }
            if (("0").equals(apiResponseData.getString("codeId"))) {
                return Result.success();
            }
        } catch (Exception e) {
            logger.error("Dglog  DgLogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
        return Result.failed();
    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, JSONObject params, Integer userId, String type) throws Exception {
        logger.info("Dglog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        JSONObject apiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params.toJSONString(), type, userId);
        logger.info("Dglog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString);
            logger.info("Dglog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(apiResponseData));
        }
        return apiResponseData;
    }


    public Result errorCode(String errorCode, String errorMessage) {
        if ("1034".equals(errorCode)) {
            return Result.failed("g091087", errorMessage);
        } else if ("1035".equals(errorCode)) {
            return Result.failed("g009999", errorMessage);
        } else if ("1200".equals(errorCode)) {
            return Result.failed("g091124", errorMessage);
        } else if ("1204".equals(errorCode)) {
            return Result.failed("g091008", errorMessage);
        } else if ("1305".equals(errorCode)) {
            return Result.failed("g100003", errorMessage);
        } else {
            return Result.failed("g009999", errorMessage);
        }
    }
}
