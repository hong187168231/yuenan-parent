package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.vo.act.ActivityTypeVO;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.ActivityType;

import java.util.List;

/**
 * <p>
 * 活动类型表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
public interface IActivityTypeService extends IService<ActivityType> {


    /**
     * 分页查询
     * @param page
     * @param limit
     * @return
     */
    Result<List<ActivityTypeVO>> queryList(Integer page, Integer limit);

    /**
     * 新增活动类型
     *
     * @param activityTypeDTO
     * @return
     */
    boolean add(ActivityTypeDTO activityTypeDTO);


    /**
     * 编辑活动类型
     *
     * @param activityTypeDTO
     * @return
     */
    boolean edit(ActivityTypeDTO activityTypeDTO);

	  boolean updateActNum(Long actTypeId, Integer actNum);
}
