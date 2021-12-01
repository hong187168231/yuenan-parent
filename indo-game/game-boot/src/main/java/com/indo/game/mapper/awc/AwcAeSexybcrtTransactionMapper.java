package com.indo.game.mapper.awc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.game.pojo.entity.awc.AwcAeSexybcrtTransaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AwcAeSexybcrtTransactionMapper extends BaseMapper<AwcAeSexybcrtTransaction> {

    @Insert({
            "<script>",
            "game_awc_ae_sexybcrt_Transaction (id, game_type, win_amount, settle_status, real_bet_amount, real_win_amount, tx_time, update_time, user_id, bet_type, platform, tx_status, bet_amount, game_name, bet_time, game_code, currency, jackpot_bet_amount, jackpot_win_amount, turnover, round_id, game_info) values ",
            "<foreach collection='awcAeSexybcrtTransactionList' item='item' index='index' separator=','>",
            "(#{item.id}, game_type}, #{item.win_amount}, #{item.settle_status}, #{item.real_bet_amount}, #{item.real_win_amount}, #{item.tx_time}, #{item.update_time}, #{item.user_id}, #{item.bet_type}, #{item.platform}, #{item.tx_status}, #{item.bet_amount}, #{item.game_name}, #{item.bet_time}, #{item.game_code}, #{item.currency}, #{item.jackpot_bet_amount}, #{item.jackpot_win_amount}, #{item.turnover}, #{item.round_id}, #{item.game_info})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param(value="awcAeSexybcrtTransactionList") List<AwcAeSexybcrtTransaction> awcAeSexybcrtTransactionList);
}
