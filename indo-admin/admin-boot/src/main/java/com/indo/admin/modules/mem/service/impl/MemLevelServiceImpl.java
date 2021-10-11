package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.admin.pojo.criteria.MemLevelQueryCriteria;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.utils.QueryHelpPlus;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.MemLevelVo;
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