package com.indo.admin.modules.act.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.act.mapper.ActTaskMapper;
import com.indo.admin.modules.act.service.IActTaskService;
import com.indo.admin.pojo.dto.ActTaskDTO;
import com.indo.admin.pojo.entity.ActTask;
import com.indo.admin.pojo.entity.ActTaskType;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
@Service
public class ActTaskServiceImpl extends ServiceImpl<ActTaskMapper, ActTask> implements IActTaskService {

    @Override
    public Page<ActTask> findPage(ActTaskDTO actTaskDTO) {
        Page<ActTask> page = new Page<>(actTaskDTO.getPage(), actTaskDTO.getLimit());
        LambdaQueryWrapper<ActTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ActTask::getCreateTime);
        Page<ActTask> pages = baseMapper.selectPage(page, queryWrapper);
        return pages;
    }
}
