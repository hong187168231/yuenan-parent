package com.live.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.mem.mapper.MemLevelMapper;
import com.live.admin.modules.mem.service.IMemLevelService;
import com.live.admin.pojo.criteria.MemLevelQueryCriteria;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.utils.QueryHelpPlus;
import com.live.user.pojo.entity.MemLevel;
import com.live.user.pojo.vo.MemLevelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户等级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Service
public class MemLevelServiceImpl extends SuperServiceImpl<MemLevelMapper, MemLevel> implements IMemLevelService {

    @Autowired
    private MemLevelMapper memLevelMapper;

    @Override
    public List<MemLevelVo> selectByPage(Page<MemLevelVo> page) {
        List<MemLevelVo> memLevelVos = memLevelMapper.listByMemLevel(page);
        return memLevelVos;
    }

    @Override
    public List<MemLevel> queryAll(MemLevelQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(MemLevel.class, criteria));
    }

}