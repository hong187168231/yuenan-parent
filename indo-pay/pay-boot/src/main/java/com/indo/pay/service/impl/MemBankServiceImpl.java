package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.util.DozerUtil;
import com.indo.pay.mapper.MemBankMapper;
import com.indo.pay.pojo.entity.MemBank;
import com.indo.pay.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.vo.MemBankVO;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import com.indo.pay.service.IMemBankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 银行信息配置表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-29
 */
@Service
public class MemBankServiceImpl extends ServiceImpl<MemBankMapper, MemBank> implements IMemBankService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public List<MemBankVO> memBankList(LoginInfo loginInfo) {
        LambdaQueryWrapper<MemBank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBank::getUserId, loginInfo.getId());
        List<MemBank> list = baseMapper.selectList(wrapper);
        List<MemBankVO> result = dozerUtil.convert(list, MemBankVO.class);
        return result;
    }
}
