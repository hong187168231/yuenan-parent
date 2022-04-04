package com.indo.game.service.dj.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.web.util.http.HttpUtils;
import com.indo.game.common.util.SnowflakeId;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.dj.DjService;
import com.indo.game.service.ps.PsService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lombok.extern.slf4j.Slf4j;


/**
 * PG
 *
 * @author
 */
@Slf4j
@Service
public class DjServiceImpl implements DjService {

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
    public Result djGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("djlog  {} jkGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
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
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setPassword(SnowflakeId.generateId().toString());
                updateCptOpenMember.setLoginTime(new Date());
                updateCptOpenMember.setId(cptOpenMember.getId());
                externalService.updateCptOpenMember(updateCptOpenMember);
            }
            String aeApiResponseData = gameInit(cptOpenMember, isMobileLogin);
            if (null == aeApiResponseData || "".equals(aeApiResponseData)) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            String name = gameLogin(aeApiResponseData, platformGameParent, cptOpenMember, isMobileLogin);

            //登录
            ApiResponseData responseData = new ApiResponseData();
            //  responseData.setPathUrl(builder.toString());
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    private String gameLogin(String aeApiResponseData, GameParentPlatform platformGameParent, CptOpenMember cptOpenMember, String isMobileLogin) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("session_id", aeApiResponseData);
        params.put("lang", platformGameParent.getLanguageType());
        params.put("login_id", cptOpenMember.getUserId() + "");
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(OpenAPIProperties.DJ_API_URL).append("/api/cash/auth");
            String result = commonRequest(apiUrl.toString(), params, cptOpenMember.getUserId(), "gameLogin");
            if (!StringUtils.isEmpty(result)) {
                Document doc = commonXml(result);
                String errorCode = doc.getElementsByTagName("status_code").item(0).getTextContent();
                if (StringUtils.isEmpty(result) && errorCode.equals("00")) {
                    return doc.getElementsByTagName("session_id").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            logger.error("aelog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return "";
    }

    private String gameInit(CptOpenMember cptOpenMember, String isMobileLogin) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", OpenAPIProperties.DJ_API_KEY);
        params.put("agent_code", OpenAPIProperties.DJ_AGENT_CODE);
        params.put("login_id", cptOpenMember.getUserId() + "");
        params.put("name", cptOpenMember.getUserName());
        try {
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append("https://api8745.cfb2.net").append("/get_session_id.aspx");
            String result = commonRequest(apiUrl.toString(), params, cptOpenMember.getUserId(), "gameLogin");
            if (!StringUtils.isEmpty(result)) {
                Document doc = commonXml(result);
                String errorCode = doc.getElementsByTagName("status_code").item(0).getTextContent();
                if (StringUtils.isEmpty(result) && errorCode.equals("00")) {
                    return doc.getElementsByTagName("session_id").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            logger.error("aelog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return "";
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
            StringBuilder apiUrl = new StringBuilder();
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", OpenAPIProperties.DJ_API_KEY);
            params.put("agent_code", OpenAPIProperties.DJ_AGENT_CODE);
            params.put("login_id", loginUser.getId() + "");
            apiUrl.append(OpenAPIProperties.DJ_API_URL).append("/disable_player.aspx");
            String result = commonRequest(apiUrl.toString(), params, loginUser.getId().intValue(), "gameLogin");
            if (!StringUtils.isEmpty(result)) {
                Document doc = commonXml(result);
                String errorCode = doc.getElementsByTagName("status_code").item(0).getTextContent();
                if (StringUtils.isEmpty(result) && errorCode.equals("00")) {
                    return Result.success();
                }
            }
        } catch (Exception e) {
            logger.error("djlog  djlog out:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
        return Result.failed();
    }


    /**
     * 公共请求
     */
    public String commonRequest(String apiUrl, Map<String, String> params, Integer userId, String type) throws Exception {
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, apiUrl, params, type, userId);
        logger.info("aelog {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                userId, type, null, resultString, resultString, resultString);

      /*  logger.info("djlog  {} commonRequest userId:{},paramsMap:{}", userId, params);
        String repXML = HttpUtils.doGet(apiUrl, params);
        logger.info("djlog  apiResponse:" + repXML);
        if (StringUtils.isNotEmpty(repXML)) {
            logger.info("djlog  {}:commonRequest type:{}, operateFlag:{}, hostName:{}, params:{}, result:{}, awcApiResponse:{}",
                    userId, type, null, params, repXML, repXML);
        }*/
        return resultString;
    }

    public static Document commonXml(String repXML) {
        Document doc = null;
        try {
            StringReader sr = new StringReader(repXML);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            return doc;
        } catch (Exception e) {
            log.error("djlog commonXml error {}", e);
            return doc;
        }
    }
}
