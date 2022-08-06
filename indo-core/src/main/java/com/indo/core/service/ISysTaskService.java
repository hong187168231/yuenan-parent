package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.dto.SysTaskDTO;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.pojo.entity.SysTaskType;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
public interface ISysTaskService extends IService<SysTask> {
    /**
     * 查询任务分页
     * @param sysTaskDTO
     * @return
     */
    Page<SysTask> findSysTaskPage(SysTaskDTO sysTaskDTO);
}
