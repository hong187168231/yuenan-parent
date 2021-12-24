package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityRecordDTO;
import com.indo.admin.pojo.entity.Activity;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.common.result.Result;

import java.util.List;

/**
 * <p>
 * 活动记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
public interface IActivityService extends IService<Activity> {


    /**
     * 分页查询
     *
     * @param activityRecordDTO
     * @return
     */
    Result<List<ActivityRecordVO>> queryList(ActivityRecordDTO activityRecordDTO);

    /**
     * 新增活动
     *
     * @param activityDTO
     * @return
     */
    boolean add(ActivityDTO activityDTO);


    /**
     * 编辑活动
     *
     * @param activityDTO
     * @return
     */
    boolean edit(ActivityDTO activityDTO);


    /**
     * 删除活动
     *
     * @param actId
     * @return
     */
    boolean delAct(Long actId);

    /**
     * 活动上下架
     *
     * @param actId
     * @param status
     * @return
     */
    boolean operateStatus(Long actId, Integer status);

}
