package com.indo.game.service.wm.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.GameUtil;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.wm.WmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WmServiceImpl implements WmService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CptOpenMemberService externalService;
    @Resource
    private GameCommonService gameCommonService;


    @Override
    public Result wmGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName) {
        logger.info("wmlog {} wmGame account:{},wmCodeId:{}", parentName, loginUser.getAccount(), platform);
        // 是否开售校验
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(parentName);
        if (null == gameParentPlatform) {
            return Result.failed("(" + parentName + ")游戏平台不存在");
        }
        if (gameParentPlatform.getIsStart().equals(0)) {
            return Result.failed("g100101", "游戏平台未启用");
        }
        if ("1".equals(gameParentPlatform.getIsOpenMaintenance())) {
            return Result.failed("g000001", gameParentPlatform.getMaintenanceContent());
        }
        if (!platform.equals(parentName)) {
            GamePlatform gamePlatform;
            // 是否开售校验
            gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
            if (null == gamePlatform) {
                return Result.failed("(" + platform + ")平台游戏不存在");
            }
            if (gamePlatform.getIsStart().equals(0)) {
                return Result.failed("g100102", "游戏未启用");
            }
            if ("1".equals(gamePlatform.getIsOpenMaintenance())) {
                return Result.failed("g091047", gamePlatform.getMaintenanceContent());
            }
        }

        BigDecimal balance = loginUser.getBalance();
        //验证站点余额
        if (null == balance || balance.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("站点wm余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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

                // 第一次登录自动创建玩家, 后续登录返回登录游戏URL
                return createMemberGame(cptOpenMember, gameParentPlatform, platform);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
                // 先退出
                commonRequest(getLoginOutUrl(loginUser.getAccount()), null);

                JSONObject jsonObject = getStartGame(cptOpenMember, gameParentPlatform.getLanguageType(), platform);
                if (jsonObject.getInteger("errorCode").equals(0)) {
                    // 请求URL
                    ApiResponseData responseData = new ApiResponseData();
                    responseData.setPathUrl(jsonObject.getString("result"));
                    return Result.success(responseData);
                } else {
                    return errorCode(jsonObject.getString("errorCode"), jsonObject.getString("errorMessage"));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    @Override
    public Result logout(LoginInfo loginUser, String platform, String ip) {
        logger.info("wmlogout {} wmGame account:{},wmCodeId:{}", ip, loginUser.getAccount(), platform);
        try {
            // 游戏退出
            JSONObject result = commonRequest(getLoginOutUrl(loginUser.getAccount()), null);
            if (null == result) {
                return Result.failed("g091087", "第三方请求异常！");
            }

            if (0 == result.getInteger("errorCode")) {
                return Result.success();
            } else {
                return errorCode(result.getString("errorCode"), result.getString("errorMessage"));
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
    private Result createMemberGame(CptOpenMember cptOpenMember, GameParentPlatform gameParentPlatform, String platform) {

        // 用户注册
        JSONObject result = commonRequest(getLoginUrl(cptOpenMember, gameParentPlatform.getLanguageType()), null);
        if (null == result) {
            return Result.failed("g091087", "第三方请求异常！");
        }

        if (0 == result.getInteger("errorCode")) {
            externalService.saveCptOpenMember(cptOpenMember);
            // 启动游戏
            JSONObject jsonObject = getStartGame(cptOpenMember, gameParentPlatform.getLanguageType(), platform);
            if (null == jsonObject) {
                return Result.failed("g091087", "第三方请求异常！");
            }
            if (jsonObject.getInteger("errorCode").equals(0)) {
                // 请求URL
                ApiResponseData responseData = new ApiResponseData();
                responseData.setPathUrl(jsonObject.getString("result"));
                return Result.success(responseData);
            } else {
                return errorCode(jsonObject.getString("errorCode"), jsonObject.getString("errorMessage"));
            }
        } else {
            return errorCode(result.getString("errorCode"), result.getString("errorMessage"));
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
        url.append("&mode=").append(cptOpenMember.getUserName());
//        url.append("&voice=").append(cptOpenMember.getUserName());
        url.append("&timestamp=").append(DateUtils.getUTC8TimeLength10());

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
        url.append("&timestamp=").append(DateUtils.getUTC8TimeLength10());
//        url.append("&syslang=").append(lang);
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
        url.append("&timestamp=").append(DateUtils.getUTC8TimeLength10());
//        url.append("&syslang=").append(lang);
        return url.toString();
    }


    public Result errorCode(String errorCode, String errorMessage) {
//        0 成功。                                                Succeed.
        switch (errorCode) {
//        104 新增会员资料错误,此帐号已被使用!!
            case "104":
                return Result.failed("g100003", errorMessage);
//        10404 帐号长度过长
            case "10404":
                return Result.failed("g100002", errorMessage);

//        10405 帐号长度过短
            case "10405":
                return Result.failed("g100002", errorMessage);

//        10406 密码长度过短
            case "10406":
                return Result.failed("g100108", errorMessage);
// 10407  密码长度过长
            case "10407":
                return Result.failed("g100108", errorMessage);
// 10409 姓名长度过长
            case "10409":
                return Result.failed("g000007", errorMessage);
// 10502  帐号名不得为空
            case "10502":
                return Result.failed("g100003", errorMessage);
// 10508  密码不得为空
            case "10508":
                return Result.failed("g100108", errorMessage);
// 10509  姓名不得为空
            case "10509":
                return Result.failed("g000007", errorMessage);
// 10419  筹码格式错误(请用逗号隔开)
            case "10419":
                return Result.failed("g000007", errorMessage);
            // 10420  筹码个数错误(介于5-10个)
            case "10420":
                return Result.failed("g000007", errorMessage);
            // 10421  筹码种类错误
            case "10421":
                return Result.failed("g000007", errorMessage);
            // 10422  帐号只接受英文、数字、下划线与@
            case "10422":
                return Result.failed("g100106", errorMessage);
            // 10520  上层代理停用或停押
            case "10520":
                return Result.failed("g091035", errorMessage);
//        9999 失败。                                                Failed.
            default:
                return Result.failed("g009999", errorMessage);
        }
    }
}
