package com.indo.admin.modules.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.constant.SystemConstants;
import com.indo.admin.common.util.BusinessRedisUtils;
import com.indo.admin.modules.act.mapper.ActivityMapper;
import com.indo.admin.modules.act.service.IActivityService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityRecordDTO;
import com.indo.admin.pojo.entity.Activity;
import com.indo.admin.pojo.entity.Advertise;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 活动记录表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Result<List<ActivityRecordVO>> queryList(ActivityRecordDTO activityRecordDTO) {
        Page<Activity> page = new Page<>(activityRecordDTO.getPage(), activityRecordDTO.getLimit());
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        Page<Activity> pageList = this.baseMapper.selectPage(page, wrapper);
        List<ActivityRecordVO> result = dozerUtil.convert(pageList.getRecords(), ActivityRecordVO.class);
        return Result.success(result, page.getTotal());
    }

    @Override
    @Transactional
    public boolean add(ActivityDTO activityDTO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityDTO, activity);
        if (baseMapper.insert(activity) > 0) {
            BusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, activity.getActId() + "", activity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean edit(ActivityDTO activityDTO) {
        checkAdeOpera(activityDTO.getActId());
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityDTO, activity);
        if (baseMapper.updateById(activity) > 0) {
            BusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, activity.getActId() + "", activity);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean delAct(Long actId) {
        checkAdeOpera(actId);
        if (this.baseMapper.deleteById(actId) > 0) {
            BusinessRedisUtils.hdel(RedisConstants.ACTIVITY_KEY, actId + "");
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean operateStatus(Long actId, Integer status) {
        Activity activity = new Activity();
        activity.setStatus(status);
        activity.setActId(actId);
        activity.setUpdateUser(JwtUtils.getUsername());
        if (this.baseMapper.updateById(activity) > 0) {
            if (status.equals(SystemConstants.ACT_SHELVES)) {
                BusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, activity.getActId() + "", activity);
            } else if (status.equals(SystemConstants.ADE_SOLD_OUT)) {
                BusinessRedisUtils.hdel(RedisConstants.ACTIVITY_KEY, actId + "");
            }
            return true;
        }
        return false;
    }

    /**
     * 检查操作状态
     *
     * @param adeId
     */
    private void checkAdeOpera(Long adeId) {
        if (selectActShelveFlag(adeId)) {
            throw new BizException("活动状态为已上架，暂不能进行编辑修改操作!");
        }
    }


    /**
     * 查询活动上架状态
     *
     * @param adeId
     * @return
     */
    private boolean selectActShelveFlag(Long adeId) {
        Activity activity = this.baseMapper.selectById(adeId);
        if (activity != null && activity.getStatus().equals(SystemConstants.ACT_SHELVES)) {
            return true;
        }
        return false;
    }

}
