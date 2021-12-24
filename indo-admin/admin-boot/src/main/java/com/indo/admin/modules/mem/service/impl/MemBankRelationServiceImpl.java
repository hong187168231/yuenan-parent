package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemBankRelation;
import com.indo.admin.modules.mem.mapper.MemBankRelationMapper;
import com.indo.admin.modules.mem.req.MemBankRelationPageReq;
import com.indo.admin.modules.mem.req.MemBankRelationSwitchStatusReq;
import com.indo.admin.modules.mem.service.IMemBankRelationService;
import com.indo.admin.modules.mem.vo.MemBankRelationVO;
import com.indo.common.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-11-06
 */
@Service
public class MemBankRelationServiceImpl extends ServiceImpl<MemBankRelationMapper, MemBankRelation> implements IMemBankRelationService {

    @Autowired
    private MemBankRelationMapper memBankRelationMapper;

    @Override
    public Page<MemBankRelationVO> queryList(MemBankRelationPageReq req) {
        Page<MemBankRelationVO> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBankRelationVO> list = memBankRelationMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public void switchStatus(MemBankRelationSwitchStatusReq req) {
        MemBankRelation memBankRelation = new MemBankRelation();
        memBankRelation.setId(req.getId());
        memBankRelation.setStatus(req.getStatus());
        baseMapper.updateById(memBankRelation);
    }
}
