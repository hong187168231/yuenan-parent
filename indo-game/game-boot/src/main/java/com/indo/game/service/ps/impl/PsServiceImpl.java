package com.indo.game.service.ps.impl;

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
import com.indo.game.service.ps.PsService;

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
public class PsServiceImpl implements PsService {

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
    public Result psGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("pslog  {} jkGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                this.logout(loginUser, platform, ip);
            }
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        countryCode = "en_US";
                    case "EN":
                        countryCode = "en_US";
                    case "CN":
                        countryCode = "zh_CN";
                    case "VN":
                        countryCode = "vi_VN";
                    case "TW":
                        countryCode = "zh_TW";
                    case "TH":
                        countryCode = "th_TH";
                    case "ID":
                        countryCode = "in_ID";
                    case "MY":
                        countryCode = "ms_MY";
                    case "KR":
                        countryCode = "ko_KR";
                    case "JP":
                        countryCode = "ja_JP";
                    default:
                        countryCode = gameParentPlatform.getLanguageType();
                }
            }else{
                countryCode = gameParentPlatform.getLanguageType();
            }
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.PS_API_URL).append("/launch/?host_id=");
            builder.append(OpenAPIProperties.PS_HOST_ID);
            builder.append("&game_id=").append(platform);
            builder.append("&lang=").append(countryCode);
            builder.append("&access_token=").append(cptOpenMember.getPassword());

            //登录
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(builder.toString());
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }


    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(platform);
            if (null == platformGameParent) {
                return Result.failed();
            }
            Map<String, String> map = new HashMap<>();
            map.put("host_id", OpenAPIProperties.PS_HOST_ID);
            map.put("member_id", loginUser.getId() + "");
            StringBuilder builder = new StringBuilder();
            builder.append(OpenAPIProperties.PS_API_URL).append("/admin/kickout");
            JSONObject jsonObject = commonRequest(builder.toString(), map, loginUser.getId().intValue(), "cqGamelogout");
            if (null == jsonObject) {
                return Result.failed();
            }
            if (("0").equals(jsonObject.getString("status_code"))) {
                return Result.success();
            } else {
                return Result.failed();
            }
        } catch (Exception e) {
            logger.error("pslog  pslog out:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public JSONObject commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("pslog  {} commonRequest userId:{},paramsMap:{},type:{}", userId, params,type);
        JSONObject psApiResponseData = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId);
        logger.info("pslog  apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            psApiResponseData = JSONObject.parseObject(resultString);
            logger.info("pslog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(psApiResponseData));
        }
        return psApiResponseData;
    }

}
