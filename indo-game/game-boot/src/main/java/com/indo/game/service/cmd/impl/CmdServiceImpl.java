package com.indo.game.service.cmd.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.cmd.CmdService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class CmdServiceImpl implements CmdService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;

    @Override
    public Result cmdGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("cmd体育log  cmdGame loginUser:{}, ip:{}, platform:{}, parentName:{}, isMobileLogin:{}", loginUser,ip,platform,parentName,isMobileLogin);
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
        //验证站点余额
        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("站点cmd余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed("g300004", "会员余额不足");
        }

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

                String returnResult = GameUtil.httpGetWithCookies(getLoginOutUrl(loginUser.getAccount()), null, null);
                JSONObject result = JSONObject.parseObject(returnResult);

                logger.info("cmd体育log  登录cmdGame输入 urlapi:{}",getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), gameParentPlatform.getLanguageType(), isMobileLogin));
                //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
                if(null!=countryCode&&!"".equals(countryCode)){
                    switch (countryCode) {
                        case "IN":
                            countryCode = "en-US";
                        case "EN":
                            countryCode = "en-US";
                        case "CN":
                            countryCode = "zh-CN";
                        case "VN":
                            countryCode = "vi-VN";
                        case "TW":
                            countryCode = "zh-TW";
                        case "TH":
                            countryCode = "th-TH";
                        case "ID":
                            countryCode = "in-ID";
                        case "MY":
                            countryCode = "ms-MY";
                        case "KR":
                            countryCode = "ko-KR";
                        case "JP":
                            countryCode = "ja-JP";
                        default:
                            countryCode = gameParentPlatform.getLanguageType();
                    }
                }else{
                    countryCode = gameParentPlatform.getLanguageType();
                }
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(),
                        countryCode, isMobileLogin));
                logger.info("cmd体育log  登录cmdGame返回 responseData:{}",responseData);
                return Result.success(responseData);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("cmdlogout {} cmdGame account:{},cmdCodeId:{}", ip, loginUser.getAccount(), platform);
        try {
            logger.info("cmd体育log  登出logout输入 loginUser:{}, ip:{}, urlapi:{}", loginUser,ip,getLoginOutUrl(loginUser.getAccount()));
            String returnResult = GameUtil.httpGetWithCookies(getLoginOutUrl(loginUser.getAccount()), null, null);
            logger.info("cmd体育log  登出logout返回 responseData:{}",returnResult);
            JSONObject result = JSONObject.parseObject(returnResult);
            if (null == result) {
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
            if (0 == result.getInteger("Code")) {
                return Result.success();
            } else {
                return errorCode(result.getString("Code"), result.getString("Message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
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
            return Result.failed("g091087", "第三方请求异常！");
        }

        if (0 == result.getInteger("Code")) {
            logger.info("cmd体育log  登录createMemberGame输入 urlapi:{}",getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), gameParentPlatform.getLanguageType(), isMobileLogin));
            //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        countryCode = "en-US";
                    case "EN":
                        countryCode = "en-US";
                    case "CN":
                        countryCode = "zh-CN";
                    case "VN":
                        countryCode = "vi-VN";
                    case "TW":
                        countryCode = "zh-TW";
                    case "TH":
                        countryCode = "th-TH";
                    case "ID":
                        countryCode = "in-ID";
                    case "MY":
                        countryCode = "ms-MY";
                    case "KR":
                        countryCode = "ko-KR";
                    case "JP":
                        countryCode = "ja-JP";
                    default:
                        countryCode = gameParentPlatform.getLanguageType();
                }
            }else{
                countryCode = gameParentPlatform.getLanguageType();
            }
            // 请求URL
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(
                    getStartGame(cptOpenMember, gameParentPlatform.getCurrencyType(), countryCode, isMobileLogin));
            logger.info("cmd体育log  登录createMemberGame返回 responseData:{}",responseData);
            return Result.success(responseData);
        } else {
            return errorCode(result.getString("Code"), result.getString("Message"));
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


    public Result errorCode(String errorCode, String errorMessage) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        2 Key 验证失败
            case "-1000":
                return Result.failed("g100104", errorMessage);
//        9 AgentId 不存在 玩家账号与 AgentId 不匹配 (详见 Chapter             No authorized to access
            case "-999":
                return Result.failed("g009999", errorMessage);

//        104 執⾏营运商于 auth 回传错误或禁用的货币                            Domain is null or the length of domain less than 2.
            case "-103":
                return Result.failed("g000002", errorMessage);

//        101 会员账号不存在/不在在线                                   Failed to pass the domain validation.
            case "-102":
                return Result.failed("g000008", errorMessage);
            case "-101":
                return Result.failed("g000001", errorMessage);
            case "-100":
                return Result.failed("g000007", errorMessage);
            case "-98":
                return Result.failed("g100003", errorMessage);
            case "-97":
                return Result.failed("g010001", errorMessage);
            case "-96":
                return Result.failed("g300003", errorMessage);
            case "-95":
                return Result.failed("g000008", errorMessage);
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
