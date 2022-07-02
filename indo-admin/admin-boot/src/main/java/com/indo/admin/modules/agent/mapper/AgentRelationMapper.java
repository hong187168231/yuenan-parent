package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.dto.MemReportDTO;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.req.agnet.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.core.pojo.entity.AgentRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员下级表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Mapper
public interface AgentRelationMapper extends BaseMapper<AgentRelation> {

    List<AgentVo> queryList(@Param("page") Page<AgentVo> page, @Param("req") MemAgentReq req);

    List<AgentSubVO> subordinateList(@Param("page")Page<AgentSubVO> page,  @Param("req") SubordinateReq req);

    /**
     *  查询代理报表
     * @param page
     * @param agentReportDTO
     * @return
     */
    Page<AgentReportVo> findAgentReport(Page<AgentReportVo> page, AgentReportDTO agentReportDTO);

}
