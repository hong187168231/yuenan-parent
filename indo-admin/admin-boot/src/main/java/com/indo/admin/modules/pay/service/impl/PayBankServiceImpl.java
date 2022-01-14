package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayBankMapper;
import com.indo.admin.modules.pay.service.IPayBankService;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayBank;
import com.indo.pay.pojo.dto.PayBankDTO;
import com.indo.pay.pojo.vo.PayBankVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付银行表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayBankServiceImpl extends ServiceImpl<PayBankMapper, PayBank> implements IPayBankService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public boolean addBank(PayBankDTO bankDTO) {
        PayBank payBank = new PayBank();
        BeanUtils.copyProperties(bankDTO, payBank);
        return baseMapper.insert(payBank) > 0;
    }

    @Override
    public Result<List<PayBankVO>> bankList(PayBankDTO bankDTO) {
        Page<PayBank> page = new Page<>(bankDTO.getPage(), bankDTO.getLimit());
        LambdaQueryWrapper<PayBank> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(PayBank::getBankName, bankDTO.getBankName());
        Page<PayBank> pageList = baseMapper.selectPage(page, wrapper);
        List<PayBankVO> result = dozerUtil.convert(pageList.getRecords(), PayBankVO.class);
        return Result.success(result, page.getTotal());
    }
}
