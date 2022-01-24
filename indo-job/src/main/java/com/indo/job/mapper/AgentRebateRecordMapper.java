package com.indo.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.core.pojo.entity.AgentRebateRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Mapper
public interface AgentRebateRecordMapper extends BaseMapper<AgentRebateRecord> {

    @Select("SELECT arr.today_remain FROM agent_rebate_record arr WHERE account =#{account}" +
            " and  TO_DAYS(NOW()) - TO_DAYS(gt.create_time)  < 1 ")
    BigDecimal yesterdayRemain(@Param("account") String account);
}
