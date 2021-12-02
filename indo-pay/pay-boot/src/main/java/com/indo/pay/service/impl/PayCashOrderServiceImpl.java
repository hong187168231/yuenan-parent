package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.pay.mapper.PayCashOrderMapper;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import com.indo.pay.service.IPayCashOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Service
public class PayCashOrderServiceImpl extends ServiceImpl<PayCashOrderMapper, PayCashOrder> implements IPayCashOrderService {

    @Resource
    private DozerUtil dozerUtil;


    @Override
    public Result<List<PayCashOrderVO>> cashRecordList(PayCashOrderDTO cashOrderDTO) {
        Page<PayCashOrder> page = new Page<>(cashOrderDTO.getPage(), cashOrderDTO.getLimit());
        LambdaQueryWrapper<PayCashOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayCashOrder::getUserId, cashOrderDTO.getUserId());
        Page<PayCashOrder> pageList = baseMapper.selectPage(page, wrapper);
        List<PayCashOrderVO> result = dozerUtil.convert(pageList.getRecords(), PayCashOrderVO.class);
        return Result.success(result, page.getTotal());
    }
}
