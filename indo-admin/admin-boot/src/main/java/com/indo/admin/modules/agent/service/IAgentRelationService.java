package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.req.agnet.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.core.pojo.entity.AgentRelation;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IAgentRelationService extends IService<AgentRelation> {

    Page<AgentVo> getPage(MemAgentReq req);

	AgentRelation findByParentId(Long parentId);

	Page<AgentSubVO> subordinatePage(SubordinateReq req);

    boolean upgradeAgent(String  account, HttpServletRequest request);

}
