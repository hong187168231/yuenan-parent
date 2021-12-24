package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.mapper.PayBankMapper;
import com.indo.user.pojo.entity.PayBank;
import com.indo.user.pojo.vo.PayBankVO;
import com.indo.user.service.IPayBankService;
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

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public List<PayBankVO> bankList() {
        LambdaQueryWrapper<PayBank> wrapper = new LambdaQueryWrapper<>();
        List<PayBank> list = baseMapper.selectList(wrapper);
        List<PayBankVO> result = dozerUtil.convert(list, PayBankVO.class);
        return result;
    }
}
