package com.indo.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.vo.game.manage.GameInfoRecord;
import com.indo.core.pojo.entity.GameTxns;
import com.indo.job.pojo.dto.BeforeDayBetDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface GameTxnsMapper extends BaseMapper<GameTxns> {

    List<BeforeDayBetDTO> beforeDayBetList(@Param("page") IPage<BeforeDayBetDTO> page);


    BigDecimal teamSumBet(@Param("account") String account);
}
