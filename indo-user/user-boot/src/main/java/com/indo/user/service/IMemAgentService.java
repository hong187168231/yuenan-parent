package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.PageResult;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.req.mem.MemAgentStatReq;
import com.indo.user.pojo.req.mem.SubordinateReq;
import com.indo.user.pojo.vo.AgentStatVo;
import com.indo.user.pojo.vo.SubordinateVo;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-12
 */
public interface IMemAgentService extends IService<MemAgent> {

    AgentStatVo agentStat(MemAgentStatReq req);

    PageResult<MemAgent> subordinatePage(SubordinateReq req);
}
