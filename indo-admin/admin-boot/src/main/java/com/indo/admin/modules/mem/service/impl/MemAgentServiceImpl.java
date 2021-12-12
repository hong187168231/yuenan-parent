package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.common.result.PageResult;
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
public class MemAgentServiceImpl extends ServiceImpl<MemAgentMapper, MemAgent> implements IMemAgentService {

    @Autowired
    private MemAgentMapper memAgentMapper;

    @Override
    public PageResult<MemAgent> getPage(MemAgentPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemAgent> page = new Page<>(pageNum, pageSize);
        List<MemAgent> list = memAgentMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }
}
