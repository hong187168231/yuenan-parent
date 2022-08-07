package com.indo.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.dto.SysTaskDTO;
import com.indo.core.pojo.dto.SysTaskTypeDTO;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.pojo.entity.SysTaskType;
import com.indo.core.pojo.vo.TaskModuleVo;

import java.util.List;

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

    /**
     * 查询任务信息及用户完成情况
     * @param loginInfo
     * @return
     */
    List<TaskModuleVo> findMemTaskInfo(LoginInfo loginInfo);

    /**
     * 领取任务奖励
     */
    void receiveTaskReward(LoginInfo loginInfo,Integer taskId);
}
