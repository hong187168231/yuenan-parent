package com.indo.pay.controller.callback;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.web.exception.BizException;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
import com.indo.pay.pojo.resp.withdraw.EasyWithdrawCallbackReq;
import com.indo.pay.pojo.resp.withdraw.HuaRenWithdrawCallbackReq;
import com.indo.pay.service.proxyWithdraw.EasyWithdrawCallBackService;
import com.indo.pay.service.proxyWithdraw.HuarenWithdrawCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 代付回调
 *
 * @author puff
 */
@Slf4j
@RestController
@RequestMapping("/withdraw")
public class WithdrawCallBackController {

    @Resource
    private HuarenWithdrawCallBackService huarenWithdrawCallBackService;
    @Resource
    private EasyWithdrawCallBackService easyWithdrawCallBackService;

    @AllowAccess
    @RequestMapping("/huaRenCallback")
    public String dileiCallback(HttpServletRequest request) {
        log.info("进入huaren支付回调接口==============================");

        String result = "";
        HuaRenCallbackReq huaRenCallbackReq = new HuaRenCallbackReq();
        try {
            // 订单状态 1：代付成功 2：代付失败
            String tradeResult = request.getParameter("tradeResult");
            // 商家转账单号
            String merTransferId = request.getParameter("merTransferId");
            // 商户代码
            String merNo = request.getParameter("merNo");
            // 平台订单号
            String tradeNo = request.getParameter("tradeNo");
            // 代付金额
            String transferAmount = request.getParameter("transferAmount");
            // 订单时间
            String applyDate = request.getParameter("applyDate");
            // 版本号
            String version = request.getParameter("version");
            // 回调状态 默认SUCCESS
            String respCode = request.getParameter("respCode");
            // 签名
            String sign = request.getParameter("sign");
            // 签名方式
            String signType = request.getParameter("signType");

            HuaRenWithdrawCallbackReq withdrawCallbackReq = new HuaRenWithdrawCallbackReq();
            withdrawCallbackReq.setTradeResult(tradeResult);
            withdrawCallbackReq.setMerTransferId(merTransferId);
            withdrawCallbackReq.setMerNo(merNo);
            withdrawCallbackReq.setTradeNo(tradeNo);
            withdrawCallbackReq.setTransferAmount(transferAmount);
            withdrawCallbackReq.setApplyDate(applyDate);
            withdrawCallbackReq.setVersion(version);
            withdrawCallbackReq.setRespCode(respCode);
            withdrawCallbackReq.setSign(sign);
            withdrawCallbackReq.setSignType(signType);

            result = huarenWithdrawCallBackService.withdrawCallBackProcess(withdrawCallbackReq);
        } catch (BizException e) {
            log.error("{}.huaRenCallback 失败:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(huaRenCallbackReq), e);
        } catch (Exception e) {
            log.error("{}.huaRenCallback 出错:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(huaRenCallbackReq), e);
        }
        return result;

    }


    @AllowAccess
    @RequestMapping("/easyCallback")
    public String easyCallback(@RequestBody EasyWithdrawCallbackReq callbackReq) {
        String result = "";
        try {
            log.info("进入easy代付回调接口==============================" + JSONObject.toJSONString(callbackReq));
            result = easyWithdrawCallBackService.withdrawCallBackProcess(callbackReq);
        } catch (BizException e) {
            log.error("{}.easyCallback 失败:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(callbackReq), e);
        } catch (Exception e) {
            log.error("{}.easyCallback 出错:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(callbackReq), e);
        }
        return result;

    }


}
