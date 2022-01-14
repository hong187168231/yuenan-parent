package com.indo.admin.modules.stat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.stat.mapper.StatUserRetentionMapper;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.service.IUserRetentionService;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.core.pojo.entity.StatUserRetention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
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
    public Page<UserRetentionVo> queryList(UserRetentionPageReq req) {
        Page<UserRetentionVo> page = new Page<>(req.getPage(), req.getLimit());
        List<UserRetentionVo> list = userRetentionMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }


}
