package com.indo.admin.modules.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.constant.SystemConstants;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.act.mapper.ActivityMapper;
import com.indo.admin.modules.act.service.IActivityService;
import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityDTO;
import com.indo.admin.pojo.dto.ActivityQueryDTO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.Activity;
import com.indo.user.pojo.vo.act.ActivityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    IActivityTypeService activityTypeService;

    @Override
    public Result<List<ActivityVo>> queryList(ActivityQueryDTO queryDTO) {
        Page<Activity> activityPage = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryDTO.getActName())) {
            wrapper.like(Activity::getActName, queryDTO.getActName());
        }
        wrapper.orderByDesc(Activity::getUpdateTime);
        Page<Activity> pageList = this.baseMapper.selectPage(activityPage, wrapper);
        List<ActivityVo> result = dozerUtil.convert(pageList.getRecords(), ActivityVo.class);
        if(!AdminBusinessRedisUtils.hasKey(RedisConstants.ACTIVITY_KEY)){
            pageList.getRecords().forEach(l->{
                AdminBusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, l.getActTypeId() + "", l);
            });
        }
        return Result.success(result, activityPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ActivityDTO activityDTO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityDTO, activity);
        if (baseMapper.insert(activity) > 0) {
            // 增加活动数量
            activityTypeService.updateActNum(activityDTO.getActTypeId(), 1);
            AdminBusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, activity.getActId() + "", activity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean edit(ActivityDTO activityDTO, HttpServletRequest request) {
        checkAdeOpera(activityDTO.getActId(),request);
        Activity activity = findActivityById(activityDTO.getActId());
        if (null == activity) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        BeanUtils.copyProperties(activityDTO, activity);
        if (baseMapper.updateById(activity) > 0) {
            AdminBusinessRedisUtils.del(RedisConstants.ACTIVITY_KEY);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean delAct(Long actId,HttpServletRequest request) {
        checkAdeOpera(actId,request);
        Activity activity = findActivityById(actId);
        if (this.baseMapper.deleteById(actId) > 0) {
            activityTypeService.updateActNum(activity.getActTypeId(), -1);
            AdminBusinessRedisUtils.del(RedisConstants.ACTIVITY_KEY);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean operateStatus(Long actId, Integer status,HttpServletRequest request) {
        Activity activity = findActivityById(actId);
        if (null == activity) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        activity.setStatus(status);
        activity.setActId(actId);
        activity.setUpdateUser(JwtUtils.getUsername());
        if (this.baseMapper.updateById(activity) > 0) {
            if (status.equals(SystemConstants.ACT_SHELVES)) {
                AdminBusinessRedisUtils.hset(RedisConstants.ACTIVITY_KEY, activity.getActId() + "", activity);
            } else if (status.equals(SystemConstants.ADE_SOLD_OUT)) {
                AdminBusinessRedisUtils.hdel(RedisConstants.ACTIVITY_KEY, actId + "");
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
    private void checkAdeOpera(Long adeId,HttpServletRequest request) {
        if (selectActShelveFlag(adeId)) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.DATA_PUT.getCode(),countryCode));
        }
    }


    /**
     * 查询活动上架状态
     *
     * @param actId
     * @return
     */
    private boolean selectActShelveFlag(Long actId) {
        Activity activity = findActivityById(actId);
        if (activity != null && activity.getStatus().equals(SystemConstants.ACT_SHELVES)) {
            return true;
        }
        return false;
    }


    private Activity findActivityById(Long actId) {
        return this.baseMapper.selectById(actId);
    }

}
