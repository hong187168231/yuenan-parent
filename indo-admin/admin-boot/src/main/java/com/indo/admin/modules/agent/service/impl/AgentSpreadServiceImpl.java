package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentSpreadMapper;
import com.indo.admin.modules.agent.service.IAgentSpreadService;
import com.indo.admin.pojo.vo.act.AdvertiseVO;
import com.indo.admin.pojo.vo.agent.AgentSpreadVO;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.AgentSpread;
import com.indo.admin.pojo.req.agnet.AgentSpreadReq;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代理推广表 服务实现类
 * </p>
 *
 * @author louis
 * @since 2022-03-11
 */
@Service
public class AgentSpreadServiceImpl extends ServiceImpl<AgentSpreadMapper, AgentSpread> implements IAgentSpreadService {
    @Resource
    private DozerUtil dozerUtil;
    @Override
    public Page<AgentSpread> findAgentSpreadPage(AgentSpreadReq req) {
        Page page = new Page<AgentSpread>(req.getPage(), req.getLimit());
        Wrapper wrapper = new QueryWrapper<AgentSpread>();
        return baseMapper.selectPage(page,wrapper);
    }

    @Override
    public void insertAgentSpread(AgentSpreadReq req) {
        if(StringUtils.isEmpty(req.getImageUrl()) ||StringUtils.isEmpty(req.getSkipUrl())||StringUtils.isEmpty(req.getContent())){
            throw new BizException("主要参数不可为空");
        }
        AgentSpread agentSpread = new AgentSpread();
        BeanUtils.copyProperties(req, agentSpread);
        //agentSpread.setCreateUser(JwtUtils.getUsername());
        if(baseMapper.insert(agentSpread)<=0){
            throw new BizException("代理推广信息插入失败，请与管理员联系");
        }
    }

    @Override
    public void updateAgentSpread(AgentSpreadReq req) {
        if(req.getId()==null){
            throw new BizException("主要参数不可为空");
        }
        AgentSpread agentSpread = baseMapper.selectById(req.getId());
        if(agentSpread==null){
            throw new BizException("数据不正确");
        }
        BeanUtils.copyProperties(req, agentSpread);
        //agentSpread.setUpdateUser(JwtUtils.getUsername());
        if(baseMapper.updateById(agentSpread)<=0){
            throw new BizException("代理推广信息更新失败，请与管理员联系");
        }
    }
}
