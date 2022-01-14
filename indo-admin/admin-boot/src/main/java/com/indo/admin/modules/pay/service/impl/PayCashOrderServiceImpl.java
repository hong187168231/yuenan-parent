package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayCashOrderMapper;
import com.indo.admin.modules.pay.service.IPayCashOrderService;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.vo.PayCashOrderApplyVO;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayCashOrderServiceImpl extends ServiceImpl<PayCashOrderMapper, PayCashOrder> implements IPayCashOrderService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Result<List<PayCashOrderApplyVO>> cashApplyList(PayCashOrderDTO cashOrderDTO) {
        return null;
    }

    @Override
    public Result<List<PayCashOrderVO>> cashRecordList(PayCashOrderDTO cashOrderDTO) {
        Page<PayCashOrder> page = new Page<>(cashOrderDTO.getPage(), cashOrderDTO.getLimit());
        LambdaQueryWrapper<PayCashOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cashOrderDTO.getOrderNo())) {
            wrapper.eq(PayCashOrder::getOrderNo, cashOrderDTO.getOrderNo());
        }
        if (cashOrderDTO.getOrderStatus() != null) {
            wrapper.eq(PayCashOrder::getCashStatus, cashOrderDTO.getOrderStatus());
        }
        if (cashOrderDTO.getUserId() != null) {
            wrapper.eq(PayCashOrder::getMemId, cashOrderDTO.getUserId());
        }
        if (cashOrderDTO.getBeginAmount() != null) {
            wrapper.ge(PayCashOrder::getActualAmount, cashOrderDTO.getBeginAmount());
        }
        if (cashOrderDTO.getEndAmount() != null) {
            wrapper.le(PayCashOrder::getActualAmount, cashOrderDTO.getEndAmount());
        }
        if (cashOrderDTO.getBeginTime() != null) {
            wrapper.ge(PayCashOrder::getCreateTime, cashOrderDTO.getBeginTime());
        }
        if (cashOrderDTO.getEndTime() != null) {
            wrapper.le(PayCashOrder::getCreateTime, cashOrderDTO.getEndTime());
        }
        Page<PayCashOrder> pageList = baseMapper.selectPage(page, wrapper);
        List<PayCashOrderVO> result = dozerUtil.convert(pageList.getRecords(), PayCashOrderVO.class);
        return Result.success(result, page.getTotal());
    }
}
