package com.indo.admin.modules.act.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.admin.pojo.entity.ActivityRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 活动记录表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Mapper
@Repository
public interface ActivityRecordMapper extends BaseMapper<ActivityRecord> {

}
