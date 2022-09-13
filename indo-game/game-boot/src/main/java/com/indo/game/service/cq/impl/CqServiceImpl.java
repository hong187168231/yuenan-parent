package com.indo.game.service.cq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.cq.CqApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
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
import java.util.List;
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
    @Autowired
    private GameLogoutService gameLogoutService;

    /**
     * 登录游戏CQ9游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result cqGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("cqlog {} cqGame account:{}, cqCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
//            logger.info("站点CQ9游戏余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
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
//            if(b){
//                this.logout(loginUser,parentName,ip,countryCode);
//            }
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
        CqApiResponseData cqApiResponseData = gameLogin(platformGameParent, gamePlatform, cptOpenMember, isMobileLogin, countryCode);
        if (null == cqApiResponseData) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        JSONObject jsonStatusObject = JSON.parseObject(cqApiResponseData.getStatus());
        if (null == jsonStatusObject) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
        if (("0").equals(jsonStatusObject.getString("code"))) {
            ApiResponseData responseData = new ApiResponseData();
            JSONObject jsonDataObject = JSON.parseObject(cqApiResponseData.getData());
            responseData.setPathUrl(jsonDataObject.getString("url"));
            return Result.success(responseData);
        } else {
            return errorCode(jsonStatusObject.getString("code"), jsonStatusObject.getString("message"),countryCode);
        }
    }

    /**
     * 调用API登录
     */
    private CqApiResponseData gameLogin(GameParentPlatform platformGameParent, GamePlatform gamePlatform, CptOpenMember cptOpenMemberm, String isMobileLogin,String countryCode) {
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
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "en";
                    break;
                case "EN":
                    lang = "en";
                    break;
                case "CN":
                    lang = "zh-cn";
                    break;
                case "VN":
                    lang = "vn";
                    break;
                case "TH":
                    lang = "th";
                    break;
                default:
                    lang = platformGameParent.getLanguageType();
                    break;
            }
        }else{
            lang = platformGameParent.getLanguageType();
        }
        params.put("lang", lang);//lang	string	必填	語言代碼，全數支援 zh-cn ,en ，部份遊戲支援 th, zh-tw，確切支援程度請調用遊戲列表 API 回傳資訊
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
    public Result logout(String account,String platform, String ip,String countryCode) {
        try {
            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(platform);
            if (null == platformGameParent) {
                return Result.failed();
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", account);
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.CQ_API_URL).append("/gameboy/player/logout");
            CqApiResponseData cqApiResponseData = commonRequest(apiUrl.toString(), params, 0, "cqGameLogin");
            if (null == cqApiResponseData) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            JSONObject jsonObject = JSON.parseObject(cqApiResponseData.getStatus());
            if (null == jsonObject) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if ("0".equals(jsonObject.getString("code"))) {
                return Result.success(cqApiResponseData);
            } else {
                return errorCode(jsonObject.getString("code"), jsonObject.getString("message"),countryCode);
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


    public Result errorCode(String errorCode, String errorMessage,String  countryCode) {
        if ("0".equals(errorCode)) {
            return Result.failed("g091123", MessageUtils.get("g091123",countryCode));
        } else if ("1".equals(errorCode)) {
            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
        } else if ("2".equals(errorCode)) {
            return Result.failed("g091124", MessageUtils.get("g091124",countryCode));
        } else if ("3".equals(errorCode)) {
            return Result.failed("g091008", MessageUtils.get("g091008",countryCode));
        } else if ("4".equals(errorCode)) {
            return Result.failed("g091125", MessageUtils.get("g091125",countryCode));
        } else if ("5".equals(errorCode)) {
            return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
        } else if ("6".equals(errorCode)) {
            return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
        } else if ("8".equals(errorCode)) {
            return Result.failed("g091068", MessageUtils.get("g091068",countryCode));
        } else if ("9".equals(errorCode)) {
            return Result.failed("g091126", MessageUtils.get("g091126",countryCode));
        } else if ("10".equals(errorCode)) {
            return Result.failed("g091024", MessageUtils.get("g091024",countryCode));
        } else if ("11".equals(errorCode)) {
            return Result.failed("g091034", MessageUtils.get("g091034",countryCode));
        } else if ("12".equals(errorCode)) {
            return Result.failed("g091127", MessageUtils.get("g091127",countryCode));
        } else if ("13".equals(errorCode)) {
            return Result.failed("g091033", MessageUtils.get("g091033",countryCode));
        } else if ("14".equals(errorCode)) {
            return Result.failed("g091097", MessageUtils.get("g091097",countryCode));
        } else if ("15".equals(errorCode)) {
            return Result.failed("g091128", MessageUtils.get("g091128",countryCode));
        } else if ("16".equals(errorCode)) {
            return Result.failed("g091129", MessageUtils.get("g091129",countryCode));
        } else if ("23".equals(errorCode)) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        } else if ("24".equals(errorCode)) {
            return Result.failed("g091130", MessageUtils.get("g091130",countryCode));
        } else if ("28".equals(errorCode)) {
            return Result.failed("g100005", MessageUtils.get("g100005",countryCode));
        } else if ("29".equals(errorCode)) {
            return Result.failed("g091131", MessageUtils.get("g091131",countryCode));
        } else if ("31".equals(errorCode)) {
            return Result.failed("g100005", MessageUtils.get("g100005",countryCode));
        } else if ("33".equals(errorCode)) {
            return Result.failed("g091132", MessageUtils.get("g091132",countryCode));
        } else if ("35".equals(errorCode)) {
            return Result.failed("g091133", MessageUtils.get("g091133",countryCode));
        } else if ("100".equals(errorCode)) {
            return Result.failed("g091134", MessageUtils.get("g091134",countryCode));
        } else if ("101".equals(errorCode)) {
            return Result.failed("g091090", MessageUtils.get("g091090",countryCode));
        } else if ("102".equals(errorCode)) {
            return Result.failed("g091135", MessageUtils.get("g091135",countryCode));
        } else if ("103".equals(errorCode)) {
            return Result.failed("g091136", MessageUtils.get("g091136",countryCode));
        } else if ("104".equals(errorCode)) {
            return Result.failed("g091137", MessageUtils.get("g091137",countryCode));
        } else if ("105".equals(errorCode)) {
            return Result.failed("g091138", MessageUtils.get("g091138",countryCode));
        } else if ("106".equals(errorCode)) {
            return Result.failed("g091139", MessageUtils.get("g091139",countryCode));
        } else if ("107".equals(errorCode)) {
            return Result.failed("g091140", MessageUtils.get("g091140",countryCode));
        } else if ("108".equals(errorCode)) {
            return Result.failed("g091141", MessageUtils.get("g091141",countryCode));
        } else if ("200".equals(errorCode)) {
            return Result.failed("g091142", MessageUtils.get("g091142",countryCode));
        } else if ("201".equals(errorCode)) {
            return Result.failed("g091143", MessageUtils.get("g091143",countryCode));
        } else if ("202".equals(errorCode)) {
            return Result.failed("g091144", MessageUtils.get("g091144",countryCode));
        } else {
            return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
