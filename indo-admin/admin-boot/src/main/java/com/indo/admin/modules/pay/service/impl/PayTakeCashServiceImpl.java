package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayTakeCashMapper;
import com.indo.admin.modules.pay.service.IPayTakeCashService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.req.PayTakeCashReq;
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
public class PayTakeCashServiceImpl extends ServiceImpl<PayTakeCashMapper, PayTakeCash> implements IPayTakeCashService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Page<PayTakeCashApplyVO> cashApplyList(PayTakeCashReq req) {
        Page<PayTakeCashApplyVO> applyPage = new Page<>(req.getPage(), req.getLimit());
        List<PayTakeCashApplyVO> list = this.baseMapper.cashApplyList(applyPage, req);
        applyPage.setRecords(list);
        return applyPage;
    }

    @Override
    public Result<List<PayTakeCashRecordVO>> cashRecordList(PayTakeCashReq cashOrderDTO) {
        Page<PayTakeCash> page = new Page<>(cashOrderDTO.getPage(), cashOrderDTO.getLimit());
        LambdaQueryWrapper<PayTakeCash> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cashOrderDTO.getOrderNo())) {
            wrapper.eq(PayTakeCash::getOrderNo, cashOrderDTO.getOrderNo());
        }
        if (cashOrderDTO.getOrderStatus() != null) {
            wrapper.eq(PayTakeCash::getCashStatus, cashOrderDTO.getOrderStatus());
        }
        if (cashOrderDTO.getUserId() != null) {
            wrapper.eq(PayTakeCash::getMemId, cashOrderDTO.getUserId());
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
        Page<PayTakeCash> pageList = baseMapper.selectPage(page, wrapper);
        List<PayTakeCashRecordVO> result = dozerUtil.convert(pageList.getRecords(), PayTakeCashRecordVO.class);
        return Result.success(result, page.getTotal());
    }
}
