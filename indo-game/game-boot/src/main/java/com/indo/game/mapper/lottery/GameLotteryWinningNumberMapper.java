package com.indo.game.mapper.lottery;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.GameLotteryWinningNumber;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameLotteryWinningNumberMapper extends BaseMapper<GameLotteryWinningNumber> {

    public GameLotteryWinningNumber qeryMinGameLotteryWinningNumber();
}
