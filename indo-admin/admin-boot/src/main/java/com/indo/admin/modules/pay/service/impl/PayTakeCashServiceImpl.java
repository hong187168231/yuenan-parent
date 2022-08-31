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
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.api.WithdrawFeignClient;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import com.indo.pay.pojo.req.PayTakeCashReq;
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
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean takeCashOpera(AudiTypeEnum audiTypeEnum, Long takeCashId) {
        PayTakeCash payTakeCash = this.baseMapper.selectById(takeCashId);
        if (ObjectUtil.isEmpty(payTakeCash)) {
            throw new BizException("提现订单不存在");
        }
        if(StringUtils.isNotEmpty(payTakeCash.getOperatorUser())){
            if(!payTakeCash.getOperatorUser().equals(JwtUtils.getUsername())){
                throw new BizException("该订单已有处理人,需由处理人继续完成后续操作");
            }
        }
        if (payTakeCash.getCashStatus().equals(GlobalConstants.PAY_CASH_STATUS_REJECT)) {
            throw new BizException("提现订单状态错误");
        }
        if (payTakeCash.getCashStatus().equals(GlobalConstants.PAY_CASH_STATUS_CANCEL)) {
            throw new BizException("提现订单状态错误");
        }
        if(audiTypeEnum.getStatus().equals(GlobalConstants.PAY_CASH_STATUS_CANCEL)){
            payTakeCash.setRemitTime(new Date());
        }
        payTakeCash.setCashStatus(audiTypeEnum.getStatus());
        payTakeCash.setOperatorUser(JwtUtils.getUsername());
        payTakeCash.setUpdateUser(JwtUtils.getUsername());
        payTakeCash.setUpdateTime(new Date());
        return this.baseMapper.updateById(payTakeCash) > 0;
    }
}
