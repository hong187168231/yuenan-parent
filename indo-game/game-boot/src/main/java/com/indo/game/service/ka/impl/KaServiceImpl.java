package com.indo.game.service.ka.impl;

import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.dto.comm.ApiResponseData;
import com.indo.game.pojo.dto.comm.LoginGame;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.common.GameLogoutService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ka.KaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class KaServiceImpl implements KaService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private GameCommonService gameCommonService;
    @Autowired
    private GameLogoutService gameLogoutService;
    @Override
    public Result kaGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode) {
        logger.info("kalog {} kaGame account:{},kaCodeId:{}", loginUser.getId(), loginUser.getAccount(), platform);
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
//            logger.info("站点ka余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
            } else {
                cptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(cptOpenMember);
            }
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
            String lang = "";
            if(null!=countryCode&&!"".equals(countryCode)){
                switch (countryCode) {
                    case "IN":
                        lang = "en_US";
                        break;
                    case "EN":
                        lang = "en_US";
                        break;
                    case "CN":
                        lang = "zh_CN";
                        break;
                    case "VN":
                        lang = "vi_VN";
                        break;
                    case "TW":
                        lang = "zh_TW";
                        break;
                    case "TH":
                        lang = "th_TH";
                        break;
                    case "ID":
                        lang = "in_ID";
                        break;
                    case "MY":
                        lang = "ms_MY";
                        break;
                    case "KR":
                        lang = "ko_KR";
                        break;
                    case "JP":
                        lang = "ja_JP";
                        break;
                    default:
                        lang = gameParentPlatform.getLanguageType();
                        break;
                }
            }else{
                lang = gameParentPlatform.getLanguageType();
            }
            ApiResponseData responseData = new ApiResponseData();
            responseData.setPathUrl(getStartGameUrl(cptOpenMember, platform, gameParentPlatform.getCurrencyType(), lang));
            return Result.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
        }
    }

    @Override
    public Result logout(String account,String platform, String ip,String countryCode) {
        logger.info("kalogout   kaGame account:{},platform:{}", account, platform);
//        try {
//            PpApiRequestData ppApiRequestData = new PpApiRequestData();
//            ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
//            ppApiRequestData.setExternalPlayerId(loginUser.getAccount());
//
//            // 获取请求参数
//            Map<String, Object> params = getPostParams(ppApiRequestData);
//
//            // 退出游戏
//            PpCommonResp ppCommonResp = commonRequest(
//                    getLogOutPpPlayerUrl(), params,
//                    loginUser.getId(), "loginoutPP");
//
//            if (0 == ppCommonResp.getError()) {
//                return Result.success(ppCommonResp);
//            } else {
//                return errorCode(ppCommonResp.getError().toString(), ppCommonResp.getDescription());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failed("g100104", MessageUtils.get("g100104",countryCode));
//        }
        return Result.success();
    }

    /**
     * 启动游戏URL
     * https://<KAGASERVER>/?g=<GAME_NAME>&p=<PARTNER_NAME>&u=<UNIQUE_PLAYERID>&t=<UNIQUE_TOKE
     * N>&ak=<PARTNER_ACCESSKEY>&loc=<PLAYER_LANGUAGE>&cr=<CURRENCY>&m=<GAME_MODE>&dn=<DE
     * NOMINATION>&o=<OPERATOR>&sn=<SOUND_OFF>&l=<LOBBY_URL>&sl=<SUPPORT_URL>&da=<DISPLAY_NA
     * ME>&ro=<REMOVE_OPTIONS>&asm=<AUTOSPIN_MODE>&if=<IFRAME_MODE>&mc=<BALANCE_METER_MODE
     * >&v=<VERTICAL_MODE>&tl=<TITLE>&ld=<LOW_DEFINITION>&dc=<DISABLE_EXIT_CONFIRMATION&db=<DIS
     * ABLE_INSUFFICIENT_BALANCE_ALERT>&sfp=<DISABLE_FULLSCREEN_PROMPT>
     *
     * @return
     */
    private String getStartGameUrl(CptOpenMember cptOpenMember, String platform, String currency, String lang) {
        StringBuilder url = new StringBuilder();
        url.append(OpenAPIProperties.KA_GAME_URL);
        url.append("/?g=").append(platform);
        url.append("&p=").append(OpenAPIProperties.KA_PARTNER_NAME);
        url.append("&u=").append(cptOpenMember.getUserName());
        url.append("&t=").append(cptOpenMember.getPassword());
        url.append("&ak=").append(OpenAPIProperties.KA_ACCESS_KEY);
        url.append("&cr=").append(currency);
        if (StringUtils.isNotEmpty(lang)) {
            url.append("&loc=").append(lang);
        }
        return url.toString();
    }

}
