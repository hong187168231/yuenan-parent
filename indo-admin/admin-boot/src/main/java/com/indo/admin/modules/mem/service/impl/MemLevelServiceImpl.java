package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemLevel;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.mem.req.MemLevelAddReq;
import com.indo.admin.modules.mem.req.MemLevelPageReq;
import com.indo.admin.modules.mem.req.MemLevelUpdateReq;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.admin.modules.mem.vo.MemLevelVo;
import com.indo.admin.pojo.criteria.MemLevelQueryCriteria;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.result.PageResult;
import com.indo.common.utils.QueryHelpPlus;
import org.springframework.beans.BeanUtils;
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
    public PageResult<MemLevelVo> selectByPage(MemLevelPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemLevelVo> page = new Page<>(pageNum, pageSize);
        List<MemLevelVo> list = memLevelMapper.listByMemLevel(page);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public List<MemLevel> queryAll(MemLevelQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(MemLevel.class, criteria));
    }

    @Override
    public void saveOne(MemLevelAddReq req) {
        MemLevel memLevel = new MemLevel();
        BeanUtils.copyProperties(req, memLevel);
        baseMapper.insert(memLevel);
        return;
    }

    @Override
    public void updateOne(MemLevelUpdateReq req) {
        MemLevel memLevel = new MemLevel();
        BeanUtils.copyProperties(req, memLevel);
        baseMapper.updateById(memLevel);
        return;
    }

}