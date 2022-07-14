package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActTaskDTO;
import com.indo.admin.pojo.dto.ActTaskTypeDTO;
import com.indo.admin.pojo.entity.ActTask;
import com.indo.admin.pojo.entity.ActTaskType;

/**
 * <p>
 * 任务记录表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
public interface IActTaskService extends IService<ActTask> {
    /**
     * 分页查询
     * @param actTaskDTO
     * @return
     */
    Page<ActTask> findPage(ActTaskDTO actTaskDTO);
}
