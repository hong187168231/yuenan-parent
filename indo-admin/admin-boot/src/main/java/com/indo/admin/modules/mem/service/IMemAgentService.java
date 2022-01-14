package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.MemAgentPageReq;
import com.indo.admin.pojo.req.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.core.pojo.entity.MemAgent;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IMemAgentService extends IService<MemAgent> {

    Page<AgentVo> getPage(MemAgentPageReq req);

    Page<AgentSubVO> subordinatePage(SubordinateReq req);

    boolean upgradeAgent(String  account);

}
