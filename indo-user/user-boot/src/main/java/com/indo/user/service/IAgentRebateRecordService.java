package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.base.service.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IAgentRebateRecordService extends SuperService<AgentRebateRecord> {

    AgentRebateInfoVO rebateInfo(LoginInfo loginInfo);


    Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req, LoginInfo loginInfo);

    Page<AgentRebateRecordVO> subRebateList(AgentRebateRecordReq req, LoginInfo loginInfo);
}
