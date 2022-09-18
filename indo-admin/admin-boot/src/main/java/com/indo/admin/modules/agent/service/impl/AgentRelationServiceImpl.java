package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.agent.service.IAgentRelationService;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.req.agnet.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.MemBaseinfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Service
public class AgentRelationServiceImpl extends ServiceImpl<AgentRelationMapper, AgentRelation> implements IAgentRelationService {

    @Autowired
    private AgentRelationMapper memAgentMapper;
    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;
    @Autowired
    private AgentRelationMapper agentRelationMapper;
    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;

    @Override
    public Page<AgentVo> getPage(MemAgentReq req) {
        Page<AgentVo> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentVo> list = memAgentMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public AgentRelation findByParentId(Long parentId) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentRelation::getParentId, parentId);
        return memAgentMapper.selectOne(wrapper);
    }

    @Override
    public Page<AgentSubVO> subordinatePage(SubordinateReq req) {
        Page<AgentSubVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentSubVO> agentSubVOList = new ArrayList<>();
        if (StringUtils.isNotEmpty(req.getSuperior())) {
            MemBaseInfoBO memBaseInfoBO = memBaseInfoMapper.findMemBaseInfoByAccount(req.getSuperior());
            if (memBaseInfoBO == null) {
                return page;
            }

            AgentRelation agentRelation = findByParentId(memBaseInfoBO.getId());
            if (agentRelation == null) {
                return page;
            }

            if (StringUtils.isBlank(agentRelation.getSubUserIds())) {
                return page;
            }

            List<Long> memIds = Arrays.asList(agentRelation.getSubUserIds().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
            agentSubVOList = agentRelationMapper.subordinateListByMemIds(page, memIds);
        } else {
            agentSubVOList = agentRelationMapper.subordinateListByMemIds(page, null);
        }
        page.setRecords(agentSubVOList);
        return page;
    }

    @Override
    public boolean upgradeAgent(String account, HttpServletRequest request) {
        LambdaQueryWrapper<MemBaseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBaseinfo::getAccount, account);
        MemBaseinfo memBaseinfo = memBaseinfoMapper.selectOne(wrapper);
        if (null == memBaseinfo) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.USERNAME_NONENTITY.getCode(),countryCode));
        }
        memBaseinfo.setAccType(2);
        return memBaseinfoMapper.updateById(memBaseinfo) > 0;
    }
}
