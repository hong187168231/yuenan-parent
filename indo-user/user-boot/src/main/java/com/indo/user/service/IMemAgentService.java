package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.RebateStatVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IMemAgentService extends SuperService<AgentRelation> {

    boolean apply(MemAgentApplyReq req, LoginInfo loginInfo, HttpServletRequest request);

    Integer applyStatus(LoginInfo loginInfo);

    boolean takeRebate(BigDecimal rebateAmount, Long memBankId, LoginInfo loginInfo, HttpServletRequest request);

    Page<AgentSubVO> subordinatePage(SubordinateAppReq req, LoginInfo loginInfo);

    AgentRebateInfoVO rebateInfo(LoginInfo loginInfo);

    Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req, LoginInfo loginInfo);

    RebateStatVO rebateStat(String beginTime, String endTime, LoginInfo loginInfo);

}
