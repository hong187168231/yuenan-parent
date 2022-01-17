package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.entity.AgentApply;
import com.indo.core.pojo.entity.AgentRebate;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.AgentApplyMapper;
import com.indo.user.mapper.AgentRebateMapper;
import com.indo.user.mapper.AgentRebateRecordMapper;
import com.indo.user.mapper.AgentRelationMapper;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.service.IMemAgentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-11
 */
@Service
public class AppAgentServiceImpl extends SuperServiceImpl<AgentRelationMapper, AgentRelation> implements IMemAgentService {


    @Autowired
    private AgentApplyMapper agentApplyMapper;
    @Autowired
    private AgentRelationMapper agentRelationMapper;
    @Autowired
    private AgentRebateRecordMapper memRebateRecordMapper;
    @Autowired
    private AgentRebateMapper agentRebateMapper;


    @Override
    public boolean apply(MemAgentApplyReq req, LoginInfo loginInfo) {
        String uuidKey = UserBusinessRedisUtils.get(req.getUuid());
        if (StringUtils.isEmpty(uuidKey) || !req.getImgCode().equalsIgnoreCase(uuidKey)) {
            throw new BizException("图像验证码错误！");
        }
        AgentApply agentApply = new AgentApply();
        agentApply.setMemId(loginInfo.getId());
        agentApply.setStatus(0);
        return agentApplyMapper.insert(agentApply) > 0;
    }

    @Override
    public Page<AgentSubVO> subordinatePage(SubordinateAppReq req, LoginInfo loginInfo) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentRelation::getSuperior, loginInfo.getAccount());
        Integer agentCount = agentRelationMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("你还没有下级");
        }
        wrapper.eq(AgentRelation::getAccount, req.getAccount());
        agentCount = agentRelationMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("请输入正确的下级账号");
        }
        Page<AgentSubVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentSubVO> agentList = agentRelationMapper.subordinateList(page, req);
        page.setRecords(agentList);
        return page;
    }


    @Override
    public AgentRebateInfoVO rebateInfo(LoginInfo loginInfo) {
        AgentRebateInfoVO infoVO = new AgentRebateInfoVO();
        BigDecimal rebateAmount = new BigDecimal("0.00");
        LambdaQueryWrapper<AgentRebate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentRebate::getMemId, loginInfo.getId());
        AgentRebate agentRebate = agentRebateMapper.selectOne(wrapper);
        if (null == agentRebate) {
            infoVO.setRebateAmount(rebateAmount);
        } else {
            infoVO.setRebateAmount(agentRebate.getRebateAmount());
        }
        return infoVO;
    }

    @Override
    public Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req, LoginInfo loginInfo) {
        req.setAccount(loginInfo.getAccount());
        Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<AgentRebateRecordVO> subRebateList(AgentRebateRecordReq req, LoginInfo loginInfo) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentRelation::getSuperior, loginInfo.getAccount());
        Integer agentCount = agentRelationMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("你还没有下级");
        }
        wrapper.eq(AgentRelation::getAccount, req.getAccount());
        agentCount = agentRelationMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("请输入正确的下级账号");
        }
        req.setAccount(loginInfo.getAccount());
        Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
