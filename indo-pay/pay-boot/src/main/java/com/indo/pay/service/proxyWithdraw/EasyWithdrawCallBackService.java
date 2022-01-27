package com.indo.pay.service.proxyWithdraw;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.constant.GlobalConstants;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.core.pojo.entity.PayWithdrawConfig;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.mapper.PayWithdrawConfigMapper;
import com.indo.pay.mapper.TakeCashMapper;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.resp.withdraw.EasyWithdrawCallbackReq;
import com.indo.pay.pojo.resp.withdraw.HuaRenWithdrawCallbackReq;
import com.indo.pay.service.ITakeCashService;
import com.indo.pay.util.SignMd5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author puff
 * @Description: easy代付服务类
 * @date 2021/9/5 15:57
 */
@Component
@Slf4j
public class EasyWithdrawCallBackService {

    @Autowired
    private PayWithdrawConfigMapper payWithdrawConfigMapper;

    @Autowired
    private TakeCashMapper takeCashMapper;
    @Autowired
    private ITakeCashService iTakeCashService;


    public String withdrawCallBackProcess(EasyWithdrawCallbackReq withdrawCallbackReq) {
        try {
            boolean checkFlag = checkCallBackParams(withdrawCallbackReq);
            if (checkFlag) {
                PayCallBackDTO callBackDto = new PayCallBackDTO();
                callBackDto.setAmount(new BigDecimal(withdrawCallbackReq.getCoin()));
                callBackDto.setOrderNo(withdrawCallbackReq.getOutTradeNo());
                boolean flag = iTakeCashService.withdrawSuccess(callBackDto);
                if (flag) {
                    return PayConstants.PAY_CALLBACK_BIG_OK;
                }
            }
        } catch (Exception e) {
            log.error("支付回调处理异常,req={} | e={}", JSON.toJSONString(withdrawCallbackReq), e.getMessage());
        }
        return PayConstants.PAY_CALLBACK_FAIL;
    }


    public boolean checkCallBackParams(EasyWithdrawCallbackReq req) {
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayTakeCash> query = new QueryWrapper<>();
        query.lambda().eq(PayTakeCash::getOrderNo, req.getOutTradeNo());
        PayTakeCash payTakeCash = takeCashMapper.selectOne(query);
        if (ObjectUtils.isEmpty(payTakeCash)) {
            log.info("huaren代付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
        PayWithdrawConfig payWithdrawConfig = payWithdrawConfigMapper.selectById(payTakeCash.getPayWithdrawId());
        if (ObjectUtils.isEmpty(payWithdrawConfig)) {
            log.info("huaren代付参数验证异常,商户无,req={}", JSON.toJSONString(req));
            return false;
        }
        // 生成验签
        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("code", req.getCode() + "");
        metaSignMap.put("message", req.getMessage());
        metaSignMap.put("merchantId", req.getMerchantId());
        metaSignMap.put("outTradeNo", req.getOutTradeNo());
        metaSignMap.put("coin", req.getCoin());

        // 商户key
        String signStr = SignMd5Utils.createSign(metaSignMap, payWithdrawConfig.getSecretKey());
        if (!signStr.equals(req.getSign())) {
            log.error("easy 代付回调签名不在确=={}", signStr);
            return false;
        }
        if (GlobalConstants.PAY_CASH_STATUS_SUCCEED.equals(payTakeCash.getCashStatus())) {
            log.error("easy 代付此单号已经处理=={}", req.getMchOrderNo());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = payTakeCash.getActualAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(req.getCoin()).compareTo(payAmt) != 0) {
            log.error("paylog hr代付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), req.getCoin(), payAmt);
            return false;
        }
        return true;
    }

}
