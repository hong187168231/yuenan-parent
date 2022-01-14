package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.MemAgent;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IMemAgentService extends SuperService<MemAgent> {

    boolean apply(MemAgentApplyReq req, LoginInfo loginInfo);

    Page<AgentSubVO> subordinatePage(SubordinateAppReq req, LoginInfo loginInfo);

}
