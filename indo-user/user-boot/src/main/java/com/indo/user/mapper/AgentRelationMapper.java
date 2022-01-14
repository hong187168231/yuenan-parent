package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
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
public interface AgentRelationMapper extends BaseMapper<AgentRelation> {

    List<AgentSubVO> subordinateList(Page<AgentSubVO> page, SubordinateAppReq req);

}
