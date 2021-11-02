package com.indo.admin.modules.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.act.mapper.ActivityRecordMapper;
import com.indo.admin.modules.act.service.IActivityRecordService;
import com.indo.admin.pojo.dto.ActivityRecordDTO;
import com.indo.admin.pojo.entity.ActivityRecord;
import com.indo.admin.pojo.entity.ActivityType;
import com.indo.admin.pojo.vo.ActivityRecordVO;
import com.indo.admin.pojo.vo.ActivityTypeVO;
import com.indo.common.result.Result;
import com.indo.common.web.util.DozerUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
public class ActivityRecordServiceImpl extends ServiceImpl<ActivityRecordMapper, ActivityRecord> implements IActivityRecordService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Result<List<ActivityRecordVO>> queryList(ActivityRecordDTO activityRecordDTO) {
        Page<ActivityRecord> page = new Page<>(activityRecordDTO.getPage(), activityRecordDTO.getLimit());
        LambdaQueryWrapper<ActivityRecord> wrapper = new LambdaQueryWrapper<>();
        Page<ActivityRecord> pageList = this.baseMapper.selectPage(page, wrapper);
        List<ActivityRecordVO> result = dozerUtil.convert(pageList.getRecords(), ActivityRecordVO.class);
        return Result.success(result, page.getTotal());
    }

    @Override
    public boolean add(ActivityRecordDTO activityRecordDTO) {
        ActivityRecord activityRecord = new ActivityRecord();
        BeanUtils.copyProperties(activityRecordDTO, activityRecord);
        return baseMapper.insert(activityRecord) > 0;
    }

    @Override
    public boolean edit(ActivityRecordDTO activityRecordDTO) {
        ActivityRecord activityRecord = new ActivityRecord();
        BeanUtils.copyProperties(activityRecordDTO, activityRecord);
        return baseMapper.updateById(activityRecord) > 0;
    }
}
