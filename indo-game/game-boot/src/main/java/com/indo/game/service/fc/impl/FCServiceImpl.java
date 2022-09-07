package com.indo.game.service.fc.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.FCHashAESEncrypt;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.fc.FCApiCommonResp;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.fc.FCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FCServiceImpl implements FCService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;

    @Override
    public Result fcGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("fclog {} fcGame account:{},kaCodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
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
//            logger.info("站点fc余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

                // 先退出
                Map<String, Object> loginouotParam = new HashMap<>();
                loginouotParam.put("MemberAccount", loginUser.getAccount());

                Map<String, Object> param = new HashMap<>();
                param.put("AgentCode", OpenAPIProperties.FC_AGENT_CODE);
                param.put("Currency", gameParentPlatform.getCurrencyType());
                param.put("Params", FCHashAESEncrypt.encrypt(JSONObject.toJSONString(loginouotParam), OpenAPIProperties.FC_AGENT_KEY));
                param.put("Sign", FCHashAESEncrypt.encryptMd5(JSONObject.toJSONString(loginouotParam)));

                String url = OpenAPIProperties.FC_API_URL + "/KickOut";
                JSONObject.parseObject(GameUtil.postForm4PP(url, param, null), FCApiCommonResp.class);

            }
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            String lang = "";
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        lang = "1";
                    case "EN":
                        lang = "1";
                    case "CN":
                        lang = "2";
                    case "VN":
                        lang = "3";
                    case "TH":
                        lang = "4";
                    default:
                        lang = gameParentPlatform.getLanguageType();
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            return getPlayerGameUrl(cptOpenMember.getUserName(), platform, gameParentPlatform.getCurrencyType(), lang, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip,String countryCode) {
        logger.info("fclogout {} fcGame account:{},t9CodeId:{}", ip, loginUser.getAccount(), platform);
        try {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.FC_PLATFORM_CODE);
            Map<String, Object> loginouotParam = new HashMap<>();
            loginouotParam.put("MemberAccount", loginUser.getAccount());

            Map<String, Object> param = new HashMap<>();
            param.put("AgentCode", OpenAPIProperties.FC_AGENT_CODE);
            param.put("Currency", gameParentPlatform.getCurrencyType());
            param.put("Params", FCHashAESEncrypt.encrypt(JSONObject.toJSONString(loginouotParam), OpenAPIProperties.FC_AGENT_KEY));
            param.put("Sign", FCHashAESEncrypt.encryptMd5(JSONObject.toJSONString(loginouotParam)));

            String url = OpenAPIProperties.FC_API_URL + "/KickOut";
            FCApiCommonResp fcApiCommonResp = JSONObject.parseObject(GameUtil.postForm4PP(url, param, null), FCApiCommonResp.class);
            if (null != fcApiCommonResp && fcApiCommonResp.getResult() == 0) {
                return Result.success();
            } else {
                return errorCode(fcApiCommonResp.getResult().toString(), countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 启动游戏获取游戏URL
     *
     * @return Result
     */
    private Result getPlayerGameUrl(String playerID, String gameCode, String currency, String lang,String countryCode) {
        try {
            Map<String, Object> loginParam = new HashMap<>();
            loginParam.put("MemberAccount", playerID);
            loginParam.put("GameID", gameCode);
            loginParam.put("LanguageID", lang);

            Map<String, Object> param = new HashMap<>();
            param.put("AgentCode", OpenAPIProperties.FC_AGENT_CODE);
            param.put("Currency", currency);
            param.put("Params", FCHashAESEncrypt.encrypt(JSONObject.toJSONString(loginParam), OpenAPIProperties.FC_AGENT_KEY));
            param.put("Sign", FCHashAESEncrypt.encryptMd5(JSONObject.toJSONString(loginParam)));

            String url = OpenAPIProperties.FC_API_URL + "/Login";
            FCApiCommonResp fcApiCommonResp = JSONObject.parseObject(GameUtil.postForm4PP(url, param, null), FCApiCommonResp.class);
            if (null != fcApiCommonResp && fcApiCommonResp.getResult() == 0) {
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(fcApiCommonResp.getUrl());
                return Result.success(responseData);
            } else {
                return errorCode(fcApiCommonResp.getResult().toString(), countryCode);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failed("g200001", "登录操作失败");
        }

    }


    private Result errorCode(String errorCode,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        200 会员账号不合法
            case "200":
                return Result.failed("g300002", MessageUtils.get("g300002",countryCode));
//        201 系统错误                                              No authorized to access
            case "201":
                return Result.failed("g300001", MessageUtils.get("g300001",countryCode));

//        202 API参数错误                            Domain is null or the length of domain less than 2.
            case "202":
                return Result.failed("g300002", MessageUtils.get("g300002",countryCode));

//        203 json 格式错误。                                          Failed to pass the domain validation.
            case "203":
                return Result.failed("g300004", MessageUtils.get("g300004",countryCode));

//        215 系统维护中。                     The encrypted data is null or the length of the encrypted data is equal to 0.
            case "215":
                return Result.failed("g091005", MessageUtils.get("g091005",countryCode));

//        217 不允许IP            Assertion(SAML) didn't pass the timestamp validation.
            case "217":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));

//        301 玩家已被锁定。                      Failed to extract the SAML parameters from the encrypted data.
            case "301":
                return Result.failed("g091116", MessageUtils.get("g091116",countryCode));

//        303 未知动作。                                            Unknow action.
            case "303":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));

//        304 Game On Maintenance。                                      The same value as before.
            case "304":
                return Result.failed("g091116", MessageUtils.get("g091116",countryCode));

//        401 游戏不存在。                                                Time out.
            case "401":
                return Result.failed("g000003", MessageUtils.get("g000003",countryCode));

//        405 游戏无法开启。                                            Read time out.
            case "405":
                return Result.failed("g100101", MessageUtils.get("g100101",countryCode));

//        406 商户被冻结。                                            Duplicate transactions.
            case "406":
                return Result.failed("g100102", MessageUtils.get("g100102",countryCode));

//        407 玩家已存在。                                          Please try again later.
            case "407":
                return Result.failed("g200003", MessageUtils.get("g200003",countryCode));

//        408 玩家不存在。                                            System is maintained.
            case "408":
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
//        410 玩家不存在。                                            System is maintained.
            case "410":
                return Result.failed("g000003", MessageUtils.get("g000003",countryCode));
            //        500 玩家不存在。                                            System is maintained.
            case "500":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            //        501 玩家不存在。                                            System is maintained.
            case "501":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
            //        502 玩家不存在。                                            System is maintained.
            case "502":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
            case "504":
                return Result.failed("g100103", MessageUtils.get("g100103",countryCode));
            //        410 玩家不存在。                                            System is maintained.
            case "505":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));
            case "604":
                return Result.failed("g100107", MessageUtils.get("g100107",countryCode));
            case "1012":
                return Result.failed("g100005", MessageUtils.get("g100005",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
