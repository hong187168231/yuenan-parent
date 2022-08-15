package com.indo.game.service.cq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.cq.CqApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.cq.CqService;

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
 * CQ
 *
 * @author
 */
@Service
public class CqServiceImpl implements CqService {

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
    public Result cqGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("cqlog {} cqGame account:{}, cqCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
            logger.info("站点CQ9游戏余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }
        try {

            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            boolean b = true;
            if (cptOpenMember == null) {
                b = false;
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setUserId(loginUser.getId().intValue());
                //储存session
                cptOpenMember.setPassword(SnowflakeId.generateId().toString());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                externalService.saveCptOpenMember(cptOpenMember);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
            }
            if(b){
                this.logout(loginUser,parentName,ip);
            }
            //登录
            return initGame(gameParentPlatform, gamePlatform, cptOpenMember, isMobileLogin);
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
        CqApiResponseData cqApiResponseData = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin);
        if (null == cqApiResponseData) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        JSONObject jsonStatusObject = JSON.parseObject(cqApiResponseData.getStatus());
        if (null == jsonStatusObject) {
            return Result.failed("g091087", "第三方请求异常！");
        }
        if (("0").equals(jsonStatusObject.getString("code"))) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonDataObject = JSON.parseObject(cqApiResponseData.getData());
            responseData.setPathUrl(jsonDataObject.getString("url"));
            return Result.success(responseData);
        } else {
            return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"));
        }
    }

    /**
     * 调用API登录
     */
    private CqApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMemberm, String isMobileLogin) {
        Map<String, String> params = new HashMap<String, String>();
//        params.put("account", cptOpenMemberm.getUserName());
//        params.put("lang", platformGameParent.getLanguageType());
//        params.put("session", "");
        params.put("account", cptOpenMemberm.getUserName());//	必填	玩家帳號 ※字串長度限制36個字元
        params.put("gamehall", gamePlatform.getParentName());//gamehall	string	必填	遊戲廠商
        params.put("gamecode", gamePlatform.getPlatformCode());//gamecode	string	必填	遊戲代碼
        //gameplat	string	必填	遊戲平台，請填入 web 或 mobile ※若 gameplat 不為該遊戲平台時，將帶入預設值，預設值為 web
        if("0".equals(isMobileLogin)){
            params.put("gameplat", "web");
        }else {
            params.put("gameplat", "mobile");
        }
        params.put("lang", platformGameParent.getLanguageType());//lang	string	必填	語言代碼，全數支援 zh-cn ,en ，部份遊戲支援 th, zh-tw，確切支援程度請調用遊戲列表 API 回傳資訊
        params.put("session", "");//session	string	選填	SessionID，貴司提供後，我司會帶入此參數去呼叫貴司錢包 Bet, Rollout 或 TakeAll API
        params.put("app", "Y");//app	string	選填	是否是透過app 執行遊戲，Y=是，N=否，預設為N
        params.put("detect", "");//detect	string	選填	是否開啟阻擋不合遊戲規格瀏覽器提示， Y=是，N=否，預設為N
        CqApiResponseData cqApiResponseData = null;
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.CQ_API_URL).append("/gameboy/player/sw/gamelink");
            cqApiResponseData = commonRequest(apiUrl.toString(), params, cptOpenMemberm.getUserId(), "cqGameLogin");
        } catch (Exception e) {
            logger.error("cqlog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return cqApiResponseData;
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
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", loginUser.getAccount());
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.CQ_API_URL).append("/gameboy/player/logout");
            CqApiResponseData cqApiResponseData = commonRequest(apiUrl.toString(), params, loginUser.getId().intValue(), "cqGameLogin");
            if (null == cqApiResponseData) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            JSONObject jsonObject = JSON.parseObject(cqApiResponseData.getStatus());
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if ("0".equals(jsonObject.getString("code"))) {
                return Result.success(cqApiResponseData);
            } else {
                return errorCode(jsonObject.getString("code"), jsonObject.getString("message"));
            }
        } catch (Exception e) {
            logger.error("cqlog cqlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }

    }


    /**
     * 公共请求
     */
    public CqApiResponseData commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        logger.info("cqlog {} commonRequest userId:{},paramsMap:{}", userId, params);
        CqApiResponseData cqApiResponseData = null;
        String resultString = GameUtil.doProxyPostHeaderJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
                apiUrl, params, type, userId, OpenAPIProperties.CQ_API_TOKEN);
        logger.info("cqlog apiResponse:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            cqApiResponseData = JSONObject.parseObject(resultString, CqApiResponseData.class);
            logger.info("cqlog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, resultString, JSONObject.toJSONString(cqApiResponseData));
        }
        return cqApiResponseData;
    }


    public Result errorCode(String errorCode, String errorMessage) {
        if ("0".equals(errorCode)) {
            return Result.failed("g091123", errorMessage);
        } else if ("1".equals(errorCode)) {
            return Result.failed("g300004", errorMessage);
        } else if ("2".equals(errorCode)) {
            return Result.failed("g091124", errorMessage);
        } else if ("3".equals(errorCode)) {
            return Result.failed("g091008", errorMessage);
        } else if ("4".equals(errorCode)) {
            return Result.failed("g091125", errorMessage);
        } else if ("5".equals(errorCode)) {
            return Result.failed("g000007", errorMessage);
        } else if ("6".equals(errorCode)) {
            return Result.failed("g100003", errorMessage);
        } else if ("8".equals(errorCode)) {
            return Result.failed("g091068", errorMessage);
        } else if ("9".equals(errorCode)) {
            return Result.failed("g091126", errorMessage);
        } else if ("10".equals(errorCode)) {
            return Result.failed("g091024", errorMessage);
        } else if ("11".equals(errorCode)) {
            return Result.failed("g091034", errorMessage);
        } else if ("12".equals(errorCode)) {
            return Result.failed("g091127", errorMessage);
        } else if ("13".equals(errorCode)) {
            return Result.failed("g091033", errorMessage);
        } else if ("14".equals(errorCode)) {
            return Result.failed("g091097", errorMessage);
        } else if ("15".equals(errorCode)) {
            return Result.failed("g091128", errorMessage);
        } else if ("16".equals(errorCode)) {
            return Result.failed("g091129", errorMessage);
        } else if ("23".equals(errorCode)) {
            return Result.failed("g000001", errorMessage);
        } else if ("24".equals(errorCode)) {
            return Result.failed("g091130", errorMessage);
        } else if ("28".equals(errorCode)) {
            return Result.failed("g100005", errorMessage);
        } else if ("29".equals(errorCode)) {
            return Result.failed("g091131", errorMessage);
        } else if ("31".equals(errorCode)) {
            return Result.failed("g100005", errorMessage);
        } else if ("33".equals(errorCode)) {
            return Result.failed("g091132", errorMessage);
        } else if ("35".equals(errorCode)) {
            return Result.failed("g091133", errorMessage);
        } else if ("100".equals(errorCode)) {
            return Result.failed("g091134", errorMessage);
        } else if ("101".equals(errorCode)) {
            return Result.failed("g091090", errorMessage);
        } else if ("102".equals(errorCode)) {
            return Result.failed("g091135", errorMessage);
        } else if ("103".equals(errorCode)) {
            return Result.failed("g091136", errorMessage);
        } else if ("104".equals(errorCode)) {
            return Result.failed("g091137", errorMessage);
        } else if ("105".equals(errorCode)) {
            return Result.failed("g091138", errorMessage);
        } else if ("106".equals(errorCode)) {
            return Result.failed("g091139", errorMessage);
        } else if ("107".equals(errorCode)) {
            return Result.failed("g091140", errorMessage);
        } else if ("108".equals(errorCode)) {
            return Result.failed("g091141", errorMessage);
        } else if ("200".equals(errorCode)) {
            return Result.failed("g091142", errorMessage);
        } else if ("201".equals(errorCode)) {
            return Result.failed("g091143", errorMessage);
        } else if ("202".equals(errorCode)) {
            return Result.failed("g091144", errorMessage);
        } else {
            return Result.failed("g009999", errorMessage);
        }
    }
}
