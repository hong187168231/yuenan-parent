package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.req.SubordinateReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
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
public interface MemAgentMapper extends BaseMapper<MemAgent> {

    List<AgentVo> queryList(@Param("page") Page<AgentVo> page, @Param("req") MemAgentPageReq req);

    List<AgentSubVO> subordinateList(Page<AgentSubVO> page, SubordinateReq req);
}
