package com.indo.game.controller.bl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.bl.BlCallBackReq;
import com.indo.game.service.bl.BlCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/bl/callBack")
public class BlCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlCallbackService blCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/player/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Object balance(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("blCallBack  getBalance 回调下注jsonObject:{},ip:{}", jsonObject,ip);
        BlCallBackReq blCallBackReq = new BlCallBackReq();
        blCallBackReq.setPlayer_account(jsonObject.getString("player_account"));
        blCallBackReq.setOperator_id(jsonObject.getString("operator_id"));
        blCallBackReq.setOperator_sub_id(jsonObject.getString("operator_sub_id"));
        blCallBackReq.setGame_code(jsonObject.getString("game_code"));
        blCallBackReq.setReport_id(jsonObject.getString("report_id"));
        blCallBackReq.setAmount(BigDecimal.valueOf(jsonObject.getDouble("amount")));
        blCallBackReq.setType(jsonObject.getInteger("type"));
        blCallBackReq.setTime(jsonObject.getString("time"));
        blCallBackReq.setApp_id(jsonObject.getString("app_id"));
        blCallBackReq.setCost_info(jsonObject.getString("cost_info"));
        blCallBackReq.setSha1(jsonObject.getString("sha1"));
        Object object = blCallbackService.blBlanceCallback(blCallBackReq, ip);
        logger.info("blCallBack getBalance 回调下注返回数据 取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 扣款接口
     */
    @RequestMapping(value = "/player/cost", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @AllowAccess
    @ResponseBody
    public Object player(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("blCallBack  player回调 params:{},ip{}", jsonObject,ip);
        BlCallBackReq blCallBackReq = new BlCallBackReq();
        blCallBackReq.setPlayer_account(jsonObject.getString("player_account"));
        blCallBackReq.setOperator_id(jsonObject.getString("operator_id"));
        blCallBackReq.setOperator_sub_id(jsonObject.getString("operator_sub_id"));
        blCallBackReq.setGame_code(jsonObject.getString("game_code"));
        blCallBackReq.setReport_id(jsonObject.getString("report_id"));
        blCallBackReq.setAmount(BigDecimal.valueOf(jsonObject.getDouble("amount")));
        blCallBackReq.setType(jsonObject.getInteger("type"));
        blCallBackReq.setTime(jsonObject.getString("time"));
        blCallBackReq.setApp_id(jsonObject.getString("app_id"));
        blCallBackReq.setCost_info(jsonObject.getString("cost_info"));
        blCallBackReq.setSha1(jsonObject.getString("sha1"));
        Object object = blCallbackService.blPlayerCallback(blCallBackReq, ip);
        logger.info("blCallBack  player回调返回数据  params:{}", object);
        return object;
    }

}
