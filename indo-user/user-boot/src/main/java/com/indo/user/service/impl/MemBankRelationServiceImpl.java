package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.MemBankRelation;
import com.indo.user.mapper.MemBankRelationMapper;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.vo.MemBankVo;
import com.indo.user.service.IMemBankRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-17
 */
@Service
public class MemBankRelationServiceImpl extends ServiceImpl<MemBankRelationMapper, MemBankRelation> implements IMemBankRelationService {

    @Autowired
    private MemBankRelationMapper memBankRelationMapper;


    @Override
    public boolean addBankCard(AddBankCardReq req, LoginInfo loginUser) {
        MemBankRelation memBankRelation = new MemBankRelation();
        BeanUtils.copyProperties(req, memBankRelation);
        memBankRelation.setMemId(loginUser.getId());
        return baseMapper.insert(memBankRelation) > 0;
    }

    @Override
    public List<MemBankVo> findPage(LoginInfo loginUser) {
        LambdaQueryWrapper<MemBankRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBankRelation::getMemId, loginUser.getId());
        List<MemBankRelation> list = memBankRelationMapper.selectList(wrapper);
        List<MemBankVo> result = DozerUtil.convert(list, MemBankVo.class);
        return result;
    }

}
