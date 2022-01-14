package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.AgentRebate;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
@Mapper
public interface AgentRebateMapper extends BaseMapper<AgentRebate> {

    @Update("update  agent_rebate set rebate_amount = rebate_amount +#{rebateAmount} where mem_id = #{memId} ")
    int modifyRebateAmount(@Param("memId") Long memId, @Param("rebateAmount") BigDecimal rebateAmount);

    @Select("select * from agent_rebate where mem_id = #{memId}")
    AgentRebate selectAgentRebateByMemId(@Param("memId") Long memId);


}
