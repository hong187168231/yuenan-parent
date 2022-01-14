package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayBank;
import com.indo.pay.mapper.PayBankMapper;
import com.indo.pay.pojo.vo.PayBankVO;
import com.indo.pay.service.IPayBankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付银行表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Service
public class PayBankServiceImpl extends ServiceImpl<PayBankMapper, PayBank> implements IPayBankService {


    @Override
    public List<PayBankVO> bankList() {
        LambdaQueryWrapper<PayBank> wrapper = new LambdaQueryWrapper<>();
        List<PayBank> list = baseMapper.selectList(wrapper);
        List<PayBankVO> result = DozerUtil.convert(list, PayBankVO.class);
        return result;
    }
}
