package com.indo.admin.modules.mem.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.encrypt.MD5;
import com.indo.admin.pojo.req.mem.MemAddReq;
import com.indo.admin.pojo.req.mem.MemBaseInfoReq;
import com.indo.admin.pojo.req.mem.MemEditStatusReq;
import com.indo.admin.pojo.req.mem.MemEditReq;
import com.indo.admin.pojo.vo.mem.MemBaseInfoVo;
import com.indo.admin.pojo.vo.mem.MemBaseDetailVO;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.util.BusinessRedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员基础信息表 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
@Service
public class MemBaseinfoServiceImpl extends SuperServiceImpl<MemBaseinfoMapper, MemBaseinfo> implements IMemBaseinfoService {

    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;
    @Autowired
    private AgentRelationMapper agentRelationMapper;

    @Override
    public Page<MemBaseInfoVo> queryList(MemBaseInfoReq req) {
        Page<MemBaseInfoVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public void addMemBaseInfo(MemAddReq req) {
        MemBaseInfoBO curentMem = this.memBaseInfoMapper.findMemBaseInfoByAccount(req.getAccount());
        if (curentMem != null) {
            throw new BizException("该账号已存在!");
        }
        MemBaseInfoBO supperMem = null;
        if (StringUtils.isNotBlank(req.getSuperAccno())) {
            supperMem = this.memBaseInfoMapper.findMemBaseInfoByAccount(req.getSuperAccno());
            if (supperMem == null || !supperMem.getAccType().equals(2)) {
                throw new BizException("请填入正确的代理账号");
            }
        }
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        AgentRelation memAgent = new AgentRelation();
        BeanUtils.copyProperties(req, memBaseinfo);
        memBaseinfo.setPasswordMd5(MD5.md5(req.getPassword()));
        if (baseMapper.insert(memBaseinfo) > 0) {
            memAgent.setMemId(memBaseinfo.getId());
            memAgent.setParentId(supperMem.getId());
            memAgent.setSuperior(supperMem.getAccount());
            memAgent.setIsDel(false);
            if (memBaseinfo.getAccType().equals(2)) {
                memAgent.setIsDel(true);
            }
            int row = agentRelationMapper.insert(memAgent);
            if (row > 0) {
                initMemParentAgent(memBaseinfo, supperMem.getId());
            }
        }
    }


    public void initMemParentAgent(MemBaseinfo memBaseinfo, Long parentId) {
        LambdaQueryWrapper<AgentRelation> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AgentRelation::getMemId, parentId)
                .eq(AgentRelation::getIsDel, false);
        AgentRelation parentAgent = agentRelationMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(wrapper)) {
            throw new BizException("该邀请人未成为代理");
        }
        String subUserIds = StringUtils.isBlank(parentAgent.getSubUserIds()) ?
                memBaseinfo.getId() + "" : parentAgent.getSubUserIds() + "," + memBaseinfo.getId();
        parentAgent.setSubUserIds(subUserIds);
        parentAgent.setTeamNum(parentAgent.getTeamNum() + 1);
        agentRelationMapper.updateById(parentAgent);
    }

    @Override
    public boolean editMemBaseInfo(MemEditReq req) {
        MemBaseinfo memBaseinfo = checkMemIsExist(req.getId());
        memBaseinfo.setId(req.getId());
        DozerUtil.map(req, memBaseinfo);
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        DozerUtil.map(req, memBaseInfoDTO);
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public boolean updateMemLevel(Long memId, Integer memLevel) {
        MemBaseinfo memBaseinfo = checkMemIsExist(memId);
        memBaseinfo.setMemLevel(memLevel);
        memBaseinfo.setId(memId);
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setMemLevel(memLevel);
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public MemBaseinfo getMemBaseInfo(Long uid) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(uid);
        return memBaseinfo;
    }

    @Override
    public MemBaseDetailVO getMemBaseInfoByAccount(String account) {
        MemBaseinfo memBaseinfo = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, account));
        MemBaseDetailVO memBaseDetailVO = new MemBaseDetailVO();
        BeanUtils.copyProperties(memBaseinfo, memBaseDetailVO);
        return memBaseDetailVO;
    }

    @Override
    @Transactional
    public boolean editStatus(MemEditStatusReq req) {
        MemBaseinfo memBaseinfo = checkMemIsExist(req.getId());
        DozerUtil.map(req, memBaseinfo);
        if (baseMapper.updateById(memBaseinfo) > 0) {
            MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
            DozerUtil.map(req, memBaseinfo);
            refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
            if (req.getStatus().equals(1) || req.getStatus().equals(2) ||
                    req.getProhibitLogin().equals(0)) {
                AdminBusinessRedisUtils.delMemAccToken(memBaseinfo.getAccount());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(Long memId) {
        MemBaseinfo memBaseinfo = checkMemIsExist(memId);
        memBaseinfo.setPasswordMd5(MD5.md5("12345678"));
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setPasswordMd5(memBaseinfo.getPasswordMd5());
        refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
        return baseMapper.updateById(memBaseinfo) > 0;
    }


    @Override
    public void refreshMemBaseInfo(MemBaseInfoDTO memBaseInfoDTO, String account) {
        MemBaseInfoBO memBaseInfoBO = this.getMemCacheBaseInfo(account);
        if (null == memBaseInfoBO) {
            memBaseInfoBO = this.baseMapper.findMemBaseInfoByAccount(account);
        }
        DozerUtil.map(memBaseInfoDTO, memBaseInfoBO);
        BusinessRedisUtils.saveMemBaseInfo(memBaseInfoBO);
    }

    public MemBaseinfo checkMemIsExist(Long id) {
        MemBaseinfo memBaseinfo = this.baseMapper.selectById(id);
        if (memBaseinfo == null) {
            throw new BizException("用户不存在");
        }
        return memBaseinfo;
    }
}
