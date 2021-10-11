package com.live.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.live.admin.pojo.entity.SysParameter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 系统参数 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Repository
@Mapper
public interface SysParameterMapper extends BaseMapper<SysParameter> {


}
