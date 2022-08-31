package com.indo.admin.modules.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.SysIpLimit;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 黑白名单IP限制表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-04-02
 */
@Mapper
public interface SysIpLimitMapper extends BaseMapper<SysIpLimit> {

}
