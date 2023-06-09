package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.pay.mapper.PayManualRechargeMapper;
import com.indo.admin.modules.pay.mapper.PayRechargeMapper;
import com.indo.admin.modules.pay.service.IPayManualRechargeService;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
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
    private PayRechargeMapper payRechargeOrderMapper;

    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;


    @Override
    public Page<ManualRechargeMemVO> memList(Long page, Long limit, String account) {
        Page<ManualRechargeMemVO> muPage = new Page<>(page, limit);
        List<ManualRechargeMemVO> result = this.baseMapper.memList(muPage, account);
        muPage.setRecords(result);
        return muPage;
    }

    @Override
    @Transactional
    public boolean operateRecharge(Integer operateType, Long memId, Float amount,String remarks, HttpServletRequest request) {
        MemBaseinfo memBaseinfo = memBaseinfoMapper.selectById(memId);
        String countryCode = request.getHeader("countryCode");
        if (null == memBaseinfo) {
            throw new BizException(MessageUtils.get(ResultCode.USERNAME_NONENTITY.getCode(),countryCode));
        }
        BigDecimal operateAmount = new BigDecimal(amount);
        PayRecharge rechargeOrder = new PayRecharge();
        rechargeOrder.setMemId(memId);
        if(operateType.equals(1)){
            rechargeOrder.setOrderNo(GeneratorIdUtil.generateId());
            //实际金额
            rechargeOrder.setOldAmount(operateAmount);
            rechargeOrder.setTotalAmount(operateAmount);
            rechargeOrder.setRealAmount(operateAmount);
            rechargeOrder.setOrderStatus(GlobalConstants.PAY_RECHARGE_STATUS_PROCESS);
            payRechargeOrderMapper.insert(rechargeOrder);
        }
            PayManualRecharge payManualRecharge = new PayManualRecharge();
            if(rechargeOrder.getRechargeId()!=null){
                payManualRecharge.setRechargeId(rechargeOrder.getRechargeId());
            }else {
                payManualRecharge.setRechargeId(null);
            }
            payManualRecharge.setMemId(memId);
            payManualRecharge.setGoldChangeId(null);
            payManualRecharge.setAccount(memBaseinfo.getAccount());
            payManualRecharge.setAmount(operateAmount);
            payManualRecharge.setOperateType(operateType);
            payManualRecharge.setBeforAmount(memBaseinfo.getBalance());
            if(operateType.equals(1)){
                payManualRecharge.setAfterAmount(memBaseinfo.getBalance().add(operateAmount));
            }
            if(operateType.equals(2)){
                payManualRecharge.setAfterAmount(memBaseinfo.getBalance().subtract(operateAmount));
            }
            payManualRecharge.setCreateUser(JwtUtils.getUsername());
            payManualRecharge.setCreateTime(new Date());
            payManualRecharge.setRemarks(remarks);
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
        throw new BizException(ResultCode.SYSTEM_EXECUTION_ERROR);
    }

    @Override
    public Page<ManualRechargeRecordVO> queryList(Integer page, Integer limit, String account, Integer operateType) {
        Page<ManualRechargeRecordVO> mrPage = new Page<>(page, limit);
        List<ManualRechargeRecordVO> list = this.baseMapper.queryList(mrPage, account, operateType);
        mrPage.setRecords(list);
        return mrPage;

    }


}
