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
 * @Description: huaren代付服务类
 * @date 2021/9/5 15:57
 */
@Component
@Slf4j
public class HuarenWithdrawCallBackService {

    @Autowired
    private PayWithdrawConfigMapper payWithdrawConfigMapper;
    @Autowired
    private TakeCashMapper takeCashMapper;
    @Autowired
    private ITakeCashService iTakeCashService;

    public String withdrawCallBackProcess(HuaRenWithdrawCallbackReq callbackReq) {
        try {
            boolean checkFlag = checkCallBackParams(callbackReq);
            if (checkFlag) {
                PayCallBackDTO callBackDto = new PayCallBackDTO();
                callBackDto.setAmount(new BigDecimal(callbackReq.getTransferAmount()));
                callBackDto.setOrderNo(callbackReq.getMchOrderNo());
                callBackDto.setTransactionNo(callbackReq.getTransactionNo());
                boolean flag = iTakeCashService.withdrawSuccess(callBackDto);
                if (flag) {
                    return PayConstants.PAY_CALLBACK_BIG_SUCCESS;
                }
            }
        } catch (Exception e) {
            log.error("huaren 代付回调处理异常,req={} | e={}", JSON.toJSONString(callbackReq), e.getMessage());
        }
        return PayConstants.PAY_CALLBACK_FAIL;
    }


    public boolean checkCallBackParams(HuaRenWithdrawCallbackReq req) {
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayTakeCash> query = new QueryWrapper<>();
        query.lambda().eq(PayTakeCash::getOrderNo, req.getMerTransferId());
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
        metaSignMap.put("tradeResult", req.getTradeResult());
        metaSignMap.put("merTransferId", req.getMerTransferId());
        metaSignMap.put("merNo", req.getMerNo());
        metaSignMap.put("tradeNo", req.getTradeNo());
        metaSignMap.put("transferAmount", req.getTransferAmount());
        metaSignMap.put("applyDate", req.getApplyDate());
        metaSignMap.put("version", req.getVersion());
        metaSignMap.put("respCode", req.getRespCode());

        // 商户key
        String signStr = SignMd5Utils.createSmallSign(metaSignMap, payWithdrawConfig.getSecretKey());
        if (!signStr.equals(req.getSign())) {
            log.error("hr支付宝签名不在确=={}", signStr);
            return false;
        }
        if (GlobalConstants.PAY_CASH_STATUS_SUCCEED.equals(payTakeCash.getCashStatus())) {
            log.error("hr支付此单号已经处理=={}", req.getMerTransferId());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = payTakeCash.getActualAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(req.getTransferAmount()).compareTo(payAmt) != 0) {
            log.error("paylog hr代付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), req.getTransferAmount(), payAmt);
            return false;
        }
        return true;
    }

}
