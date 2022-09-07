package com.indo.game.service.bl.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.RandomUtil;
import com.indo.common.utils.SignMd5Utils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.vo.callback.bl.BlResponseParentData;
import com.indo.game.service.bl.BlService;
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
 * BOLE
 *
 * @author
 */
@Service
public class BlServiceImpl implements BlService {

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
    public Result blGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("BLlog  {} dgGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
//            logger.info("站点PG余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                Result result = logout(loginUser, platform, ip, countryCode);
            }
            BlResponseParentData apiResponseData = gameLogin(gameParentPlatform, gamePlatform,cptOpenMember,ip, countryCode);
            if (null != apiResponseData && "200".equals(apiResponseData.getResp_msg().getCode())) {
                //登录
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(apiResponseData.getResp_data().getUrl());
                return Result.success(responseData);
            }else {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    private BlResponseParentData gameLogin(GameParentPlatform platformGameParent,GamePlatform gamePlatform, CptOpenMember cptOpenMember,String ip,String countryCode) {
        Map<String, String> map = new HashMap<String, String>();
        Integer random = RandomUtil.getRandomOne(7);
        Long dataTime = System.currentTimeMillis() / 1000;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OpenAPIProperties.BL_KEY_SECRET).append(random).append(dataTime);
        String sign = SignMd5Utils.getSha1(stringBuilder.toString()).toLowerCase();
        if(OpenAPIProperties.BTI_IS_PLATFORM_LOGIN.equals("Y")){
            map.put("game_code", "");
        }else {
            map.put("game_code", gamePlatform.getPlatformCode());
        }
        map.put("player_account", cptOpenMember.getUserName());
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "en_US";
                case "EN":
                    lang = "en_US";
                case "CN":
                    lang = "zh_CN";
                case "VN":
                    lang = "vi_VN";
                case "KR":
                    lang = "ko_KR";
                default:
                    lang = platformGameParent.getLanguageType();
            }
        }else{
            lang = platformGameParent.getLanguageType();
        }
        map.put("lang", lang);
        map.put("ip", ip);

        map.put("country", platformGameParent.getCurrencyType());
        map.put("AccessKeyId", OpenAPIProperties.BL_KEY_ID);
        map.put("Timestamp", dataTime + "");
        map.put("Nonce", random + "");
        map.put("Sign", sign);
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(OpenAPIProperties.BL_API_URL).append("/v1/player/login");
        BlResponseParentData dgApiResponseData = null;
        try {
            logger.info("BL启动游戏 gameLogin apiUrl:{},map:{}",apiUrl.toString(), map);
            dgApiResponseData = commonRequest(apiUrl.toString(), map, cptOpenMember.getUserId(), "createBLlogin");
            logger.info("BL启动游戏 gameLogin apiResponseData:{}",dgApiResponseData);
        } catch (Exception e) {
            logger.error("BLlog createBLlogin:{}", e);
            e.printStackTrace();
        }
        return dgApiResponseData;
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip,String countryCode) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            Long dataTime =  System.currentTimeMillis() / 1000;
            Integer random = RandomUtil.getRandomOne(7);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(OpenAPIProperties.BL_KEY_SECRET).append(random).append(dataTime);
            String sign = SignMd5Utils.getSha1(stringBuilder.toString()).toLowerCase();
            map.put("player_account", loginUser.getAccount());
            map.put("AccessKeyId", OpenAPIProperties.BL_KEY_ID);
            map.put("Timestamp", dataTime + "");
            map.put("Nonce", random + "");
            map.put("Sign", sign);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.BL_API_URL).append("/v1/player/logout");
            logger.info("BL强迫登出玩家 logout apiUrl:{},map:{}",apiUrl.toString(), map);
            BlResponseParentData apiResponseData = commonRequest(apiUrl.toString(), map, loginUser.getId().intValue(), "BLlogout");
            logger.info("BL强迫登出玩家 logout apiResponseData:{}",apiResponseData);
            if (null != apiResponseData && "0".equals(apiResponseData.getResp_msg().getCode())) {
                return Result.success();
            }else {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BLlog  BLlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
    }


    /**
     * 公共请求
     */
    public BlResponseParentData commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("BLlog commonRequest userId:{},type:{},paramsMap:{},apiUrl:{}", userId, type,params,apiUrl);
        BlResponseParentData apiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("BLlog  apiResponse apiResponseParamsMap:{},apiUrl:{}",resultString,apiUrl);
        if (StringUtils.isNotEmpty(resultString)) {
            apiResponseData = JSONObject.parseObject(resultString,BlResponseParentData.class);
            logger.info("BLlog  commonRequest apiResponse userId:{}, type:{}, apiResponse:{}",
                    userId, type,JSONObject.toJSONString(apiResponseData));
        }
        return apiResponseData;
    }

}
