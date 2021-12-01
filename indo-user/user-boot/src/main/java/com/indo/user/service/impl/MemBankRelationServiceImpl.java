package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.result.PageResult;
import com.indo.user.mapper.MemBankMapper;
import com.indo.user.mapper.MemBankRelationMapper;
import com.indo.user.pojo.entity.MemBank;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.BankCardPageReq;
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
    @Autowired
    private MemBankMapper memBankMapper;


    @Override
    public void addbankCard(AddBankCardReq req) {
        MemBankRelation memBankRelation = new MemBankRelation();
        BeanUtils.copyProperties(req, memBankRelation);
        baseMapper.insert(memBankRelation);
    }

    @Override
    public PageResult<MemBankRelation> findPage(BankCardPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemBankRelation> page = new Page<>(pageNum, pageSize);
        List<MemBankRelation> list = memBankRelationMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public List<MemBank> findAllBank() {
        return memBankMapper.selectList(new QueryWrapper<>());
    }
}
