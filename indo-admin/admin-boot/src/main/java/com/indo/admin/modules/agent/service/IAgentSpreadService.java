package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.vo.act.AdvertiseVO;
import com.indo.admin.pojo.vo.agent.AgentSpreadVO;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.AgentSpread;
import com.indo.admin.pojo.req.agnet.AgentSpreadReq;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>
 * 代理推广表 服务类
 * </p>
 *
 * @author louis
 * @since 2022-03-11
 */
public interface IAgentSpreadService extends IService<AgentSpread> {
    /**
     * 分页查询代理推广
     * @param req
     * @return
     */
    Page<AgentSpread>  findAgentSpreadPage(AgentSpreadReq req);

    /**
     * 新增代理推广
     * @param agentSpread
     */
    void insertAgentSpread(AgentSpreadReq agentSpread,HttpServletRequest request);

    /**
     * 修改代理推广
     */
    void updateAgentSpread(AgentSpreadReq agentSpread, HttpServletRequest request);

}
