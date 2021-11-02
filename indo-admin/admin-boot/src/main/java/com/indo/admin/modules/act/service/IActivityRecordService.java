package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActivityRecordDTO;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.entity.ActivityRecord;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.admin.pojo.vo.ActivityTypeVO;
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
public interface IActivityRecordService extends IService<ActivityRecord> {



    /**
     * 分页查询
     * @param activityRecordDTO
     * @return
     */
    Result<List<ActivityRecordVO>> queryList(ActivityRecordDTO activityRecordDTO);

    /**
     * 新增活动
     * @param activityRecordDTO
     * @return
     */
    boolean add(ActivityRecordDTO activityRecordDTO);


    /**
     * 编辑活动
     * @param activityRecordDTO
     * @return
     */
    boolean edit(ActivityRecordDTO activityRecordDTO);

}
