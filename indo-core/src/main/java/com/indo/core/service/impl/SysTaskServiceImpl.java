package com.indo.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.mapper.SysTaskMapper;
import com.indo.core.pojo.dto.SysTaskDTO;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.service.ISysTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements ISysTaskService {

    @Override
    public Page<SysTask> findSysTaskPage(SysTaskDTO sysTaskDTO) {
        Page<SysTask> page = new Page<>(sysTaskDTO.getPage(), sysTaskDTO.getLimit());
        LambdaQueryWrapper<SysTask> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectPage(page,wrapper);
    }
}
