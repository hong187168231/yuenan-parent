package com.indo.admin.modules.act.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.act.mapper.ActTaskMapper;
import com.indo.admin.modules.act.mapper.ActTaskTypeMapper;
import com.indo.admin.modules.act.service.IActTaskTypeService;
import com.indo.admin.pojo.dto.ActTaskTypeDTO;
import com.indo.admin.pojo.entity.ActTask;
import com.indo.admin.pojo.entity.ActTaskType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 任务类型表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
@Service
public class ActTaskTypeServiceImpl extends ServiceImpl<ActTaskTypeMapper, ActTaskType> implements IActTaskTypeService {
    @Resource
    private ActTaskMapper actTaskMapper;
    @Override
    public Page<ActTaskType> findPage(ActTaskTypeDTO actTaskTypeDTO) {
        Page<ActTaskType> page = new Page<>(actTaskTypeDTO.getPage(), actTaskTypeDTO.getLimit());
        LambdaQueryWrapper<ActTaskType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ActTaskType::getCreateTime);
        Page<ActTaskType> pages = baseMapper.selectPage(page, queryWrapper);
        pages.getRecords().forEach(l->{
            LambdaQueryWrapper<ActTask> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ActTask::getTaskTypeId,l.getId());
            Integer count = actTaskMapper.selectCount(wrapper);
            l.setTaskNum(count);
        });
        return pages;
    }
}
