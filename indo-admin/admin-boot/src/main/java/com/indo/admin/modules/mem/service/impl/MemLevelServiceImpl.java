package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.mem.req.MemLevelAddReq;
import com.indo.admin.modules.mem.req.MemLevelPageReq;
import com.indo.admin.modules.mem.req.MemLevelUpdateReq;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.admin.modules.mem.vo.MemLevelVo;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.user.pojo.entity.MemLevel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<MemLevelVo> selectByPage(MemLevelPageReq req) {
        Page<MemLevelVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemLevelVo> list = memLevelMapper.listByMemLevel(page);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public boolean saveOne(MemLevelAddReq req) {
        MemLevel memLevel = new MemLevel();
        BeanUtils.copyProperties(req, memLevel);
        if (baseMapper.insert(memLevel) > 0) {
            refreshMemLevel();
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateOne(MemLevelUpdateReq req) {
        MemLevel memLevel = new MemLevel();
        BeanUtils.copyProperties(req, memLevel);
        if (baseMapper.updateById(memLevel) > 0) {
            refreshMemLevel();
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delMemLevel(Long id) {
        if (this.baseMapper.deleteById(id) > 0) {
            refreshMemLevel();
        }
        return false;
    }


    @Transactional
    void refreshMemLevel() {
        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper();
        List<MemLevel> levelList = this.baseMapper.selectList(wrapper);
        AdminBusinessRedisUtils.refreshMemLevel(levelList);
    }

}