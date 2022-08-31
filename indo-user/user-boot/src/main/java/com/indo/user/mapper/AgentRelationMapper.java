package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员下级表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-12
 */
@Mapper
public interface AgentRelationMapper extends BaseMapper<AgentRelation> {

    List<AgentSubVO> subordinateList(Page<AgentSubVO> page, @Param("memIds") List<Long> memIds);

    @Select("SELECT IFNULL(ar.team_num, 0) from agent_relation ar  WHERE  ar.`status` = 1 and ar.superior = #{account}")
    Integer selectTeamNum(String account);


    @Select("SELECT IFNULL(sum(arr.rebate_amout),0.00) from agent_rebate_record arr WHERE arr.`status` = 1 and arr.account = #{account} " +
            " and   create_time between #{beginTime} and #{endTime} ")
    BigDecimal selectRebateByTime(String account, String beginTime, String endTime);


    @Select("SELECT " +
            " IFNULL(SUM(pr.real_amount),0.00) " +
            " FROM" +
            " pay_recharge pr " +
            " RIGHT  JOIN agent_relation ar ON pr.account = ar.account " +
            " AND ar.superior = #{account} " +
            " and pr.order_status = 1  " +
            " and   pr.pay_time between #{beginTime} and #{endTime} ")
    BigDecimal selectTeamRecharge(String account, String beginTime, String endTime);


    @Select("SELECT" +
            " IFNULL(SUM(gt.real_bet_amount),0.00) AS teamBet " +
            " FROM " +
            " game_txns gt " +
            " RIGHT JOIN agent_relation ar ON gt.user_id = ar.account " +
            " AND ar.superior = #{account} " +
            " and  gt.bet_time between #{beginTime} and #{endTime} ")
    BigDecimal selectTeamBet(String account, String beginTime, String endTime);


    @Select("SELECT count(1) from agent_relation ar  WHERE  ar.`status` = 1 and ar.superior = #{account} " +
            " and  ar.create_time BETWEEN CONCAT(CURDATE(),' 00:00:00') AND CONCAT(CURDATE(),' 23:59:59') ")
    Integer selectDayAddNum(String account);

    @Select("SELECT count(1) from agent_relation ar  WHERE  ar.`status` = 1 and ar.superior = #{account} " +
            "  and   create_time between #{beginTime} and #{endTime} ")
    Integer selectMonthAddNum(String account, String beginTime, String endTime);
}
