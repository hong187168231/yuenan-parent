package com.indo.game.service.sbo.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.utils.GameUtil;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.mapper.frontend.GameTypeMapper;
import com.indo.game.mapper.sbo.GameAgentMapper;
import com.indo.game.pojo.dto.sbo.SboPlayerLoginJsonDTO;
import com.indo.game.pojo.dto.sbo.SboPlayerLogoutJsonDTO;
import com.indo.game.pojo.dto.sbo.SboRegisterPlayerJsonDTO;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.sbo.GameAgent;
import com.indo.game.pojo.vo.callback.sbo.SboApiResponseData;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.sbo.SboService;
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
 * @author
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
    @Autowired
    private GameAgentMapper gameAgentMapper;

    /**
     * 登录游戏
     *
     * @return loginUser 用户信息
     */
    @Override
    public Result sboGame(LoginInfo loginUser, String ip, String platform) {
        logger.info("sbolog {} aeGame account:{}, aeCodeId:{}", loginUser.getId(), loginUser.getNickName());
        // 是否开售校验
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
        if (null == gamePlatform) {
            return Result.failed("(sbo)" + MessageUtils.get("tgdne"));
        }
        if ("0".equals(gamePlatform.getIsStart())) {
            return Result.failed(MessageUtils.get("tgocinyo"));
        }
        //初次判断站点棋牌余额是否够该用户
//        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(loginUser.getAccount());
        BigDecimal balance = loginUser.getBalance();
        //验证站点棋牌余额
        if (null == balance || BigDecimal.ZERO == balance) {
            logger.info("站点sbo余额不足，当前用户memid {},nickName {},balance {}", loginUser.getId(), loginUser.getNickName(), balance);
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
                return restrictedPlayer(loginUser, gamePlatform, ip, cptOpenMember);
            } else {
                CptOpenMember updateCptOpenMember = new CptOpenMember();
                updateCptOpenMember.setId(cptOpenMember.getId());
                updateCptOpenMember.setLoginTime(new Date());
                externalService.updateCptOpenMember(updateCptOpenMember);
                //登录
                return initGame(loginUser, gamePlatform, ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("tnibptal"));
        }
    }

    /**
     * 注册会员
     *
     * @return loginUser 用户信息
     */
    public Result restrictedPlayer(LoginInfo loginUser, GamePlatform gamePlatform, String ip, CptOpenMember cptOpenMember) {
        logger.info("sbolog {} sboGame account:{}, sboCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            LambdaQueryWrapper<GameAgent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GameAgent::getParentName, gamePlatform.getParentName());
            GameAgent gameAgent = gameAgentMapper.selectOne(wrapper);
            if(null==gameAgent){
                return Result.failed(MessageUtils.get("praaa"));
            }

            SboRegisterPlayerJsonDTO sboRegisterPlayerJsonDTO = new SboRegisterPlayerJsonDTO();
            sboRegisterPlayerJsonDTO.setUsername(loginUser.getAccount());
            sboRegisterPlayerJsonDTO.setAgent(gameAgent.getUsername());//代理

            SboApiResponseData sboApiResponse = commonRequest(sboRegisterPlayerJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/register-player.aspx", loginUser.getId().intValue(), ip, "restrictedPlayer");

            if (null == sboApiResponse) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if ("0".equals(sboApiResponse.getError().getId()) || "4103".equals(sboApiResponse.getError().getId())) {
                externalService.saveCptOpenMember(cptOpenMember);
                return initGame(loginUser, gamePlatform, ip);
            } else {
                return Result.failed(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 登录
     */
    private Result initGame(LoginInfo loginUser, GamePlatform gamePlatform, String ip) throws Exception {
        logger.info("sbolog {} sboGame account:{}, sboCodeId:{}", loginUser.getId(), loginUser.getNickName());
        try {
            SboPlayerLoginJsonDTO sboPlayerLoginJsonDTO = new SboPlayerLoginJsonDTO();
            sboPlayerLoginJsonDTO.setUsername(loginUser.getAccount());
            sboPlayerLoginJsonDTO.setPortfolio(gamePlatform.getPlatformCode());
            sboPlayerLoginJsonDTO.setIsWapSports(false);

            SboApiResponseData sboApiResponse = commonRequest(sboPlayerLoginJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/login.aspx", loginUser.getId().intValue(), ip, "PlayerLogin");
            if ("0".equals(sboApiResponse.getError().getId())) {
                return Result.success(sboApiResponse);
            } else {
                return Result.failed(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            logger.error("sbolog game error {} ", e);
            return null;
        }
    }

    /**
     * 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String ip) {
        SboPlayerLogoutJsonDTO sboPlayerLogoutJsonDTO = new SboPlayerLogoutJsonDTO();
        sboPlayerLogoutJsonDTO.setUsername(loginUser.getAccount());

        SboApiResponseData sboApiResponse = null;
        try {
            sboApiResponse = commonRequest(sboPlayerLogoutJsonDTO, OpenAPIProperties.SBO_API_URL + "/web-root/restricted/player/logout.aspx", Integer.valueOf(loginUser.getId().intValue()), ip, "logout");
            if (null == sboApiResponse) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if ("0".equals(sboApiResponse.getError().getId())) {
                return Result.success(sboApiResponse);
            } else {
                return Result.failed(sboApiResponse.getError().getId(), sboApiResponse.getError().getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("tnibptal"));
        }

    }

    /**
     * 公共请求
     */
    public SboApiResponseData commonRequest(Object obj, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("sbolog {} commonRequest ,url:{},paramsMap:{}", userId, url, JSONObject.toJSONString(obj));

        SboApiResponseData sboApiResponse = null;
        String resultString = GameUtil.doProxyPostJson(OpenAPIProperties.PROXY_HOST_NAME, OpenAPIProperties.PROXY_PORT, OpenAPIProperties.PROXY_TCP, url, JSONObject.toJSONString(obj), type, userId);
        logger.info("sbo_api_response:" + resultString);
        if (StringUtils.isNotEmpty(resultString)) {
            sboApiResponse = JSONObject.parseObject(resultString, SboApiResponseData.class);
            //String operateFlag = (String) redisTemplate.opsForValue().get(Constants.AE_GAME_OPERATE_FLAG + userId);
            logger.info("sbolog {}:commonRequest type:{}, operateFlag:{}, url:{}, hostName:{}, params:{}, result:{}, sboApiResponse:{}",
                    //userId, type, operateFlag, url,
                    userId, type, null, url, JSONObject.toJSONString(obj), resultString, JSONObject.toJSONString(sboApiResponse));
        }
        return sboApiResponse;
    }

}
