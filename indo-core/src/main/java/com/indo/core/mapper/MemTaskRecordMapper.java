package com.indo.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.MemTaskRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Mapper
public interface MemTaskRecordMapper extends BaseMapper<MemTaskRecord> {
    /**
     * 查询用户今日任务奖励领取记录
     * @return
     */
   List<MemTaskRecord> findTodayTaskRecord(@Param("memId") Long memId);

    /**
     * 根据code查询用户任务奖励领取记录
     */
   List<MemTaskRecord> findTaskRecordByCode(@Param("memId") Long memId,@Param("code")String code);

    /**
     * 查询会员当天各游戏类型下注金额
     * @param account
     * @param gameType
     * @return
     */
   BigDecimal findMemBetAmountByGameType(@Param("account") String account,@Param("gameType")String gameType);
}
