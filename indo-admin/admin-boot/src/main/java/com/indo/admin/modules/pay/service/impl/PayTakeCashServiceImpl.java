package com.indo.admin.modules.pay.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.pay.mapper.PayTakeCashMapper;
import com.indo.admin.modules.pay.service.IPayTakeCashService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.pay.api.WithdrawFeignClient;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import com.indo.pay.pojo.req.PayTakeCashReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 提现服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayTakeCashServiceImpl extends ServiceImpl<PayTakeCashMapper, PayTakeCash> implements IPayTakeCashService {
    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;
    @Override
    public Page<PayTakeCash> cashApplyList(PayTakeCashReq req) {
        Page<PayTakeCash> applyPage = new Page<>(req.getPage(), req.getLimit());
        LambdaQueryWrapper<PayTakeCash> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PayTakeCash::getCreateTime);
        return baseMapper.selectPage(applyPage, queryWrapper);
    }

    @Override
    public Page<PayTakeCash> cashRecordList(PayTakeCashReq cashOrderDTO) {
        Page<PayTakeCash> page = new Page<>(cashOrderDTO.getPage(), cashOrderDTO.getLimit());
        LambdaQueryWrapper<PayTakeCash> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cashOrderDTO.getOrderNo())) {
            wrapper.eq(PayTakeCash::getOrderNo, cashOrderDTO.getOrderNo());
        }
        if (cashOrderDTO.getOrderStatus() != null) {
            wrapper.eq(PayTakeCash::getCashStatus, cashOrderDTO.getOrderStatus());
        }
        if (cashOrderDTO.getAccount() != null) {
            wrapper.eq(PayTakeCash::getAccount,cashOrderDTO.getAccount());
        }
        if (cashOrderDTO.getBeginAmount() != null) {
            wrapper.ge(PayTakeCash::getActualAmount, cashOrderDTO.getBeginAmount());
        }
        if (cashOrderDTO.getEndAmount() != null) {
            wrapper.le(PayTakeCash::getActualAmount, cashOrderDTO.getEndAmount());
        }
        if (cashOrderDTO.getBeginTime() != null) {
            wrapper.ge(PayTakeCash::getCreateTime, cashOrderDTO.getBeginTime());
        }
        if (cashOrderDTO.getEndTime() != null) {
            wrapper.le(PayTakeCash::getCreateTime, cashOrderDTO.getEndTime());
        }
        wrapper.orderByDesc(PayTakeCash::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean takeCashOpera(AudiTypeEnum audiTypeEnum, Long takeCashId) {
        PayTakeCash payTakeCash = this.baseMapper.selectById(takeCashId);
        if (ObjectUtil.isEmpty(payTakeCash)) {
            throw new BizException(ResultCode.DATA_NONENTITY);
        }
        if(StringUtils.isNotEmpty(payTakeCash.getOperatorUser())){
            if(!payTakeCash.getOperatorUser().equals(JwtUtils.getUsername())){
                throw new BizException(ResultCode.HANDLER_ERROR);
            }
        }
        if (payTakeCash.getCashStatus().equals(GlobalConstants.PAY_CASH_STATUS_REJECT)) {
            throw new BizException(ResultCode.DATA_STATUS_ERROR);
        }
        if (payTakeCash.getCashStatus().equals(GlobalConstants.PAY_CASH_STATUS_CANCEL)) {
            throw new BizException(ResultCode.DATA_STATUS_ERROR);
        }
        if(audiTypeEnum.getStatus().equals(GlobalConstants.PAY_CASH_STATUS_CANCEL)){
            payTakeCash.setRemitTime(new Date());
        }
        if(audiTypeEnum.getStatus().equals(GlobalConstants.PAY_CASH_STATUS_REJECT)){
            MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
            goldChangeDO.setChangeAmount(payTakeCash.getActualAmount());
            goldChangeDO.setTradingEnum(TradingEnum.INCOME);
            goldChangeDO.setGoldchangeEnum(GoldchangeEnum.TXKK_REFUSE);
            goldChangeDO.setUserId(payTakeCash.getMemId());
            goldChangeDO.setUpdateUser(JwtUtils.getUsername());
            iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
        }
        payTakeCash.setCashStatus(audiTypeEnum.getStatus());
        payTakeCash.setOperatorUser(JwtUtils.getUsername());
        payTakeCash.setUpdateUser(JwtUtils.getUsername());
        payTakeCash.setUpdateTime(new Date());
        return this.baseMapper.updateById(payTakeCash) > 0;
    }
}
