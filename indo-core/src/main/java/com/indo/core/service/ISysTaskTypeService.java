package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTaskType;

/**
 * <p>
 * 任务类型表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
public interface ISysTaskTypeService extends IService<SysTaskType> {
    /**
     * 查询任务类型分页
     * @param sysTaskTypeDTO
     * @return
     */
   Page<SysTaskType> findSysTaskTypePage(SysTaskTypeDTO sysTaskTypeDTO);
}
