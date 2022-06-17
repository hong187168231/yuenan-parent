package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActTaskTypeDTO;
import com.indo.admin.pojo.entity.ActTaskType;

/**
 * <p>
 * 任务类型表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-06-16
 */
public interface IActTaskTypeService extends IService<ActTaskType> {
    /**
     * 分页查询
     * @param actTaskTypeDTO
     * @return
     */
  Page<ActTaskType>findPage(ActTaskTypeDTO actTaskTypeDTO);
}
