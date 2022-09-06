package com.indo.game.service.yl.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.yl.YlService;

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
 * PG
 *
 * @author
 */
@Service
public class YlServiceImpl implements YlService {

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
    public Result ylGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("yllog  {} YLGame account:{}, ylCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点YL余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {

            // 验证且绑定（YL-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                logout(loginUser, platform, ip);
            }
            return initGame(gamePlatform,gameParentPlatform, cptOpenMember, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private Result gameLogin(String apiKey, String platform, GameParentPlatform platformGameParent, CptOpenMember cptOpenMember,String countryCode) {
        String pathUrl = "";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("user", cptOpenMember.getUserName());
            map.put("key", apiKey);
            map.put("extension1", OpenAPIProperties.YL_EXTENSION);
            map.put("userName", cptOpenMember.getUserName());
            //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        countryCode = "EN";
                    case "EN":
                        countryCode = "EN";
                    case "CN":
                        countryCode = "CN";
                    case "VN":
                        countryCode = "VN";
                    case "TW":
                        countryCode = "HK";
                    case "TH":
                        countryCode = "TH";
                    case "ID":
                        countryCode = "ID";
                    case "SP":
                        countryCode = "SP";
                    case "KR":
                        countryCode = "KO";
                    case "JP":
                        countryCode = "JP";
                    case "MM":
                        countryCode = "MM";
                    default:
                        countryCode = platformGameParent.getLanguageType();
                }
            }else{
                countryCode = platformGameParent.getLanguageType();
            }
            map.put("language", countryCode);
            if (!OpenAPIProperties.YL_IS_PLATFORM_LOGIN.equals("Y")) {
                map.put("gameId", platform);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/login");
            logger.error("YL捕鱼登录请求：url{},params{},userAcct{}", builder,map,cptOpenMember.getUserName());
            JSONObject jsonObject = commonRequest(builder.toString(), map, cptOpenMember.getUserId().intValue(), "yLGameLogin");
            logger.error("YL捕鱼登录请求返回：JSONObject{}", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("status"))) {
                pathUrl = jsonObject.getString("url");
                //登录
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(pathUrl);
                return Result.success(responseData);
            }else if(null == jsonObject){
                return Result.failed();
            }else {
                return errorCode(jsonObject.getString("status"),jsonObject.getString("desc"));
            }
        } catch (Exception e) {
            logger.error("YL捕鱼登录获取异常：", e);
            e.printStackTrace();
            return Result.failed();
        }
    }

    private Result initGame(GamePlatform gamePlatform,GameParentPlatform platformGameParent, CptOpenMember cptOpenMember,String countryCode) {
        String apiKey = "";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("cert", OpenAPIProperties.YL_CERT);
            map.put("user", cptOpenMember.getUserName());
            map.put("userName", cptOpenMember.getUserName());
            map.put("extension1", OpenAPIProperties.YL_EXTENSION);
            map.put("currency", platformGameParent.getCurrencyType());
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/getKey");
            logger.error("YL捕鱼获取KEY请求：url{},params{},userAcct{}", builder,map,cptOpenMember.getUserName());
            JSONObject jsonObject = commonRequest(builder.toString(), map, cptOpenMember.getUserId().intValue(), "yLInitGame");
            logger.info("YL捕鱼获取KEY请求返回：", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("status"))) {
                apiKey = jsonObject.getString("key");
                return gameLogin(apiKey, gamePlatform.getPlatformCode(), platformGameParent, cptOpenMember, countryCode);
            }else if(null == jsonObject){
                return Result.failed();
            }else {
                return errorCode(jsonObject.getString("status"),jsonObject.getString("desc"));
            }
        } catch (Exception e) {
            logger.error("YL捕鱼获取KEY异常：", e);
            e.printStackTrace();
            return Result.failed();
        }
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("cert", OpenAPIProperties.YL_CERT);
            map.put("user", loginUser.getAccount());
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.YL_API_URL).append("/api/").append(OpenAPIProperties.YL_WEB_SITE).append("/logout");
            logger.error("YL捕鱼强迫登出玩家请求：url{},params{},userAcct{}", builder,map,loginUser.getAccount());
            JSONObject jsonObject = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "yLLogOut");
            logger.info("YL捕鱼强迫登出玩家请求返回：", jsonObject);
            if (null != jsonObject && "1".equals(jsonObject.getString("desc"))) {
                return Result.success();
            }else if(null == jsonObject){
                return Result.failed();
            }else {
                return errorCode(jsonObject.getString("status"),jsonObject.getString("desc"));
            }

        } catch (Exception e) {
            logger.error("yllog 强迫登出玩家异常:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        JSONObject psApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        if (StringUtils.isNotEmpty(resultString)) {
            psApiResponseData = JSONObject.parseObject(resultString);
        }
        return psApiResponseData;
    }

    public Result  errorCode(String errorCode,String errorMessage){
//        0000 成功。                                                Succeed.
        switch (errorCode){
            case "0":
                return Result.failed("g009999",errorMessage);
            case "500":
                return Result.failed("g009999",errorMessage);
            case "1001":
                return Result.failed("g000002",errorMessage);
            case "1002":
                return Result.failed("g300007",errorMessage);
            case "1003":
                return Result.failed("g091068",errorMessage);
            case "1004":
                return Result.failed("g091084",errorMessage);
            case "1006":
                return Result.failed("g091075",errorMessage);
            case "1007":
                return Result.failed("g091155",errorMessage);
            case "1008":
                return Result.failed("g091156",errorMessage);
            case "1009":
                return Result.failed("g300004",errorMessage);
            case "1011":
                return Result.failed("g091033",errorMessage);
            case "1012":
                return Result.failed("g091158",errorMessage);
            case "1014":
                return Result.failed("g091157",errorMessage);
            case "1015":
                return Result.failed("g091159",errorMessage);
            case "1016":
                return Result.failed("g091035",errorMessage);
            case "1018":
                return Result.failed("g200003",errorMessage);
            case "1023":
                return Result.failed("g091159",errorMessage);
            case "1024":
                return Result.failed("g100001",errorMessage);
            case "1030":
                return Result.failed("g091035",errorMessage);
            //        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999",errorMessage);
        }
    }
}
