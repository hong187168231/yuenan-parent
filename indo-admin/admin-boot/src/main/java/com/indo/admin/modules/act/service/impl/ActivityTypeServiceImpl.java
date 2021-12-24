package com.indo.admin.modules.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.indo.admin.modules.act.mapper.ActivityTypeMapper;
import com.indo.admin.modules.act.service.IActivityTypeService;
import com.indo.admin.pojo.dto.ActivityTypeDTO;
import com.indo.admin.pojo.entity.ActivityType;
import com.indo.admin.pojo.vo.ActivityTypeVO;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
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
    public Result<List<ActivityTypeVO>> queryList(ActivityTypeDTO activityTypeDTO) {
        Page<ActivityType> agentApplyPage = new Page<>(activityTypeDTO.getPage(), activityTypeDTO.getLimit());
        LambdaQueryWrapper<ActivityType> wrapper = new LambdaQueryWrapper<>();
        Page<ActivityType> pageList = this.baseMapper.selectPage(agentApplyPage, wrapper);
        List<ActivityTypeVO> result = dozerUtil.convert(pageList.getRecords(), ActivityTypeVO.class);
        return Result.success(result, agentApplyPage.getTotal());
    }

    @Override
    public boolean add(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = new ActivityType();
        BeanUtils.copyProperties(activityTypeDTO, activityType);
        return baseMapper.insert(activityType) > 0;
    }

    @Override
    public boolean edit(ActivityTypeDTO activityTypeDTO) {
        ActivityType activityType = new ActivityType();
        BeanUtils.copyProperties(activityTypeDTO, activityType);
        return baseMapper.updateById(activityType) > 0;
    }
}
