package com.indo.admin.modules.stat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.stat.entity.StatUserRetention;
import com.indo.admin.modules.stat.mapper.StatUserRetentionMapper;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.service.IUserRetentionService;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.common.mybatis.base.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
@Service
public class UserRetentionServiceImpl extends ServiceImpl<StatUserRetentionMapper, StatUserRetention> implements IUserRetentionService {

    @Autowired
    private StatUserRetentionMapper userRetentionMapper;

    @Override
    public PageResult<UserRetentionVo> queryList(UserRetentionPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<UserRetentionVo> page = new Page<>(pageNum, pageSize);
        List<UserRetentionVo> list = userRetentionMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }
}
