package com.indo.game.service.ug.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.AWCUtil;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.GameCategoryMapper;
import com.indo.game.mapper.GamePlatformMapper;
import com.indo.game.mapper.GameTypeMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.ug.UgApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ug.UgService;
import com.indo.user.pojo.entity.MemBaseinfo;
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
    public Result ugGame(LoginInfo loginUser, String ip,String platform) {
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
        MemBaseinfo memBaseinfo = gameCommonService.getMemBaseInfo(loginUser.getId().toString());
        BigDecimal balance = memBaseinfo.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点ug余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
            //站点棋牌余额不足
            return Result.failed(MessageUtils.get("tcgqifpccs"));
        }

        try {

            // 验证且绑定（KY-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(loginUser.getId().intValue(), platform);
            if (cptOpenMember == null) {
                cptOpenMember = new CptOpenMember();
                cptOpenMember.setUserId(loginUser.getId().intValue());
                cptOpenMember.setUserName(loginUser.getAccount());
                cptOpenMember.setPassword(loginUser.getAccount());
                cptOpenMember.setCreateTime(new Date());
                cptOpenMember.setLoginTime(new Date());
                cptOpenMember.setType(platform);
                //创建玩家
                return restrictedPlayer(loginUser,gamePlatform, ip, cptOpenMember);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
                //登录
                return initGame(loginUser,gamePlatform, ip);
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
    public Result restrictedPlayer(LoginInfo loginUser,GamePlatform gamePlatform, String ip,CptOpenMember cptOpenMember) {
        logger.info("uglog {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("MemberAccount", loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            trr.put("NickName", loginUser.getNickName());//昵称,长度需要小于 50
            trr.put("Currency", gamePlatform.getCurrencyType());//货币代码
            String url = "";
            if(null!=OpenAPIProperties.UG_AGENT&&!"".equals(OpenAPIProperties.UG_AGENT)){
                trr.put("AgentID", gamePlatform.getPlatformCode());//代理编号
                url = OpenAPIProperties.UG_API_URL+"/SportApi/Register";
            }else {
                url = OpenAPIProperties.UG_API_URL+"/SportApi/RegisterByAgent";
            }

            UgApiResponseData ugApiResponse = commonRequest(trr, url, loginUser.getId().intValue(), ip, "restrictedPlayer");

            if (null == ugApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("000000".equals(ugApiResponse.getErrorCode())){
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(loginUser,gamePlatform, ip);
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
    private Result initGame(LoginInfo loginUser,GamePlatform gamePlatform, String ip) throws Exception {
        logger.info("uglog {} ugGame account:{}, ugCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("MemberAccount", loginUser.getAccount());//账户名,长度需要小于 20,大小写不敏感
            trr.put("WebType", loginUser.getNickName());//登录类型: PC \Smart \Wap；默认值：PC
            trr.put("LoginIP", ip);//登录 IP
            trr.put("Language", gamePlatform.getLanguageType());// string 否 语言文字代码；默认值：EN
            trr.put("PageStyle", "");// string 否 网站版面 SP1, SP2, SP3, SP4, SP5,SP6,SP7；默认值：SP1
            trr.put("OddsStyle", "");// string 否 赔率样式代码(OddsStyle) ；默认值：MY
            trr.put("HostUrl", "");// string 否 对接商域名,如果有 cname 指向我们域名的可以传入该参数，指向的域名询问我们客服
            trr.put("PUrl", "");// string 否 对接商的首页的链接，仅 Smart 版有效
            trr.put("Balance", "");// decimal 否 用于登录后的余额展示，仅 H5 版有效
            trr.put("CashBalance", "");// decimal 否 用于登录后的现金余额展示，仅 H5 版有效

            UgApiResponseData ugApiResponse = commonRequest(trr, OpenAPIProperties.UG_API_URL+"/SportApi/Login", loginUser.getId().intValue(), ip, "Login");
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
     * 公共请求
     */
    @Override
    public UgApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("uglog {} commonRequest ,url:{},paramsMap:{}", userId, url, paramsMap);

        UgApiResponseData ugApiResponse = null;
        paramsMap.put("CompanyKey", OpenAPIProperties.UG_KEY);
        paramsMap.put("APIPassword", OpenAPIProperties.UG_API_PASSWORD);
        JSONObject sortParams = AWCUtil.sortMap(paramsMap);
        String resultString = AWCUtil.doProxyPostJson(url, paramsMap, type, userId);
        logger.info("ug_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            ugApiResponse = JSONObject.parseObject(resultString, UgApiResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("uglog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, ugApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, sortParams.toString(), resultString, JSONObject.toJSONString(ugApiResponse));
        }
        return ugApiResponse;
    }

}
