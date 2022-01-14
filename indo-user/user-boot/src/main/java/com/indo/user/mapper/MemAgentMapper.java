package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.pojo.req.mem.SubordinateReq;
import org.apache.ibatis.annotations.Mapper;

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
public interface MemAgentMapper extends BaseMapper<MemAgent> {

    List<AgentSubVO> subordinateList(Page<AgentSubVO> page, SubordinateAppReq req);

}
