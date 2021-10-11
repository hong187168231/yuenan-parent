package com.live.Pay.service;

import com.live.Pay.common.constant.PayConstants;
import com.live.Pay.factory.OnlinePaymentService;
import com.live.Pay.mapper.PayRechargeOrderMapper;
import com.live.common.utils.ViewUtil;
import com.live.pay.pojo.dto.PaymentVastDto;
import com.live.pay.pojo.entity.PayRechargeOrder;
import com.live.pay.pojo.resp.DiLeiCallbackReq;
import com.live.user.pojo.entity.MemBaseinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author puff
 * @Description: 支付回调业务类
 * @date 2021/9/5 15:57
 */
@Slf4j
@Component
public class PaymentCallBackService {

    @Resource
    private PayRechargeOrderMapper payRechargeOrderMapper;


    @Resource(name = "diLeiOnlinePaymentService")
    private OnlinePaymentService diLeiOnlinePaymentService;


    public String diLeiCallback(DiLeiCallbackReq req) {
        return commonCallback(diLeiOnlinePaymentService.callBackProcess(req), PayConstants.PAY_CALLBACK_FAIL, PayConstants.PAY_CALLBACK_BIG_OK);
    }

    public String commonCallback(boolean result, String msgFail, String msgSuccess) {
        String msg = msgFail;
        if (result) {
            msg = msgSuccess;
        }
        return msg;
    }

    /**
     * 公共回调订单处理成功
     */
    @Transactional
    public boolean paymentSuccess(PaymentVastDto paymentVastVo, PayRechargeOrder rechargeOrder) {
        log.info("进入回调数据成功处理========================================={}==", rechargeOrder.getOrderNo());
        // 【分布式读写锁】
        try {
            MemBaseinfo memBaseinfo = new MemBaseinfo();//todo
            double amount = rechargeOrder.getRealAmount().setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
            if (null != memBaseinfo) {
                //更新充值订单表信息
                rechargeOrder.setTotalAmount(ViewUtil.getTradeOffAmount(new BigDecimal(amount)));
                rechargeOrder.setTransactionNo(paymentVastVo.getTransactionNo());
                rechargeOrder.setOrderStatus(PayConstants.PAY_RECHARGE_STATUS_COMPLETE);
                boolean flag = payRechargeOrderMapper.updateById(rechargeOrder) > 0;
                if (!flag) {
                    throw new RuntimeException("修改订单为成功状态失败");
                }
                //更新会员等级 替换 updateMemLevel
                // TODO
//                MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
//                goldChangeDO.setChangeAmount(rechargeOrder.getRealAmount());
//                goldChangeDO.setTradingEnum(TradingEnum.INCOME);
//                String goldChangeCode = PayTypeToChangeEnum.getEnumByTypeCode(rechargeOrder.getTypeCode()).name();
//                goldChangeDO.setGoldchangeEnum(GoldchangeEnum.valueOf(goldChangeCode));
//                goldChangeDO.setUid(memBaseinfo.getUid());
//                goldChangeDO.setUpdateUser(memBaseinfo.getAcclogin());
//                goldChangeDO.setRefId(rechargeOrder.getRechargeOrderId());
//                boolean changeFlag = memGoldChangeBusiness.updateMemChangeInfo(goldChangeDO);
//                if (!changeFlag) {
//                    log.info("payDataOrder error parm{}", JSONObject.toJSONString(goldChangeDO));
//                    throw new RuntimeException("订单处理失败！");
//                }
            }
            log.info("进入回调数据成功处理结束========================================={}==", rechargeOrder.getOrderNo());
        } catch (TransactionSystemException e) {
            log.error("paymentSucesss occur error.", e);
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("paymentSucesss occur error.", e);
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }


}
