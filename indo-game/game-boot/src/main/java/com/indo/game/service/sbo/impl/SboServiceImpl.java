package com.indo.game.service.sbo.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.AWCUtil;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.sbo.SboApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.sbo.SboService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * awc ae真人 游戏业务类
 *
 * @author eric
 */
@Service
public class SboServiceImpl implements SboService {

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
    public Result sboGame(LoginInfo loginUser, String ip,String platform) {
        logger.info("sbolog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName());
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
        if(null==gamePlatform){
            return Result.failed("(sbo)"+MessageUtils.get("tgdne"));
        }
        if ("0".equals(gamePlatform.getIsStart())) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
        BigDecimal balance = memBaseinfo.getBalance();
        //验证站点棋牌余额
        if (null==balance || BigDecimal.ZERO==balance) {
            logger.info("站点sbo余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
        logger.info("sbolog {} sboGame account:{}, sboCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("username", loginUser.getAccount());
            trr.put("agent", OpenAPIProperties.SBO_AGENT);//代理
            trr.put("language", gamePlatform.getLanguageType());//语言

            SboApiResponseData sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/player/register-player.aspx", loginUser.getId().intValue(), ip, "restrictedPlayer");

            if (null == sboApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("0".equals(sboApiResponse.getError().getId())||"4103".equals(sboApiResponse.getError().getId())){
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(loginUser,gamePlatform, ip);
            }else {
                return Result.failed(sboApiResponse.getError().getId(),sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(LoginInfo loginUser,GamePlatform gamePlatform, String ip) throws Exception {
        logger.info("sbolog {} sboGame account:{}, sboCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("username", loginUser.getAccount());
            trr.put("portfolio", gamePlatform.getPlatformCode());

            SboApiResponseData sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/player/register-player.aspx", loginUser.getId().intValue(), ip, "restrictedPlayer");
            if("0".equals(sboApiResponse.getError().getId())){
                return Result.success(sboApiResponse);
            }else {
                return Result.failed(sboApiResponse.getError().getId(),sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip){
        Map<String, String> trr = new HashMap<>();
        trr.put("Username", loginUser.getAccount());

        SboApiResponseData sboApiResponse = null;
        try {
            sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/player/logout.aspx", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            if (null == sboApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("0".equals(sboApiResponse.getError().getId())){
                return Result.success(sboApiResponse);
            }else {
                return Result.failed(sboApiResponse.getError().getId(),sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("tnibptal"));
        }

    }

    /**
     * 公共请求
     */
    @Override
    public SboApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("sbolog {} commonRequest ,url:{},paramsMap:{}", userId, url, paramsMap);

        SboApiResponseData sboApiResponse = null;
        paramsMap.put("companyKey", OpenAPIProperties.SBO_KEY);
        paramsMap.put("serverId", SnowflakeIdWorker.createOrderSn());
        JSONObject sortParams = AWCUtil.sortMap(paramsMap);
        Map<String, String> trr = new HashMap<>();
        trr.put("param", sortParams.toString());
        String resultString = AWCUtil.doProxyPostJson(url, trr, type, userId);
        logger.info("sbo_api_response:"+resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            sboApiResponse = JSONObject.parseObject(resultString, SboApiResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("sbolog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, sboApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, sortParams.toString(), resultString, JSONObject.toJSONString(sboApiResponse));
        }
        return sboApiResponse;
    }

}
