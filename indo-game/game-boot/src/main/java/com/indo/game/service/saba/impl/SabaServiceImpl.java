package com.indo.game.service.saba.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.saba.SabaApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.saba.SabaService;

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
 * awc ae真人 游戏业务类
 *
 * @author eric
 */
@Service
public class SabaServiceImpl implements SabaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    GameTypeMapper gameTypeMapper;
    @Autowired
    GameCategoryMapper gameCategoryMapper;
    @Autowired
    private GamePlatformMapper gamePlatformMapper;

    /**
     * 登录游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result sabaGame(LoginInfo loginUser, String ip, String platform, String parentName,String isMobileLogin) {
        logger.info("saba体育log  sabaGame loginUser:{}, ip:{}, platform:{}, parentName:{}", loginUser,ip,platform,parentName);
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
        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点saba余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(parentName);
                //创建玩家
                return restrictedPlayer(gameParentPlatform, loginUser, gamePlatform, ip, cptOpenMember, isMobileLogin);
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
                //登出
                this.logout(loginUser,ip);
                //登录
                return initGame(gameParentPlatform, loginUser, gamePlatform, ip, isMobileLogin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }
    }

    /**
     * 注册会员
     *
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(GameParentPlatform gameParentPlatform, LoginInfo loginUser, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember,String isMobileLogin) {
        logger.info("sabalog {} sabaGame account:{}, sabaCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("vendor_Member_ID", loginUser.getAccount());//厂商会员识别码（建议跟 Username 一样）, 支援 ASCII Table 33-126, 最大长度 = 30
            trr.put("username", loginUser.getAccount());
            trr.put("oddsType", "3");//为此会员设置赔率类型。请参考附件"赔率类型表"
            trr.put("currency", gameParentPlatform.getCurrencyType());//为此会员设置币别。请参考附件中"币别表"
            trr.put("maxTransfer", null!=gamePlatform.getMaxTransfer()?gamePlatform.getMaxTransfer().toPlainString():"");//于 Sportsbook 系统与厂商间的最大限制转帐金额
            trr.put("minTransfer", null!=gamePlatform.getMinTransfer()?gamePlatform.getMinTransfer().toPlainString():"");//于 Sportsbook 系统与厂商间的最小限制转帐金额
            logger.info("saba体育log  注册会员restrictedPlayer输入 loginUser:{}, ip:{}, params:{}, urlapi:{}", loginUser,ip,trr,OpenAPIProperties.SABA_API_URL + "/CreateMember");
            SabaApiResponseData sabaApiResponse = commonRequest(trr, OpenAPIProperties.SABA_API_URL + "/CreateMember", loginUser.getId().intValue(), ip, "restrictedPlayer");
            logger.info("saba体育log  注册会员restrictedPlayer返回 sabaApiResponse:{}", JSONObject.toJSONString(sabaApiResponse));
            if (null == sabaApiResponse) {
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
            if ("0".equals(sabaApiResponse.getError_code()) || "4103".equals(sabaApiResponse.getError_code())) {
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(gameParentPlatform, loginUser, gamePlatform, ip,isMobileLogin);
            } else {
                return errorCode(sabaApiResponse.getError_code(), sabaApiResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("sabalog game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(GameParentPlatform gameParentPlatform, LoginInfo loginUser, GamePlatform gamePlatform, String ip,String isMobileLogin) throws Exception {
        logger.info("sabalog {} sabaGame account:{}, sabaCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("vendor_member_id", loginUser.getAccount());
            trr.put("platform", isMobileLogin);  //投注平台. 输入要登录的平台码。 1:桌机, 2:手机 h5, 3:手机纯文字.
            logger.info("saba体育log  登录initGame输入 loginUser:{}, ip:{}, params:{}, urlapi:{}", loginUser,ip,trr,OpenAPIProperties.SABA_API_URL + "/GetSabaUrl");
            SabaApiResponseData sabaApiResponse = commonRequest(trr, OpenAPIProperties.SABA_API_URL + "/GetSabaUrl", loginUser.getId().intValue(), ip, "restrictedPlayer");
            logger.info("saba体育log  登录initGame返回 sabaApiResponse:{}", JSONObject.toJSONString(sabaApiResponse));
            if (null == sabaApiResponse) {
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
            if ("0".equals(sabaApiResponse.getError_code())) {
                ApiResponseData responseData = new ApiResponseData();
                JSONObject jsonObject = JSON.parseObject(sabaApiResponse.getData());
                responseData.setPathUrl(jsonObject.getString("gameUrl"));
                return Result.success(responseData);
            } else {
                return errorCode(sabaApiResponse.getError_code(), sabaApiResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("sabalog game error {} ", e);
            return null;
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String ip) {
        Map<String, String> trr = new HashMap<>();
        trr.put("vendor_member_id ", loginUser.getAccount());

        SabaApiResponseData sabaApiResponse = null;
        try {
            logger.info("saba体育log  登出玩家logout输入 loginUser:{}, ip:{}, params:{}, urlapi:{}", loginUser,ip,trr,OpenAPIProperties.SABA_API_URL + "/KickUser");
            sabaApiResponse = commonRequest(trr, OpenAPIProperties.SABA_API_URL + "/KickUser", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            logger.info("saba体育log  登出玩家logout返回 sabaApiResponse:{}", JSONObject.toJSONString(sabaApiResponse));
            if (null == sabaApiResponse) {
                return Result.failed("g100104", "网络繁忙，请稍后重试！");
            }
            if ("0".equals(sabaApiResponse.getError_code())) {
                return Result.success(sabaApiResponse);
            } else {
                return Result.failed(sabaApiResponse.getError_code(), sabaApiResponse.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", "网络繁忙，请稍后重试！");
        }

    }

    /**
     * 公共请求
     */
    public SabaApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {

        SabaApiResponseData sabaApiResponse = null;
        paramsMap.put("vendor_id", OpenAPIProperties.SABA_VENDORID);
        paramsMap.put("operatorId", OpenAPIProperties.SABA_SITENAME);
        JSONObject sortParams = GameUtil.sortMap(paramsMap);
        Map<String, String> trr = new HashMap<>();
        trr.put("param", sortParams.toString());
        logger.info("公共请求commonRequest ug_api_request:" + sortParams);
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, url, paramsMap, type, userId);
        if (StringUtils.isNotEmpty(resultString)) {
            sabaApiResponse = JSONObject.parseObject(resultString, SabaApiResponseData.class);
        }
        return sabaApiResponse;
    }


    public Result errorCode(String errorCode, String errorMessage) {
        if ("1".equals(errorCode)) {
            return Result.failed("g009999", errorMessage);
        } else if ("2".equals(errorCode)) {
            return Result.failed("g100003", errorMessage);
        } else if ("3".equals(errorCode)) {
            return Result.failed("g091147", errorMessage);
        } else if ("4".equals(errorCode)) {
            return Result.failed("g091148", errorMessage);
        } else if ("5".equals(errorCode)) {
            return Result.failed("g100005", errorMessage);
        } else if ("6".equals(errorCode)) {
            return Result.failed("g091149", errorMessage);
        } else if ("7".equals(errorCode)) {
            return Result.failed("g091150", errorMessage);
        } else if ("8".equals(errorCode)) {
            return Result.failed("g091151", errorMessage);
        } else if ("9".equals(errorCode)) {
            return Result.failed("g091152", errorMessage);
        } else if ("10".equals(errorCode)) {
            return Result.failed("g000001", errorMessage);
        } else if ("12".equals(errorCode)) {
            return Result.failed("g091153", errorMessage);
        } else if ("13".equals(errorCode)) {
            return Result.failed("g091154", errorMessage);
        } else {
            return Result.failed("g009999", errorMessage);
        }
    }

}
