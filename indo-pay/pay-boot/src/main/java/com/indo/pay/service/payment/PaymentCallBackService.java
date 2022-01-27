package com.indo.pay.service.payment;

import com.alibaba.fastjson.JSON;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayFirstRecharge;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.pay.mapper.PayFirstRechargeMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.common.constant.PayConstants;
import com.indo.common.utils.ViewUtil;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.resp.EasyCallbackReq;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
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
    private RechargeMapper payRechargeOrderMapper;

    @Resource
    private PayFirstRechargeMapper payOrderFirstMapper;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;


    @Resource(name = "huaRenOnlinePaymentService")
    private OnlinePaymentService huaRenOnlinePaymentService;
    @Resource(name = "easyOnlinePaymentService")
    private OnlinePaymentService easyOnlinePaymentService;



    public String huaRenCallback(HuaRenCallbackReq req) {
        return commonCallback(huaRenOnlinePaymentService.callBackProcess(req), PayConstants.PAY_CALLBACK_FAIL, PayConstants.PAY_CALLBACK_BIG_SUCCESS);
    }
    public String easyCallback(EasyCallbackReq req) {
        return commonCallback(easyOnlinePaymentService.callBackProcess(req), PayConstants.PAY_CALLBACK_FAIL, PayConstants.PAY_CALLBACK_BIG_OK);
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
    public boolean paymentSuccess(PayCallBackDTO callBackDto, PayRecharge payRecharge) {
        log.info("进入回调数据成功处理========================================={}==", payRecharge.getOrderNo());
        // 【分布式读写锁】
        try {
            MemBaseinfo memBaseinfo = new MemBaseinfo();//todo
            double amount = payRecharge.getRealAmount().setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
            if (null != memBaseinfo) {
                Date now = new Date();
                int count = payRechargeOrderMapper.countMemIsFirstRecharge(payRecharge.getMemId());
                if (count == 0) {
                    PayFirstRecharge firstOrder = new PayFirstRecharge();
                    firstOrder.setOrderNo(payRecharge.getOrderNo());
                    firstOrder.setMemId(payRecharge.getMemId());
                    firstOrder.setCreateUser("system");
                    firstOrder.setPayType(1);
                    firstOrder.setPayTime(now);
                    payOrderFirstMapper.insert(firstOrder);
                }
                //更新充值订单表信息
                payRecharge.setTotalAmount(ViewUtil.getTradeOffAmount(new BigDecimal(amount)));
                payRecharge.setTransactionNo(callBackDto.getTransactionNo());
                payRecharge.setOrderStatus(GlobalConstants.PAY_RECHARGE_STATUS_COMPLETE);
                payRecharge.setPayTime(now);
                boolean flag = payRechargeOrderMapper.updateById(payRecharge) > 0;
                if (!flag) {
                    throw new RuntimeException("修改订单为成功状态失败");
                }
                //更新会员等级 替换 updateMemLevel
                // TODO
                MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
                goldChangeDO.setChangeAmount(payRecharge.getRealAmount());
                goldChangeDO.setTradingEnum(TradingEnum.INCOME);
                goldChangeDO.setGoldchangeEnum(GoldchangeEnum.CZ);
                goldChangeDO.setUserId(payRecharge.getMemId());
//                goldChangeDO.setRefId(rechargeOrder.getRechargeOrderId());
                boolean changeFlag = iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
                if (!changeFlag) {
                    log.info("payDataOrder error parm{}", JSON.toJSONString(goldChangeDO));
                    throw new RuntimeException("订单处理失败！");
                }
            }
            log.info("进入回调数据成功处理结束========================================={}==", payRecharge.getOrderNo());
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
