package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashApplyVO;
import com.indo.admin.pojo.vo.agent.AgentCashRecordVO;
import com.indo.user.pojo.entity.AgentCashRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
@Mapper
public interface AgentCashRecordMapper extends BaseMapper<AgentCashRecord> {

    List<AgentCashRecordVO> recordList(@Param("page") Page<AgentCashRecordVO> vo, @Param("req") AgentCashReq req);
}
