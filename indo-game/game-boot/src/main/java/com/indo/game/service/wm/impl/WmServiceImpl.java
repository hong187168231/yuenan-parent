package com.indo.game.service.wm.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.wm.WmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WmServiceImpl implements WmService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;

    @Override
    public Result wmGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("wmlog {} wmGame account:{},wmCodeId:{}", parentName, loginUser.getAccount(), platform);
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
//            logger.info("站点wm余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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

                // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
                return createMemberGame(cptOpenMember, gameParentPlatform, platform, countryCode);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                // 先退出
//                commonRequest(getLoginOutUrl(loginUser.getAccount()), null);
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
                String lang = "";
                if(null!=countryCode&&!"".equals(countryCode)){
                    switch (countryCode) {
                        case "IN":
                            lang = "6";
                            break;
                        case "EN":
                            lang = "1";
                            break;
                        case "CN":
                            lang = "0";
                            break;
                        case "VN":
                            lang = "3";
                            break;
                        case "TW":
                            lang = "9";
                            break;
                        case "TH":
                            lang = "3";
                            break;
                        case "ID":
                            lang = "8";
                            break;
                        case "MY":
                            lang = "7";
                            break;
                        case "KR":
                            lang = "5";
                            break;
                        case "JP":
                            lang = "4";
                            break;
                        default:
                            lang = gameParentPlatform.getLanguageType();
                            break;
                    }
                }else{
                    lang = gameParentPlatform.getLanguageType();
                }
                JSONObject jsonObject = getStartGame(cptOpenMember, lang, platform);
                logger.info("wm 启动游戏返回result:{}", jsonObject);
                if (jsonObject.getInteger("errorCode").equals(0)) {
                    // 请求URL
                    ApiResponseData responseData = new ApiResponseData();
                    responseData.setPathUrl(jsonObject.getString("result"));
                    return Result.success(responseData);
                } else {
                    return errorCode(jsonObject.getString("errorCode"), jsonObject.getString("errorMessage"), countryCode);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("wmlogout {} wmGame account:{},wmCodeId:{}", ip, account, platform);
        try {
            // 游戏退出
            logger.info("wmlogout 退出请求url:{}", getLoginOutUrl(account));
            JSONObject result = commonRequest(getLoginOutUrl(account), null);
            logger.info("wmlogout 退出返回result:{}", result);
            if (null == result) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }

            if (0 == result.getInteger("errorCode")) {
                return Result.success();
            } else {
                return errorCode(result.getString("errorCode"), result.getString("errorMessage"), countryCode);
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
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, String platform,String countryCode) {
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String lang = "";
        if(null!=countryCode&&!"".equals(countryCode)){
            switch (countryCode) {
                case "IN":
                    lang = "6";
                    break;
                case "EN":
                    lang = "1";
                    break;
                case "CN":
                    lang = "0";
                    break;
                case "VN":
                    lang = "3";
                    break;
                case "TW":
                    lang = "9";
                    break;
                case "TH":
                    lang = "3";
                    break;
                case "ID":
                    lang = "8";
                    break;
                case "MY":
                    lang = "7";
                    break;
                case "KR":
                    lang = "5";
                    break;
                case "JP":
                    lang = "4";
                    break;
                default:
                    lang = gameParentPlatform.getLanguageType();
                    break;
            }
        }else{
            lang = gameParentPlatform.getLanguageType();
        }
        // 用户注册
        JSONObject result = commonRequest(getLoginUrl(cptOpenMember, lang), null);
        logger.info("wm 用户注册返回result:{}", result);
        if (null == result) {
            return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
        }

        if (0 == result.getInteger("errorCode")) {
            externalService.saveCptOpenMember(cptOpenMember);
            // 启动游戏
            JSONObject jsonObject = getStartGame(cptOpenMember, gameParentPlatform.getLanguageType(), platform);
            logger.info("wm 启动游戏返回result:{}", jsonObject);
            if (null == jsonObject) {
                return Result.failed("g091087", MessageUtils.get("g091087",countryCode));
            }
            if (jsonObject.getInteger("errorCode").equals(0)) {
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jsonObject.getString("result"));
                return Result.success(responseData);
            } else {
                return errorCode(jsonObject.getString("errorCode"), jsonObject.getString("errorMessage"), countryCode);
            }
        } else {
            return errorCode(result.getString("errorCode"), result.getString("errorMessage"), countryCode);
        }
    }

    /**
     * 公共请求
     */
    private JSONObject commonRequest(String apiUrl, Map<String, Object> params) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accept", "application/json");
        String returnResult = GameUtil.postJson(apiUrl, params, header);
        return JSONObject.parseObject(returnResult);
    }

    /**
     * 获取登录URL 账号
     *
     * @param cptOpenMember、 cptOpenMember
     * @return url
     */
    private JSONObject getStartGame(CptOpenMember cptOpenMember, String lang, String platform) {

        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.WM_API_URL);
        url.append("cmd=").append("LoginGame");
        url.append("&vendorId=").append(OpenAPIProperties.WM_VENDORID);
        url.append("&signature=").append(OpenAPIProperties.WM_SIGNATURE);
        url.append("&user=").append(cptOpenMember.getUserName());
        url.append("&password=").append(cptOpenMember.getPassword());
        url.append("&lang=").append(lang);
//        url.append("&mode=").append(cptOpenMember.getUserName());
//        url.append("&voice=").append(cptOpenMember.getUserName());
        url.append("&timestamp=").append(getGMT8TimeLength10());
        logger.info("wm 登录URL请求url:{}", url.toString());
        return commonRequest(url.toString(), null);
    }

    /**
     * 創建用戶wm請求地址
     *
     * @return String
     */
    private String getLoginUrl(CptOpenMember cptOpenMember, String lang) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.WM_API_URL);
        url.append("cmd=").append("MemberRegister");
        url.append("&vendorId=").append(OpenAPIProperties.WM_VENDORID);
        url.append("&signature=").append(OpenAPIProperties.WM_SIGNATURE);
        url.append("&user=").append(cptOpenMember.getUserName());
        url.append("&password=").append(cptOpenMember.getPassword());
        url.append("&username=").append(cptOpenMember.getUserName());
        url.append("&timestamp=").append(getGMT8TimeLength10());
//        url.append("&syslang=").append(lang);
        logger.info("wm 創建用戶URL请求url:{}", url.toString());
        return url.toString();
    }

    /**
     * 玩家退出游戏API地址
     *
     * @param userAccount userAccount
     * @return String
     */
    private String getLoginOutUrl(String userAccount) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.WM_API_URL);
        url.append("cmd=").append("LogoutGame");
        url.append("&vendorId=").append(OpenAPIProperties.WM_VENDORID);
        url.append("&signature=").append(OpenAPIProperties.WM_SIGNATURE);
        url.append("&user=").append(userAccount);
        url.append("&timestamp=").append(getGMT8TimeLength10());
//        url.append("&syslang=").append(lang);
        return url.toString();
    }
    public static long getGMT8TimeLength10(){
        long epoch = 0;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINESE);
            Calendar day = Calendar.getInstance();
            day.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            day.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            day.set(Calendar.DATE, cal.get(Calendar.DATE));
            day.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            day.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            day.set(Calendar.SECOND, cal.get(Calendar.SECOND));
            Date gmt8 = day.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String t = df.format(gmt8);
            epoch = (df.parse(t).getTime()-30000 )/ 1000;
        } catch (Exception e) {
            System.out.println("获取GMT8时间 getGMT8Time() error !");
            e.printStackTrace();
        }
        return  epoch;
    }

    public Result errorCode(String errorCode, String errorMessage,String countryCode) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        104 新增会员资料错误,此帐号已被使用!!
            case "104":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
//        10404 帐号长度过长
            case "10404":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));

//        10405 帐号长度过短
            case "10405":
                return Result.failed("g100002", MessageUtils.get("g100002",countryCode));

//        10406 密码长度过短
            case "10406":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10407  密码长度过长
            case "10407":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10409 姓名长度过长
            case "10409":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
// 10502  帐号名不得为空
            case "10502":
                return Result.failed("g100003", MessageUtils.get("g100003",countryCode));
// 10508  密码不得为空
            case "10508":
                return Result.failed("g100108", MessageUtils.get("g100108",countryCode));
// 10509  姓名不得为空
            case "10509":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
// 10419  筹码格式错误(请用逗号隔开)
            case "10419":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10420  筹码个数错误(介于5-10个)
            case "10420":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10421  筹码种类错误
            case "10421":
                return Result.failed("g000007", MessageUtils.get("g000007",countryCode));
            // 10422  帐号只接受英文、数字、下划线与@
            case "10422":
                return Result.failed("g100106", MessageUtils.get("g100106",countryCode));
            // 10520  上层代理停用或停押
            case "10520":
                return Result.failed("g091035", MessageUtils.get("g091035",countryCode));
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", MessageUtils.get("g009999",countryCode));
        }
    }
}
