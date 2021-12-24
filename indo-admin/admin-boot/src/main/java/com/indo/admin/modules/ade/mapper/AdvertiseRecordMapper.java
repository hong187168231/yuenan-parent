package com.indo.admin.modules.ade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.admin.pojo.entity.Advertise;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 站内信 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Mapper
public interface AdvertiseRecordMapper extends BaseMapper<Advertise> {

}
