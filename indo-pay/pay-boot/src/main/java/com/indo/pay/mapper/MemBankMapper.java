package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.pay.pojo.entity.MemBank;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 银行信息配置表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-29
 */
@Mapper
public interface MemBankMapper extends BaseMapper<MemBank> {

}
