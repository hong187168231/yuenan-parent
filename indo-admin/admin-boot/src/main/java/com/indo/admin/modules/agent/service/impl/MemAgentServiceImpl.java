package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.MemAgentMapper;
import com.indo.admin.modules.agent.service.IMemAgentService;
import com.indo.admin.modules.mem.mapper.MemBaseInfoMapper;
import com.indo.admin.pojo.dto.LogErrorDTO;
import com.indo.admin.pojo.entity.AgentApply;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.entity.SysUser;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.dto.AgentDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
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
    private MemBaseInfoMapper memBaseInfoMapper;

    @Override
    public List<AgentVo> agentlist(Page<AgentVo> page, AgentDTO agentDTO) {
        List<AgentVo> list = baseMapper.agentList(page, agentDTO);
        return list;
    }

    @Override
    public String queryAgentByNickName(String nickName) {
        LambdaQueryWrapper<MemBaseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBaseinfo::getNickName, nickName);
        MemBaseinfo memBaseinfo = memBaseInfoMapper.selectOne(wrapper);
        if (memBaseinfo == null) {

        }
        LambdaQueryWrapper<MemAgent> memAgentWrapper = new LambdaQueryWrapper<>();
        memAgentWrapper.eq(MemAgent::getMemId, memBaseinfo.getId());
        MemAgent memAgent = baseMapper.selectOne(memAgentWrapper);
        if (memAgent != null) {

        }
        return "ok";
    }

    @Override
    public boolean addAgent(Long memId) {
        MemAgent memAgent = new MemAgent();
        memAgent.setMemId(memId);
        return baseMapper.insert(memAgent) > 0;
    }
}
