package com.indo.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.game.GameTxns;
import com.indo.job.pojo.dto.BeforeDayBetDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface GameTxnsMapper extends BaseMapper<GameTxns> {

    List<BeforeDayBetDTO> beforeDayBetList(@Param("startTime") String startTime,@Param("endTime") String endTime);


    BigDecimal teamSumBet(@Param("account") String account);
}
