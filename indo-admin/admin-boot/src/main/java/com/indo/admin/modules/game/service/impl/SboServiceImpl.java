package com.indo.admin.modules.game.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.admin.modules.game.mapper.GameAgentMapper;
import com.indo.admin.modules.game.service.SboService;
import com.indo.admin.pojo.dto.game.sbo.SboAgentDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentPresetBetDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentStatusDTO;
import com.indo.admin.pojo.entity.GameAgent;
import com.indo.admin.pojo.vo.game.sbo.SboApiResponseData;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.GameUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.util.JwtUtils;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GamePlatform;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
public class SboServiceImpl implements SboService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GameAgentMapper gameAgentMapper;

    /**
     * 注册代理
     * @return loginUser 用户信息
     */
    public Result registerAgent(SboAgentDTO sboAgentDTO,String ip) {
        try {
            LambdaQueryWrapper<GameAgent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GameAgent::getUsername,sboAgentDTO.getUsername());
            wrapper.eq(GameAgent::getParentName,"SBO");
            GameAgent gameAgent = gameAgentMapper.selectOne(wrapper);
            if(null!=gameAgent){
                return Result.failed("代理已经存在");
            }

            Map<String, String> trr = new HashMap<>();
            trr.put("Username", sboAgentDTO.getUsername());
            trr.put("Password", sboAgentDTO.getPassword());
            trr.put("Currency", sboAgentDTO.getCurrency());
            trr.put("Min", sboAgentDTO.getMin().toString());//*	Integer	该代理底下玩家的预设单笔注单最低限额。
            trr.put("Max", sboAgentDTO.getMax().toString());//*	Integer	该代理底下玩家的预设单笔注单最高限额。
            trr.put("MaxPerMatch", sboAgentDTO.getMaxPerMatch().toString());//*	Integer	该代理底下玩家的预设单场比赛最高限额。
            trr.put("CasinoTableLimit", sboAgentDTO.getCasinoTableLimit().toString());//*	Integer	该代理底下玩家的预设真人赌场限额设定。 1： 低 2：中 3：高 4：VIP

            SboApiResponseData sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/agent/register-agent.aspx", JwtUtils.getUserId().intValue(), ip, "registerAgent");

            if (null == sboApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("0".equals(sboApiResponse.getError().getId())){
                gameAgent = new GameAgent();
                BeanUtils.copyProperties(sboAgentDTO,gameAgent);
                gameAgent.setStatus("ACTIVE");
                gameAgent.setParentName("SBO");
                gameAgentMapper.insert(gameAgent);
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
     * 更新代理状态
     * @return loginUser 用户信息
     */
    public Result updateAgentStatus(SboUpdateAgentStatusDTO sboUpdateAgentStatusDTO,  String ip) {
        try {
            LambdaQueryWrapper<GameAgent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GameAgent::getUsername,sboUpdateAgentStatusDTO.getUsername());
            wrapper.eq(GameAgent::getParentName,"SBO");
            GameAgent gameAgent = gameAgentMapper.selectOne(wrapper);
            if(null==gameAgent){
                return Result.failed("代理不存在");
            }
//            更新代理状态至 CLOSED / SUSPEND / ACTIVE.
//            若更新状态为 CLOSED, 该代理底下的玩家将无法登入。 若更新状态为 SUSPEND, 该代理底下的玩家可以登入，但无法下注。 代理状态更新的影响会马上生效。
            Map<String, String> trr = new HashMap<>();
            trr.put("Username", sboUpdateAgentStatusDTO.getUsername());
            trr.put("Status", sboUpdateAgentStatusDTO.getStatus());

            SboApiResponseData sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/agent/update-agent-status.aspx", JwtUtils.getUserId().intValue(), ip, "updateAgentStatus");

            if (null == sboApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("0".equals(sboApiResponse.getError().getId())){
                gameAgent.setStatus(sboUpdateAgentStatusDTO.getStatus());
                gameAgentMapper.updateById(gameAgent);
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
     * 修改代理预设下注设定
     * @return loginUser 用户信息
     */
    public Result updateAgentPresetBet(SboUpdateAgentPresetBetDTO sboUpdateAgentPresetBetDTO,  String ip) {
        try {
            Map<String, String> trr = new HashMap<>();
            trr.put("Username", sboUpdateAgentPresetBetDTO.getUsername());
            trr.put("Min", sboUpdateAgentPresetBetDTO.getMin().toString());//*	Integer	该代理底下玩家的预设单笔注单最低限额。
            trr.put("Max", sboUpdateAgentPresetBetDTO.getMax().toString());//*	Integer	该代理底下玩家的预设单笔注单最高限额。
            trr.put("MaxPerMatch", sboUpdateAgentPresetBetDTO.getMaxPerMatch().toString());//*	Integer	该代理底下玩家的预设单场比赛最高限额。
            trr.put("CasinoTableLimit", sboUpdateAgentPresetBetDTO.getCasinoTableLimit().toString());//*	Integer	该代理底下玩家的预设真人赌场限额设定。 1： 低 2：中 3：高 4：VIP

            LambdaQueryWrapper<GameAgent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GameAgent::getUsername,sboUpdateAgentPresetBetDTO.getUsername());
            wrapper.eq(GameAgent::getParentName,"SBO");
            GameAgent gameAgent = gameAgentMapper.selectOne(wrapper);
            SboApiResponseData sboApiResponse = commonRequest(trr, OpenAPIProperties.SBO_API_URL+"/web-root/restricted/agent/update-agent-preset-bet-settings.aspx", JwtUtils.getUserId().intValue(), ip, "updateAgentPresetBet");

            if (null == sboApiResponse ) {
                return Result.failed(MessageUtils.get("etgptal"));
            }
            if("0".equals(sboApiResponse.getError().getId())){
                BeanUtils.copyProperties(sboUpdateAgentPresetBetDTO,gameAgent);
                gameAgentMapper.updateById(gameAgent);
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
     * 公共请求
     */
    @Override
    public SboApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception {
        logger.info("sbolog {} commonRequest ,url:{},paramsMap:{}", userId, url, paramsMap);

        SboApiResponseData sboApiResponse = null;
        paramsMap.put("CompanyKey", OpenAPIProperties.SBO_KEY);
        paramsMap.put("ServerId", OpenAPIProperties.SBO_SERVERID);
        JSONObject sortParams = GameUtil.sortMap(paramsMap);
        Map<String, String> trr = new HashMap<>();
        trr.put("param", sortParams.toString());
        String resultString = GameUtil.doProxyPostJson(url, trr, type, userId);
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
