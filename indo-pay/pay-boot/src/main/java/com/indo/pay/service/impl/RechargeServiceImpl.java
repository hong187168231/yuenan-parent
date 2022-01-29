package com.indo.pay.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.mapper.PayWayMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.bo.PayChannel;
import com.indo.pay.pojo.bo.RechargeBO;
import com.indo.pay.pojo.bo.PayWay;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.RechargeReq;
import com.indo.pay.service.IPayChannelService;
import com.indo.pay.service.IPayWayService;
import com.indo.pay.service.rechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class RechargeServiceImpl extends SuperServiceImpl<RechargeMapper, PayRecharge> implements rechargeService {

    @Autowired
    private IPayChannelService iPayChannelService;

    @Autowired
    private IPayWayService iPayWayService;

    @Override
    public boolean saveRechargeRecord(RechargeDTO rechargeDTO) {
        PayRecharge rechargeOrder = new PayRecharge();
        rechargeOrder.setMemId(rechargeDTO.getMemId());
        rechargeOrder.setOrderNo(rechargeDTO.getOrderNo());
//        rechargeOrder.setPayWayId(payRequestVo.getChannelWay());
        //实际金额
        BigDecimal amount = new BigDecimal(rechargeDTO.getAmount());
        rechargeOrder.setOldAmount(amount);
        rechargeOrder.setTotalAmount(amount);
        rechargeOrder.setRealAmount(amount);
        rechargeOrder.setOrderStatus(GlobalConstants.PAY_RECHARGE_STATUS_PROCESS);
        return this.baseMapper.insert(rechargeOrder) > 0;

    }

    @Override
    public RechargeBO logicConditionCheck(RechargeReq rechargeReq, LoginInfo loginInfo) {
        MemBaseInfoBO currentMem = getMemCacheBaseInfo(loginInfo.getAccount());
        if (currentMem.getProhibitRecharge().equals(1)) {
            throw new BizException("你暂不能发起充值,请联系管理员");
        }
        PayChannelConfig payChannelConfig = iPayChannelService.getPayChannelById(rechargeReq.getPayChannelId());
        if (ObjectUtil.isEmpty(payChannelConfig)) {
            throw new BizException("暂无充值渠道");
        }
        PayWayConfig payWayCfg = iPayWayService.getPayWayById(rechargeReq.getPayWayId());
        if (ObjectUtil.isEmpty(payWayCfg)) {
            throw new BizException("暂无充值方式");
        }
        if (null != payWayCfg.getMinAmount() &&
                rechargeReq.getAmount().intValue() < payWayCfg.getMinAmount()) {
            throw new BizException("小于渠道最低充值金额");
        }
        if (null != payWayCfg.getMaxAmount() &&
                rechargeReq.getAmount().intValue() > payWayCfg.getMaxAmount()) {
            throw new BizException("大于渠道最高充值金额");
        }
        RechargeBO rechargeBO = new RechargeBO();
        rechargeBO.setPayChannel(DozerUtil.map(payChannelConfig, PayChannel.class));
        rechargeBO.setPayWay(DozerUtil.map(payWayCfg, PayWay.class));
        return rechargeBO;
    }
}
