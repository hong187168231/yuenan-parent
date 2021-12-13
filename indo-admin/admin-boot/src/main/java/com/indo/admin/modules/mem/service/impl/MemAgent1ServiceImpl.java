package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.mapper.MemAgent1Mapper;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.common.result.PageResult;
import com.indo.admin.modules.mem.req.SubordinateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Service
public class MemAgent1ServiceImpl extends ServiceImpl<MemAgent1Mapper, MemAgent> implements IMemAgentService {

    @Autowired
    private MemAgent1Mapper memAgent1Mapper;

    @Override
    public PageResult<MemAgent> getPage(MemAgentPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemAgent> page = new Page<>(pageNum, pageSize);
        List<MemAgent> list = memAgent1Mapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public PageResult<MemAgent> subordinatePage(SubordinateReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemAgent> page = new Page<>(pageNum, pageSize);
        List<MemAgent> list = memAgent1Mapper.subordinateList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }
}
