package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.user.pojo.entity.MemBank;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * <p>
 * 银行信息配置表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Mapper
public interface MemBankMapper extends BaseMapper<MemBank> {

}
