package com.indo.game.service.ug.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.utils.GameUtil;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.pojo.dto.ug.UgLoginJsonDTO;
import com.indo.game.pojo.dto.ug.UgLogoutJsonDTO;
import com.indo.game.pojo.dto.ug.UgRegisterPlayerJsonDTO;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.ug.UgApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ug.UgService;
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
public class UgServiceImpl implements UgService {

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
     * @return loginUser 用户信息
     */
    @Override
    public Result ugGame(LoginInfo loginUser, String ip,String platform,String WebType) {
        logger.info("uglog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName());
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
        if(null==gamePlatform){
            return Result.failed("(ug)"+MessageUtils.get("tgdne"));
        }
        if ("0".equals(gamePlatform.getIsStart())) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点ug余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }

        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), gamePlatform.getParentName());
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(gamePlatform.getParentName());
                //创建玩家
                return restrictedPlayer(loginUser,gamePlatform, ip, cptOpenMember,WebType);
            } else {
                Result result = this.logout(loginUser,ip);
                if(null!=result&&"00000".equals(result.getCode())) {
                    CptOpenMember updateCptOpenMember = new CptOpenMember();
                    updateCptOpenMember.setId(cptOpenMember.getId());
                    updateCptOpenMember.setLoginTime(new Date());
                    externalService.updateCptOpenMember(updateCptOpenMember);
                    //登录
                    return initGame(loginUser, gamePlatform, ip, WebType);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("tnibptal"));
        }
    }

    /**
     * 注册会员
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(LoginInfo loginUser,GamePlatform gamePlatform, String ip,CptOpenMember cptOpenMember,String WebType) {
        logger.info("uglog {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            UgRegisterPlayerJsonDTO ugRegisterPlayerJsonDTO = new UgRegisterPlayerJsonDTO();
            ugRegisterPlayerJsonDTO.setMemberAccount(loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            ugRegisterPlayerJsonDTO.setNickName(null==loginUser.getNickName()?"":loginUser.getNickName());//昵称,长度需要小于 50
            ugRegisterPlayerJsonDTO.setCurrency(gamePlatform.getCurrencyType());//货币代码
            String url = "";
            if(null!=OpenAPIProperties.UG_AGENT&&!"".equals(OpenAPIProperties.UG_AGENT)){
                ugRegisterPlayerJsonDTO.setAgentID(OpenAPIProperties.UG_AGENT);//代理编号
                url = OpenAPIProperties.UG_API_URL+"/SportApi/RegisterByAgent";

            }else {
                url = OpenAPIProperties.UG_API_URL+"/SportApi/Register";
            }

            UgApiResponseData ugApiResponse = commonRequest(ugRegisterPlayerJsonDTO, url, loginUser.getId().intValue(), ip, "restrictedPlayer");

            if (null == ugApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("000000".equals(ugApiResponse.getErrorCode())){
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(loginUser,gamePlatform, ip,WebType);
            }else {
                return Result.failed(ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("uglog game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(LoginInfo loginUser,GamePlatform gamePlatform, String ip,String WebType) throws Exception {
        logger.info("uglog {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            UgLoginJsonDTO ugLoginJsonDTO = new UgLoginJsonDTO();
            ugLoginJsonDTO.setMemberAccount(loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            ugLoginJsonDTO.setWebType(WebType);//登录类型: PC \Smart \Wap；默认值：PC
            ugLoginJsonDTO.setLoginIP(OpenAPIProperties.PROXY_HOST_NAME);//登录 IP
            ugLoginJsonDTO.setLanguage(gamePlatform.getLanguageType());// string 否 语言文字代码；默认值：EN
//            ugLoginJsonDTO.setPageStyle("");// string 否 网站版面 SP1, SP2, SP3, SP4, SP5,SP6,SP7；默认值：SP1
//            ugLoginJsonDTO.setOddsStyle("");// string 否 赔率样式代码(OddsStyle) ；默认值：MY
//            ugLoginJsonDTO.setHostUrl("");// string 否 对接商域名,如果有 cname 指向我们域名的可以传入该参数，指向的域名询问我们客服
//            ugLoginJsonDTO.setPUrl("");// string 否 对接商的首页的链接，仅 Smart 版有效
//            ugLoginJsonDTO.setBalance("");// decimal 否 用于登录后的余额展示，仅 H5 版有效
//            ugLoginJsonDTO.setCashBalance("");// decimal 否 用于登录后的现金余额展示，仅 H5 版有效

            UgApiResponseData ugApiResponse = commonRequest(ugLoginJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/Login", loginUser.getId().intValue(), ip, "Login");
            if("000000".equals(ugApiResponse.getErrorCode())){
                return Result.success(ugApiResponse);
            }else {
                return Result.failed(ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            logger.error("uglog game error {} ", e);
            return null;
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip){
        UgLogoutJsonDTO ugLogoutJsonDTO = new UgLogoutJsonDTO();
        ugLogoutJsonDTO.setMemberAccount(loginUser.getAccount());

        UgApiResponseData ugApiResponse = null;
        try {
            ugApiResponse = commonRequest(ugLogoutJsonDTO, OpenAPIProperties.UG_API_URL+"/SportApi/Logout", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            if("000000".equals(ugApiResponse.getErrorCode())){
                return Result.success(ugApiResponse);
            }else {
                return Result.failed(ugApiResponse.getErrorCode(),ugApiResponse.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("tnibptal"));
        }

    }

    /**
     * 公共请求
     */
    public UgApiResponseData commonRequest(Object object, String url, Integer userId, String ip, String type) throws Exception {

        UgApiResponseData ugApiResponse = null;
        logger.info("uglog {} commonRequest ,url:{},paramsMap:{}", userId, url, object);

        logger.info("ug_api_request:"+JSONObject.toJSONString(object));
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP,url, JSONObject.toJSONString(object), type, userId);
        logger.info("ug_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            ugApiResponse = JSONObject.parseObject(resultString, UgApiResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("uglog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, ugApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, JSONObject.toJSONString(object), resultString, JSONObject.toJSONString(ugApiResponse));
        }
        return ugApiResponse;
    }

}
