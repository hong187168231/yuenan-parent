package com.indo.game.service.pg.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.encrypt.Base64;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.vo.callback.ae.AeCallBackRespFail;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceResp;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceRespSuccess;
import com.indo.game.pojo.vo.callback.pg.PgCallBackResponse;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.pg.PgCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import javax.annotation.Resource;


/**
 * AE电子
 *
 * @author
 */
@Service
public class PgCallbackServiceImpl implements PgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object pgBalanceCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode("PG");
        //进行秘钥
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(pgVerifyCallBackReq.getPlayer_name());
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();
        if (null == memBaseinfo) {
            pgCallBackRespFail.setData(dataJson.toJSONString());
            errorJson.put("code", "1034");
            errorJson.put("message", "无效请求");
            pgCallBackRespFail.setError(errorJson.toJSONString());
            return JSONObject.toJSONString(pgCallBackRespFail);
        } else {
            long currentTime = System.currentTimeMillis();
            dataJson.put("updated_time", currentTime);
            dataJson.put("balance_amount", memBaseinfo.getBalance());
            dataJson.put("currency_code", platformGameParent.getCurrencyType());
            pgCallBackRespFail.setData(dataJson.toJSONString());
            pgCallBackRespFail.setError(errorJson.toJSONString());
            return JSONObject.toJSONString(pgCallBackRespFail);
        }
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode("PG");
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

    @Override
    public Object pgVerifyCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(pgVerifyCallBackReq.getOperator_player_session(), "PG");
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode("PG");
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        JSONObject errorJson = new JSONObject();
        if (cptOpenMember == null) {
            pgCallBackRespFail.setData(dataJson.toJSONString());
            errorJson.put("code", "1034");
            errorJson.put("message", "无效请求");
            pgCallBackRespFail.setError(errorJson.toJSONString());
            return JSONObject.toJSONString(pgCallBackRespFail);
        }
        dataJson.put("player_name", cptOpenMember.getUserName());
        dataJson.put("nickname", cptOpenMember.getUserName());
        dataJson.put("currency", platformGameParent.getCurrencyType());
        pgCallBackRespFail.setData(dataJson.toJSONString());
        pgCallBackRespFail.setError(errorJson.toJSONString());
        return JSONObject.toJSONString(pgCallBackRespFail);
    }
}

