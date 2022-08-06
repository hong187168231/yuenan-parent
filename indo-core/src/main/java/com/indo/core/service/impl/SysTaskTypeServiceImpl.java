package com.indo.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.mapper.SysTaskMapper;
import com.indo.core.mapper.SysTaskTypeMapper;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.pojo.entity.SysTaskType;
import com.indo.core.service.ISysTaskTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 任务类型表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Service
public class SysTaskTypeServiceImpl extends ServiceImpl<SysTaskTypeMapper, SysTaskType> implements ISysTaskTypeService {
 @Resource
 private SysTaskMapper sysTaskMapper;
    @Override
    public Page<SysTaskType> findSysTaskTypePage(SysTaskTypeDTO sysTaskTypeDTO) {
        Page<SysTaskType> page = new Page<>(sysTaskTypeDTO.getPage(), sysTaskTypeDTO.getLimit());
        LambdaQueryWrapper<SysTaskType> wrapper = new LambdaQueryWrapper<>();
        page = baseMapper.selectPage(page,wrapper);
        page.getRecords().forEach(l->{
            LambdaQueryWrapper<SysTask> taskwrapper = new LambdaQueryWrapper<>();
            taskwrapper.eq(SysTask::getTypeId,l.getId());
            Integer num = sysTaskMapper.selectCount(taskwrapper);
            l.setTaskNum(num);
        });
        return page;
    }
}
