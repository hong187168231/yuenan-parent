package com.indo.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.result.PageResult;
import com.indo.user.mapper.MemNoticeMapper;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.entity.MemNotice;
import com.indo.user.pojo.req.mem.MemNoticePageReq;
import com.indo.user.service.IMemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员站内信 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
public class MemNoticeServiceImpl extends ServiceImpl<MemNoticeMapper, MemNotice> implements IMemNoticeService {

    @Autowired
    private MemNoticeMapper memNoticeMapper;

    @Override
    public PageResult<MemNotice> getPage(MemNoticePageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemNotice> page = new Page<>(pageNum, pageSize);
        List<MemNotice> list = memNoticeMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }
}
