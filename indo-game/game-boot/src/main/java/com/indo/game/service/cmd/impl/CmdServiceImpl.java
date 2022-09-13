package com.indo.game.service.cmd.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.cmd.CmdService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CmdServiceImpl implements CmdService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;
    @Override
    public Result cmdGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("cmd体育log  cmdGame loginUser:{}, ip:{}, platform:{}, parentName:{}, isMobileLogin:{}", loginUser,ip,platform,parentName,isMobileLogin);
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
//            logger.info("站点cmd余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
//            //站点棋牌余额不足
//            return Result.failed("g300004", MessageUtils.get("g300004",countryCode));
//        }
        gameLogoutService.gamelogout(loginUser.getAccount(),  ip,  countryCode);
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
                externalService.saveCptOpenMember(cptOpenMember);

                // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
                return createMemberGame(cptOpenMember, gameParentPlatform, isMobileLogin, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);

//                String returnResult = GameUtil.httpGetWithCookies(getLoginOutUrl(loginUser.getAccount()), null, null);
//                JSONObject result = JSONObject.parseObject(returnResult);

                logger.info("cmd体育log  登录cmdGame输入 urlapi:{}",getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), gameParentPlatform.getLanguageType(), isMobileLogin));
                //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
                String lang = "";
                if(null!=countryCode&&!"".equals(countryCode)){
                    switch (countryCode) {
                        case "IN":
                            lang = "en-US";
                            break;
                        case "EN":
                            lang = "en-US";
                            break;
                        case "CN":
                            lang = "zh-CN";
                            break;
                        case "VN":
                            lang = "vi-VN";
                            break;
                        case "TW":
                            lang = "zh-TW";
                            break;
                        case "TH":
                            lang = "th-TH";
                            break;
                        case "ID":
                            lang = "in-ID";
                            break;
                        case "MY":
                            lang = "ms-MY";
                            break;
                        case "KR":
                            lang = "ko-KR";
                            break;
                        case "JP":
                            lang = "ja-JP";
                            break;
                        default:
                            lang = gameParentPlatform.getLanguageType();
                            break;
                    }
                }else{
                    lang = gameParentPlatform.getLanguageType();
                }
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(),
                        lang, isMobileLogin));
                logger.info("cmd体育log  登录cmdGame返回 responseData:{}",responseData);
                return Result.success(responseData);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("cmdlogout {} cmdGame account:{},cmdCodeId:{}", ip, account ,platform);
        try {
            logger.info("cmd体育log  登出logout输入 loginUser:{}, ip:{}, urlapi:{}", account,ip,getLoginOutUrl(account));
            String returnResult = GameUtil.httpGetWithCookies(getLoginOutUrl(account), null, null);
            logger.info("cmd体育log  登出logout返回 responseData:{}",returnResult);
            JSONObject result = JSONObject.parseObject(returnResult);
            if (null == result) {
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
            }
            if (0 == result.getInteger("Code")) {
                return Result.success();
            } else {
                return errorCode(result.getString("Code"), result.getString("Message"),countryCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    /**
     * 登录游戏, 第一次登录会自动创建账号
     *
     * @param cptOpenMember cptOpenMember
     * @return Result
     */
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, String isMobileLogin,String countryCode) {
        logger.info("cmd体育log  创建账号createMemberGame输入 urlapi:{}",getLoginUrl(cptOpenMember.getPassword(), gameParentPlatform.getCurrencyType()));
        String returnResult
                = GameUtil.httpGetWithCookies(
                getLoginUrl(cptOpenMember.getPassword(), gameParentPlatform.getCurrencyType()), null, null);
        logger.info("cmd体育log  创建账号createMemberGame返回 responseData:{}",returnResult);
        JSONObject result = JSONObject.parseObject(returnResult);
        if (null == result) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }

        if (0 == result.getInteger("Code")) {
            logger.info("cmd体育log  登录createMemberGame输入 urlapi:{}",getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), gameParentPlatform.getLanguageType(), isMobileLogin));
            //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            String lang = "";
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        lang = "en-US";
                        break;
                    case "EN":
                        lang = "en-US";
                        break;
                    case "CN":
                        lang = "zh-CN";
                        break;
                    case "VN":
                        lang = "vi-VN";
                        break;
                    case "TW":
                        lang = "zh-TW";
                        break;
                    case "TH":
                        lang = "th-TH";
                        break;
                    case "ID":
                        lang = "in-ID";
                        break;
                    case "MY":
                        lang = "ms-MY";
                        break;
                    case "KR":
                        lang = "ko-KR";
                        break;
                    case "JP":
                        lang = "ja-JP";
                        break;
                    default:
                        lang = gameParentPlatform.getLanguageType();
                        break;
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            // 请求URL
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(
                    getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), lang, isMobileLogin));
            logger.info("cmd体育log  登录createMemberGame返回 responseData:{}",responseData);
            return Result.success(responseData);
        } else {
            return errorCode(result.getString("Code"), result.getString("Message"),countryCode);
        }
    }


    /**
     * 获取登录URL 账号
     * 、
     * https://<CustomizeEntry>/auth.aspx?lang=<LanguageCode>&user=<UserName>&token=<Tok
     * en>&currency=<CurrencyCode>&templatename=<TemplateName>&view=<View>
     *
     * @param cptOpenMember、 cptOpenMember
     * @return url
     */
    private String getStartGame(CptOpenMember cptOpenMember, String currency, String lang, String isMobileLogin) {

        StringBuilder url = new StringBuilder();
        if (isApp(isMobileLogin)) {
            url.append(OpenAPIProperties.CMD_NEWMOBILE_URL);
        } else {
            url.append(OpenAPIProperties.CMD_WEBROOT_URL);
        }
        url.append("/auth.aspx?");
        url.append("lang=").append(lang);
        url.append("&user=").append(cptOpenMember.getUserName());
        url.append("&token=").append(cptOpenMember.getPassword());
        url.append("&currency=").append(currency);
        url.append("&templatename=").append(OpenAPIProperties.CMD_TEMPLATE_NAME);
        url.append("&view=").append(OpenAPIProperties.CMD_VIEW);
        logger.info("getStartGame玩家登录地址输入, url:{}", url);
        return url.toString();
    }

    /**
     * 創建用戶CMD請求地址
     * /?Method=createmember&PartnerKey=<PartnerKey>&UserName=<UserName
     * >&Currency=<CurrencyCode>
     *
     * @return String
     */
    private String getLoginUrl(String userAccount, String currency) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.CMD_API_URL).append("/?Method=createmember");
        url.append("&PartnerKey=").append(OpenAPIProperties.CMD_PARTNER_KEY);
        url.append("&UserName=").append(userAccount);
        url.append("&Currency=").append(currency);
        logger.info("getLoginUrl創建用戶CMD請求地址输入, url:{}", url);
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     * kickuser&PartnerKey=<PartnerKey>&UserName=<UserName>
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getLoginOutUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.CMD_API_URL).append("/?Method=kickuser");
        url.append("&PartnerKey=").append(OpenAPIProperties.CMD_PARTNER_KEY);
        url.append("&UserName=").append(userAccount);
        logger.info("getLoginUrl玩家退出游戏API地址输入, url:{}", url);
        return url.toString();
    }

    private boolean isApp(String isMobileLogin) {
        return "1".equals(isMobileLogin);
    }


    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        2 Key 验证失败
            case "-1000":
                return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//        9 AgentId 不存在 玩家账号与 AgentId 不匹配 (详见 Chapter             No authorized to access
            case "-999":
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));

//        104 執⾏营运商于 auth 回传错误或禁用的货币                            Domain is null or the length of domain less than 2.
            case "-103":
                return Result.failed("g000002", MessageUtils.get("g000002",countryCode));

//        101 会员账号不存在/不在在线                                   Failed to pass the domain validation.
            case "-102":
                return Result.failed("g000008", MessageUtils.get("g000008",countryCode));
            case "-101":
                return Result.failed("g000001", MessageUtils.get("g000001",countryCode));
            case "-100":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            case "-98":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
            case "-97":
                return Result.failed("g010001", MessageUtils.get("g010001",countryCode));
            case "-96":
                return Result.failed("g300003", MessageUtils.get("g300003",countryCode));
            case "-95":
                return Result.failed("g000008", MessageUtils.get("g000008",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
