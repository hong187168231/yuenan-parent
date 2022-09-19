package com.indo.admin.modules.act.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.Activity;
import com.indo.user.pojo.vo.act.ActivityVo;

import javax.servlet.http.HttpServletRequest;
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
     * @param queryDTO
     * @return
     */
    Result<List<ActivityVo>> queryList(ActivityQueryDTO queryDTO);

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
    boolean edit(ActivityDTO activityDTO, HttpServletRequest request);


    /**
     * 删除活动
     *
     * @param actId
     * @return
     */
    boolean delAct(Long actId,HttpServletRequest request);

    /**
     * 活动上下架
     *
     * @param actId
     * @param status
     * @return
     */
    boolean operateStatus(Long actId, Integer status,HttpServletRequest request);

}
