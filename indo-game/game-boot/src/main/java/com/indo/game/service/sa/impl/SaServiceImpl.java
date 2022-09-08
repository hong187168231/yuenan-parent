package com.indo.game.service.sa.impl;

import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.FCHashAESEncrypt;
import com.indo.game.common.util.SAJEncryption;
import com.indo.game.common.util.XmlUtil;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.sa.SaKickUserResp;
import com.indo.game.pojo.dto.sa.SaLoginResp;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.sa.SaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaServiceImpl implements SaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;


    @Override
    public Result saGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("salog {} saGame account:{},saCodeId:{}", parentName, loginUser.getAccount(), platform);
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
//        //验证站点余额
//        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
//            logger.info("站点sa余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }

        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), parentName);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(GeneratorIdUtil.generateId());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);

                // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
                return createMemberGame(cptOpenMember, gameParentPlatform, isMobileLogin, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                //先退出
                this.logout(loginUser,platform,ip,countryCode);

                logger.info("salogin ip{} saGame account:{},platform:{},parentName:{}", ip, loginUser.getAccount(), platform,parentName);
                String time = DateUtils.getLongDateString(new Date());
                String qs = "method=KickUser&Key=" + OpenAPIProperties.SA_SECRET_KEY + "&Time=" + time + "&Username=" + cptOpenMember.getUserName();
                Map<String, Object> param = new HashMap<>();
                param.put("method", "KickUser");
                param.put("Key", OpenAPIProperties.SA_SECRET_KEY);
                param.put("Time", time);
                param.put("Username", loginUser.getAccount());
                param.put("s", FCHashAESEncrypt.encryptMd5(qs + OpenAPIProperties.SA_MD5KEY + time + OpenAPIProperties.SA_SECRET_KEY));
                param.put("q", SAJEncryption.encrypt(qs, OpenAPIProperties.SA_ENCRYPT_KEY));
                // 用户离线
                Object result = loginOutRequst(OpenAPIProperties.SA_API_URL, param);
                if (null == result) {
                    return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
                }

                SaKickUserResp saLoginResp = (SaKickUserResp) result;
                if (saLoginResp.getErrorMsgId() == 0) {
                    String time1 = DateUtils.getLongDateString(new Date());
                    String qs1 = "method=LoginRequest&Key=" + OpenAPIProperties.SA_SECRET_KEY + "&Time=" + time1 +
                            "&Username=" + cptOpenMember.getUserName() + "&CurrencyType=" + gameParentPlatform.getCurrencyType();
                    Map<String, Object> param1 = new HashMap<>();
                    param1.put("method", "LoginRequest");
                    param1.put("Key", OpenAPIProperties.SA_SECRET_KEY);
                    param1.put("Time", time1);
                    param1.put("Username", cptOpenMember.getUserName());
                    param1.put("CurrencyType", gameParentPlatform.getCurrencyType());
                    param1.put("s", FCHashAESEncrypt.encryptMd5(qs1 + OpenAPIProperties.SA_MD5KEY + time1 + OpenAPIProperties.SA_SECRET_KEY));
                    param1.put("q", SAJEncryption.encrypt(qs1, OpenAPIProperties.SA_ENCRYPT_KEY));
                    // 用户注册
                    Object result1 = commonRequest(OpenAPIProperties.SA_API_URL, param1);
                    if (null == result1) {
                        return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
                    }
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
                    String lang = "";
                    if(null!=countryCode&&!"".equals(countryCode)){
                        switch (countryCode) {
                            case "IN":
                                lang = "hi";
                                break;
                            case "EN":
                                lang = "en_US";
                                break;
                            case "CN":
                                lang = "zh_CN";
                                break;
                            case "VN":
                                lang = "vn";
                                break;
                            case "TW":
                                lang = "zh_TW";
                                break;
                            case "TH":
                                lang = "th";
                                break;
                            case "ID":
                                lang = "id";
                                break;
                            case "MY":
                                lang = "ms";
                                break;
                            case "JP":
                                lang = "jp";
                                break;
                            default:
                                lang = gameParentPlatform.getLanguageType();
                                break;
                        }
                    }else{
                        lang = gameParentPlatform.getLanguageType();
                    }
                    SaLoginResp saLoginResp1 = (SaLoginResp) result1;
                    if (0 == saLoginResp1.getErrorMsgId()) {
                        // 请求URL
                        ApiResponseData responseData = new ApiResponseData();
                        // PC
                        if (isMobileLogin.equals("1")) {
                            responseData.setPathUrl(initLoginUrl(cptOpenMember.getUserName(), saLoginResp1.getToken(),lang, false));
                        } else {
                            responseData.setPathUrl(initLoginUrl(cptOpenMember.getUserName(), saLoginResp1.getToken(),lang, true));
                        }
                        return Result.success(responseData);
                    } else {
                        return errorCode(saLoginResp1.getErrorMsgId().toString(), saLoginResp1.getErrorMsg(),countryCode);
                    }
                }
                return errorCode(saLoginResp.getErrorMsgId().toString(), saLoginResp.getErrorMsg(),countryCode);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip,String countryCode) {
        logger.info("salogout ip{} saGame account:{},platform:{},parentName:{}", ip, loginUser.getAccount(), platform);
        try {
            String time = DateUtils.getLongDateString(new Date());
            String qs = "method=KickUser&Key=" + OpenAPIProperties.SA_SECRET_KEY + "&Time=" + time;
            Map<String, Object> param = new HashMap<>();
            param.put("method", "KickUser");
            param.put("Key", OpenAPIProperties.SA_SECRET_KEY);
            param.put("Time", time);
            param.put("Username", loginUser.getAccount());
            param.put("s", FCHashAESEncrypt.encryptMd5(qs + OpenAPIProperties.SA_MD5KEY + time + OpenAPIProperties.SA_SECRET_KEY));
            param.put("q", SAJEncryption.encrypt(qs, OpenAPIProperties.SA_ENCRYPT_KEY));
            // 用户离线
            Object result = loginOutRequst(OpenAPIProperties.SA_API_URL, param);
            if (null == result) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }

            SaLoginResp saLoginResp = (SaLoginResp) result;
            if (saLoginResp.getErrorMsgId() == 0) {
                return Result.success();
            }
            return errorCode(saLoginResp.getErrorMsgId().toString(), saLoginResp.getErrorMsg(),countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @param isMobileLogin 1-pc 其他-web
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, String isMobileLogin,String countryCode) {

        String time = DateUtils.getLongDateString(new Date());
        String qs = "method=LoginRequest&Key=" + OpenAPIProperties.SA_SECRET_KEY + "&Time=" + time +
                "&Username=" + cptOpenMember.getUserName() + "&CurrencyType=" + gameParentPlatform.getCurrencyType();
        Map<String, Object> param = new HashMap<>();
        param.put("method", "LoginRequest");
        param.put("Key", OpenAPIProperties.SA_SECRET_KEY);
        param.put("Time", time);
        param.put("Username", cptOpenMember.getUserName());
        param.put("CurrencyType", gameParentPlatform.getCurrencyType());
        param.put("s", FCHashAESEncrypt.encryptMd5(qs + OpenAPIProperties.SA_MD5KEY + time + OpenAPIProperties.SA_SECRET_KEY));
        param.put("q", SAJEncryption.encrypt(qs, OpenAPIProperties.SA_ENCRYPT_KEY));
        // 用户注册
        Object result = commonRequest(OpenAPIProperties.SA_API_URL, param);
        if (null == result) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "hi";
                    break;
                case "EN":
                    lang = "en_US";
                    break;
                case "CN":
                    lang = "zh_CN";
                    break;
                case "VN":
                    lang = "vn";
                    break;
                case "TW":
                    lang = "zh_TW";
                    break;
                case "TH":
                    lang = "th";
                    break;
                case "ID":
                    lang = "id";
                    break;
                case "MY":
                    lang = "ms";
                    break;
                case "JP":
                    lang = "jp";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
        SaLoginResp saLoginResp = (SaLoginResp) result;
        if (0 == saLoginResp.getErrorMsgId()) {
            externalService.saveCptOpenMember(cptOpenMember);
            // 请求URL
            ApiResponseData responseData = new ApiResponseData();
            // PC
            if (isMobileLogin.equals("1")) {
                responseData.setPathUrl(initLoginUrl(cptOpenMember.getUserName(), saLoginResp.getToken(),lang, false));
            } else {
                responseData.setPathUrl(initLoginUrl(cptOpenMember.getUserName(), saLoginResp.getToken(),lang, true));
            }
            return Result.success(responseData);
        } else {
            return errorCode(saLoginResp.getErrorMsgId().toString(), saLoginResp.getErrorMsg(), countryCode);
        }
    }

    /**
     * 拼接返回登录URL
     * @param username 用户名
     * @param token token
     * @param lang 语言
     * @param mobile 是否手机登录
     * @return String
     */
    private String initLoginUrl(String username, String token, String lang, boolean mobile){
        String url = OpenAPIProperties.SA_WEB_URL
                + "?username=" + username + "&token=" + token
                + "&lobby=" + OpenAPIProperties.SA_LOBBYCODE
                + "&lang=" + lang + "&mobile=" + mobile;
        return url;
    }

    /**
     * 公共请求
     */
    private Object commonRequest(String apiUrl, Map<String, Object> params) {
//        Map<String, String> header = new HashMap<>();
//        header.put("Content-Type", "application/json");
//        header.put("accept", "application/json");
        return XmlUtil.convertXmlStrToObject(SaLoginResp.class, GameUtil.postForm4PP(apiUrl, params, null));
    }

    /**
     * 公共请求
     */
    private Object loginOutRequst(String apiUrl, Map<String, Object> params) {
//        Map<String, String> header = new HashMap<>();
//        header.put("Content-Type", "application/json");
//        header.put("accept", "application/json");
        return XmlUtil.convertXmlStrToObject(SaKickUserResp.class, GameUtil.postForm4PP(apiUrl, params, null));
    }


    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        108 用户名长度或者格式错误
            case "108":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
//        113 用户名已存在
            case "113":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));

//        114  币种不存在
            case "114":
                return Result.failed("g100001", MessageUtils.get("g100001",countryCode));

//        133  建立帐户失败
            case "133":
                return Result.failed("g100004", MessageUtils.get("g100004",countryCode));
// 100  用户名错误
            case "100":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
// 102: 密钥错误
            case "102":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//104: 服务器不可用
            case "104":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//116: 用户名不存在
            case "116":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
//125: 强制用户离线失败
            case "125":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//128: 解密错误
            case "128":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g100104",countryCode));
        }
    }
}
