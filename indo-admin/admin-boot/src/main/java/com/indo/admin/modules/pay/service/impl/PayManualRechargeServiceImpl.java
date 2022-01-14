package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.pay.mapper.PayManualRechargeMapper;
import com.indo.admin.modules.pay.mapper.PayRechargeMapper;
import com.indo.admin.modules.pay.service.IPayManualRechargeService;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayManualRecharge;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.pojo.vo.ManualRechargeMemVO;
import com.indo.pay.pojo.vo.ManualRechargeRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 人工充值表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-01-12
 */
@Service
public class PayManualRechargeServiceImpl extends SuperServiceImpl<PayManualRechargeMapper, PayManualRecharge> implements IPayManualRechargeService {

    @Autowired
    private PayManualRechargeMapper payManualRechargeMapper;

    @Autowired
    private PayRechargeMapper payRechargeOrderMapper;

    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;

    @Override
    public Page<ManualRechargeMemVO> memList(Integer page, Integer limit, String account) {
        Page<ManualRechargeMemVO> muPage = new Page<>(page, limit);
        List<ManualRechargeMemVO> list = payManualRechargeMapper.memList(muPage, account);
        muPage.setRecords(list);
        return muPage;
    }

    @Override
    @Transactional
    public boolean operateRecharge(Integer operateType, Long memId, Float amount) {
        MemBaseinfo memBaseinfo = memBaseinfoMapper.selectById(memId);
        if (null == memBaseinfo) {
            throw new BizException("该用户不存在");
        }
        BigDecimal operateAmount = new BigDecimal(amount);
        PayRecharge rechargeOrder = new PayRecharge();
        rechargeOrder.setMemId(memId);
        rechargeOrder.setOrderNo("RG_" + SnowflakeIdWorker.createOrderSn());
        //实际金额
        rechargeOrder.setOldAmount(operateAmount);
        rechargeOrder.setTotalAmount(operateAmount);
        rechargeOrder.setRealAmount(operateAmount);
        rechargeOrder.setOrderStatus(GlobalConstants.PAY_RECHARGE_STATUS_PROCESS);
        int row = payRechargeOrderMapper.insert(rechargeOrder);
        if (row > 0) {
            PayManualRecharge payManualRecharge = new PayManualRecharge();
            payManualRecharge.setRechargeId(rechargeOrder.getRechargeId());
            payManualRecharge.setMemId(memId);
            payManualRecharge.setGoldChangeId(null);
            payManualRecharge.setAccount(memBaseinfo.getAccount());
            payManualRecharge.setOperateType(operateType);
            payManualRecharge.setBeforAmount(memBaseinfo.getBalance());
            payManualRecharge.setCreateUser(JwtUtils.getUsername());
            if (baseMapper.insert(payManualRecharge) > 0) {
                MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
                goldChangeDO.setChangeAmount(rechargeOrder.getRealAmount());
                if (operateType.equals(1)) {
                    goldChangeDO.setTradingEnum(TradingEnum.INCOME);
                } else {
                    goldChangeDO.setTradingEnum(TradingEnum.SPENDING);
                }
                goldChangeDO.setGoldchangeEnum(GoldchangeEnum.RGCZ);
                goldChangeDO.setUserId(rechargeOrder.getMemId());
                goldChangeDO.setChangeAmount(operateAmount);
                goldChangeDO.setRefId(payManualRecharge.getManualRechargeId());
                iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
                return true;
            }
            throw new RuntimeException("系统错误");
        }
        return false;
    }

    @Override
    public Page<ManualRechargeRecordVO> queryList(Integer page, Integer limit, String account, Integer operateType) {
        Page<ManualRechargeRecordVO> mrPage = new Page<>(page, limit);
        List<ManualRechargeRecordVO> list = payManualRechargeMapper.queryList(mrPage, account, operateType);
        mrPage.setRecords(list);
        return mrPage;

    }


}
