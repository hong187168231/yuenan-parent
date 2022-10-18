package com.indo.game.service.sgwin.impl;

import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.encrypt.MD5;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.common.util.RSAUtils;
import com.indo.game.common.util.SgwinEncrypt;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.dto.sgwin.SgwinApiResp;
import com.indo.game.pojo.dto.sgwin.SgwinLoginRequest;
import com.indo.game.pojo.dto.sgwin.SgwinRequest;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.sgwin.SGWinService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class SGWinServiceImpl implements SGWinService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;

    private HttpURLConnection conn;
    private String hashValue;
    private String postParams;

    @Override
    public Result sgwinGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("SGWinlog  {} SGWinGame account:{}, pgCodeId:{}", loginUser.getId(), loginUser.getNickName(), platform);
        // 是否开售校验
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == platformGameParent) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("0".equals(platformGameParent.getIsStart())) {
            return Result.failed("g100101", MessageUtils.get("g100101",countryCode));
        }
        if ("1".equals(platformGameParent.getIsOpenMaintenance())) {
            return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
        }
        GamePlatform gamePlatform = new GamePlatform();
        if (!platform.equals(parentName)) {
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platform,parentName);
            if (null == gamePlatform) {
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
            }
            if ("0".equals(gamePlatform.getIsStart())) {
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", MessageUtils.get("g091047",countryCode));
            }
        }
//        BigDecimal balance = loginUser.getBalance();
//        //验证站点棋牌余额
//        if (null == balance || BigDecimal.ZERO.equals(balance)) {
//            logger.info("站点SGWin余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
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
//                logout(loginUser, platform, ip, countryCode);
            }

            SgwinApiResp sgwinApiResp = gameLogin( cptOpenMember,  countryCode, platformGameParent);
            if(sgwinApiResp.getSuccess()){
                JSONObject jsonObject = JSONObject.parseObject(sgwinApiResp.getResult());
                //登录
                ApiResponseData responseData = new ApiResponseData();
                String session = jsonObject.getString("session");
                String url = "";
                if(!"1".equals(isMobileLogin)){//1：手机 0:PC
                    url = OpenAPIProperties.SGWIN_LOGIN_URL+"/member/index?_OLID_="+session;
                }else {
                    url = OpenAPIProperties.SGWIN_LOGIN_URL+"/mobile/member/index?_OLID_="+session;
                }

                responseData.setPathUrl(url);
                return Result.success(responseData);
            }else {
                return this.errorCode(sgwinApiResp.getError(),sgwinApiResp.getMessage(), countryCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * @return
     */
    /**
     * 调用API登录
     */
    private SgwinApiResp gameLogin(CptOpenMember cptOpenMember,String  countryCode,GameParentPlatform platformGameParent) {
        String apiKey = OpenAPIProperties.SGWIN_API_KEY;
        String root = OpenAPIProperties.SGWIN_AGENT;

        String website = OpenAPIProperties.SGWIN_API_URL;
        String urlLogin = website + "/api/login";
//        String urlLogout = website + "/api/logout";
//        String urlTransaction = website + "/api/transaction";
//        String urlconfirm = website + "/api/confirm";
//        String urlAccount = website + "/api/account";
//        String urlOnline = website + "/api/online";
//        String urlLastBets = website + "/api/lastbets";
//        String urlBets = website + "/api/bets";
        Long timestamp = new Date().getTime();
        String rawData = root + root + cptOpenMember.getUserName() + 1 + timestamp ;
//        String hashValue = this.calcuHashValue(rawData);
        try {
            hashValue = RSAUtils.privateEncrypt(rawData,RSAUtils.getPrivateKey(apiKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        postParams = "root=" + root + "&username=" + cptOpenMember.getUserName() + "&hash=" + hashValue;
        SgwinLoginRequest sgwinLoginRequest = new SgwinLoginRequest();
        sgwinLoginRequest.setRoot(root);
        sgwinLoginRequest.setAgentID(root);
        sgwinLoginRequest.setUsername(cptOpenMember.getUserName());
        sgwinLoginRequest.setUserType(1);
        sgwinLoginRequest.setTimestamp(timestamp);
        sgwinLoginRequest.setSign(hashValue);
        sgwinLoginRequest.setDefaultBgColor("black");
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "EN";
                    break;
                case "EN":
                    lang = "EN";
                    break;
                case "CN":
                    lang = "CN";
                    break;
                case "VN":
                    lang = "VN";
                    break;
                default:
                    lang = platformGameParent.getLanguageType();
                    break;
            }
        }else{
            lang = platformGameParent.getLanguageType();
        }
        sgwinLoginRequest.setDefaultLang(lang);

        String jsonStr = JSON.toJSONString(sgwinLoginRequest);
        SgwinApiResp sgwinApiResp = null;
        try {
//            logger.info("SGWin  gameLogin登录请求apiUrl:{}, jsonParams:{}, rawData:{}", urlLogin, postParams, rawData);
            logger.info("SGWin  gameLogin登录请求apiUrl:{}, jsonParams:{}", urlLogin, jsonStr);
            sgwinApiResp = commonRequest(urlLogin, jsonStr, cptOpenMember.getUserId(), "gameLogin");
            logger.info("SGWin  gameLogin登录返回resultString:{}", JSON.toJSONString(sgwinApiResp));
        } catch (Exception e) {
            logger.error("aelog aeGameLogin:{}", e);
            e.printStackTrace();
        }
        return sgwinApiResp;
    }



    /**
     * 强迫登出玩家
     */
    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        try {
            String apiKey = OpenAPIProperties.SGWIN_API_KEY;
            String root = OpenAPIProperties.SGWIN_AGENT;

            String website = OpenAPIProperties.SGWIN_API_URL;
            String urlLogout = website + "/api/logout";
            Long timestamp = new Date().getTime();
            String rawData = root + root + account + 1 + timestamp ;
//            String hashValue = this.calcuHashValue(rawData);
            try {
                hashValue = RSAUtils.privateEncrypt(rawData,RSAUtils.getPrivateKey(apiKey));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            postParams = "root=" + root + "&username=" + account + "&hash=" + hashValue;
            SgwinRequest sgwinRequest = new SgwinRequest();
            sgwinRequest.setRoot(root);
            sgwinRequest.setAgentID(root);
            sgwinRequest.setUsername(account);
            sgwinRequest.setUserType(1);
            sgwinRequest.setTimestamp(timestamp);
            sgwinRequest.setSign(hashValue);
            String jsonStr = JSON.toJSONString(sgwinRequest);
            SgwinApiResp sgwinApiResp = null;
            logger.info("SGWin  logout注销请求apiUrl:{}, jsonParams:{}, 加密前参数rawData:{}", urlLogout, jsonStr, rawData);
            sgwinApiResp = commonRequest(urlLogout, jsonStr, 0, "logout");
            logger.info("SGWin  logout注销返回resultString:{}", JSON.toJSONString(sgwinApiResp));
            if(sgwinApiResp.getSuccess()){
                return Result.success();
            }else {
                return this.errorCode(sgwinApiResp.getError(),sgwinApiResp.getMessage(), countryCode);
            }
        } catch (Exception e) {
            logger.error("BLlog  BLlogout:{}", e);
            e.printStackTrace();
            return Result.failed();
        }
    }

    /**
     * 公共请求
     */
    public SgwinApiResp commonRequest(String apiUrl, String jsonStr, Integer userId, String type) throws Exception {
        SgwinApiResp sgwinApiResp = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,
            apiUrl, jsonStr, type, userId);
        if (StringUtils.isNotEmpty(resultString)) {
            sgwinApiResp = JSONObject.parseObject(resultString, SgwinApiResp.class);
        }
        return sgwinApiResp;
    }

    private String sendPost(String url, String postParams) throws Exception {

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // Acts like a browser to post
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        return response.toString();
    }

    public String calcuHashValue(String rawData) {
        return SgwinEncrypt.md5(rawData);
    }

    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
        if ("E0016.member.offline".equals(errorCode)) {//会员不在线
            return Result.failed("g091088", MessageUtils.get("\"g091088\"",countryCode));
        }else if ("E0003.user.not.found".equals(errorCode)) {//用户不存在
            return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
        }else  if ("E0015.validate.username".equals(errorCode)) {//请输入帐号
            return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
        }else  if ("E0003.user.not.found".equals(errorCode)) {//只输入英文字母和数字
            return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
        }else  if ("E0018.validate.username.length".equals(errorCode)) {//用户名要最少2个字位
            return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
        }else  if ("E0019.validate.username.length.max.32".equals(errorCode)) {//帐号最多32位
            return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
        }else  if ("E0020.validate.account.exist".equals(errorCode)) {//帐号已存在
            return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
        }else  if ("E0025.validate.users.range".equals(errorCode)) {//盘口不能为空
            return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
        }else  if ("E0015.login.error.status.5".equals(errorCode)) {//抱歉!你的帐号被停用了。
            return Result.failed("g200002", MessageUtils.get("g200002",countryCode));
        }else  if ("E0022.validate.users.range".equals(errorCode)) {//盘口{X}不可使用。{X}为 A, B, C, D
            return Result.failed("g100110", MessageUtils.get("g100110",countryCode));
        }else{
            return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
