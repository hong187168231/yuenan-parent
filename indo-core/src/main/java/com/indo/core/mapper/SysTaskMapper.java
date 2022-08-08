package com.indo.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.SysTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 任务表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Mapper
public interface SysTaskMapper extends BaseMapper<SysTask> {
    /**
     * 查询用户今日转账充值金额
     * @param memId
     * @return
     */
  BigDecimal findMemAmountTransferredToday(@Param("memId") Long memId);

    /**
     * 查询用户下级数量
     * @param memId
     * @return
     */
  Integer findMemSubNum(@Param("memId") Long memId);
}
