package com.indo.admin.modules.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.act.mapper.ActivityTypeMapper;
import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.vo.act.ActivityTypeVO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.ActivityType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 活动类型表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Service
public class ActivityTypeServiceImpl extends ServiceImpl<ActivityTypeMapper, ActivityType> implements IActivityTypeService {

    @Resource
    private DozerUtil dozerUtil;


    @Override
    public Result<List<ActivityTypeVO>> queryList(Integer page, Integer limit) {
        Page<ActivityType> agentApplyPage = new Page<>(page, limit);
        LambdaQueryWrapper<ActivityType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ActivityType::getUpdateTime);

        Page<ActivityType> pageList = this.baseMapper.selectPage(agentApplyPage, wrapper);
        List<ActivityTypeVO> result = dozerUtil.convert(pageList.getRecords(), ActivityTypeVO.class);
        return Result.success(result, agentApplyPage.getTotal());
    }

    @Override
    public boolean add(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = new ActivityType();
        BeanUtils.copyProperties(activityTypeDTO, activityType);
        activityType.setCreateUser(JwtUtils.getUsername());
        if (baseMapper.insert(activityType) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.ACTIVITY_TYPE_KEY, activityType.getActTypeId() + "", activityType);
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = this.baseMapper.selectById(activityTypeDTO.getActTypeId());
        if (null == activityType) {
            throw new BizException("活动类型不存在");
        }
        BeanUtils.copyProperties(activityTypeDTO, activityType);
        if (baseMapper.updateById(activityType) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.ACTIVITY_TYPE_KEY, activityType.getActTypeId() + "", activityType);
            return true;
        }
        return false;
    }

    /**
     * 修改活动类型数量
     * @param actTypeId 活动类型id
     * @return
     */
    @Override
    public boolean updateActNum(Long actTypeId, Integer actNum) {
      return SqlHelper.retBool(this.baseMapper.updateActNum(actTypeId, actNum));
    }
}
