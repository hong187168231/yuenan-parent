package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.MemAgentMapper;
import com.indo.admin.modules.agent.service.IMemAgentService;
import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.AgentDetailVO;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.admin.pojo.vo.SubordinateMemVo;
import com.indo.common.result.ResultCode;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.dto.AgentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
@Service
public class MemAgentServiceImpl extends ServiceImpl<MemAgentMapper, MemAgent> implements IMemAgentService {


    @Resource
    private DozerUtil dozerUtil;

    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;

    @Override
    public List<AgentVo> agentlist(Page<AgentVo> page, AgentDTO agentDTO) {
        List<AgentVo> list = baseMapper.agentList(page, agentDTO);
        return list;
    }

    @Override
    public List<SubordinateMemVo> subordinateMemList(Long agentId) {
        LambdaQueryWrapper<MemAgent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemAgent::getParentId, agentId);
        List<MemAgent> memAgentList = baseMapper.selectList(wrapper);


        return null;
    }

    @Override
    public String queryAgentByNickName(String nickName) {
        LambdaQueryWrapper<MemBaseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBaseinfo::getNickName, nickName);
        MemBaseinfo memBaseinfo = memBaseInfoMapper.selectOne(wrapper);
        if (memBaseinfo == null) {
            throw new BizException(ResultCode.USERNAME_NONENTITY.getCode());
        }
        LambdaQueryWrapper<MemAgent> memAgentWrapper = new LambdaQueryWrapper<>();
        memAgentWrapper.eq(MemAgent::getMemId, memBaseinfo.getId());
        MemAgent memAgent = baseMapper.selectOne(memAgentWrapper);
        if (memAgent != null) {
            throw new BizException(ResultCode.USER_ALREADY_AGENT.getCode());
        }
        return "ok";
    }

    @Override
    public boolean addAgent(Long memId) {
        MemAgent memAgent = new MemAgent();
        memAgent.setMemId(memId);
        return baseMapper.insert(memAgent) > 0;
    }

    @Override
    public boolean updateAgent(Long agentId) {
        MemAgent memAgent = baseMapper.selectById(agentId);
        if (memAgent == null) {
            throw new BizException(ResultCode.AGENT_NONENTITY.getCode());
        }
        return true;
    }

    @Override
    public AgentDetailVO agentDetail(Long agentId) {
        MemAgent memAgent = baseMapper.selectById(agentId);
        if (memAgent == null) {
            throw new BizException(ResultCode.AGENT_NONENTITY.getCode());
        }
        AgentDetailVO agentDetailVO = new AgentDetailVO();
        agentDetailVO.setAgentId(1L);
        agentDetailVO.setMemId(1L);
        agentDetailVO.setFacebook("facebook");
        agentDetailVO.setWhatsapp("whatsapp");
        agentDetailVO.setRealName("zs");
        agentDetailVO.setMobile("12345776575");
        return agentDetailVO;
    }
}
