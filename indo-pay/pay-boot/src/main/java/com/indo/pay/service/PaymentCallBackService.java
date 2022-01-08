package com.indo.pay.service;

import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.pay.mapper.PayOrderFirstMapper;
import com.indo.pay.mapper.PayRechargeOrderMapper;
import com.indo.pay.pojo.entity.PayOrderFirst;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import com.indo.pay.common.constant.PayConstants;
import com.indo.common.utils.ViewUtil;
import com.indo.pay.pojo.dto.RechargeCallBackDto;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
import com.indo.user.pojo.entity.MemBaseinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

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

    @Resource
    private PayOrderFirstMapper payOrderFirstMapper;


    @Resource(name = "huaRenOnlinePaymentService")
    private OnlinePaymentService huaRenOnlinePaymentService;


    public String huaRenCallback(HuaRenCallbackReq req) {
        return commonCallback(huaRenOnlinePaymentService.callBackProcess(req), PayConstants.PAY_CALLBACK_FAIL, PayConstants.PAY_CALLBACK_BIG_SUCCESS);
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
    public boolean paymentSuccess(RechargeCallBackDto callBackDto, PayRechargeOrder rechargeOrder) {
        log.info("进入回调数据成功处理========================================={}==", rechargeOrder.getOrderNo());
        // 【分布式读写锁】
        try {
            MemBaseinfo memBaseinfo = new MemBaseinfo();//todo
            double amount = rechargeOrder.getRealAmount().setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
            if (null != memBaseinfo) {
                Date now = new Date();
                int count = payRechargeOrderMapper.countMemIsFirstRecharge(rechargeOrder.getMemId());
                if (count == 0) {
                    PayOrderFirst firstOrder = new PayOrderFirst();
                    firstOrder.setOrderNo(rechargeOrder.getOrderNo());
                    firstOrder.setMemId(rechargeOrder.getMemId());
                    firstOrder.setCreateUser("system");
                    firstOrder.setPayType(1);
                    firstOrder.setPayTime(now);
                    payOrderFirstMapper.insert(firstOrder);
                }
                //更新充值订单表信息
                rechargeOrder.setTotalAmount(ViewUtil.getTradeOffAmount(new BigDecimal(amount)));
                rechargeOrder.setTransactionNo(callBackDto.getTransactionNo());
                rechargeOrder.setOrderStatus(PayConstants.PAY_RECHARGE_STATUS_COMPLETE);
                rechargeOrder.setPayTime(now);
                boolean flag = payRechargeOrderMapper.updateById(rechargeOrder) > 0;
                if (!flag) {
                    throw new RuntimeException("修改订单为成功状态失败");
                }
                //更新会员等级 替换 updateMemLevel
                // TODO
                MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
                goldChangeDO.setChangeAmount(rechargeOrder.getRealAmount());
                goldChangeDO.setTradingEnum(TradingEnum.INCOME);
                goldChangeDO.setGoldchangeEnum(GoldchangeEnum.CZ);
                goldChangeDO.setUserId(rechargeOrder.getMemId());
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
