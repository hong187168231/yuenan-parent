package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBankMapper;
import com.indo.admin.pojo.req.mem.MemBankPageReq;
import com.indo.admin.pojo.req.mem.MemBankSwitchReq;
import com.indo.admin.modules.mem.service.IMemBankService;
import com.indo.admin.pojo.vo.mem.MemBankVO;
import com.indo.core.pojo.entity.MemBank;
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
public class MemBankServiceImpl extends ServiceImpl<MemBankMapper, MemBank> implements IMemBankService {

    @Autowired
    private MemBankMapper memBankMapper;

    @Override
    public Page<MemBankVO> queryList(MemBankPageReq req) {
        Page<MemBankVO> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBankVO> list = memBankMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public void switchStatus(MemBankSwitchReq req) {
        MemBank memBankRelation = new MemBank();
        memBankRelation.setMemBankId(req.getId());
        memBankRelation.setStatus(req.getStatus());
        baseMapper.updateById(memBankRelation);
    }
}
