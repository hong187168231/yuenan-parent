package com.indo.admin.modules.mem.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.ShareCodeUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.encrypt.MD5;
import com.indo.admin.modules.mem.req.MemAddReq;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.req.MemEditStatusReq;
import com.indo.admin.modules.mem.req.MemEditReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.mem.vo.MemBaseDetailVO;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.entity.MemInviteCode;
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
    private MemAgentMapper memAgentMapper;

    @Override
    public Page<MemBaseInfoVo> queryList(MemBaseInfoPageReq req) {
        Page<MemBaseInfoVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public void addMemBaseInfo(MemAddReq req) {
        MemBaseinfoBo curentMem = getMemCacheBaseInfo(req.getAccount());
        if (curentMem != null) {
            throw new BizException("该账号已存在!");
        }
        MemBaseinfoBo supperMem = null;
        if (StringUtils.isNotBlank(req.getSuperAccno())) {
            supperMem = this.getMemCacheBaseInfo(req.getSuperAccno());
            if (supperMem == null || !supperMem.getAccType().equals(2)) {
                throw new BizException("请填入正确的代理账号");
            }
        }
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        MemAgent memAgent = new MemAgent();
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
            int row = memAgentMapper.insert(memAgent);
            if (row > 0) {
                initMemParentAgent(memBaseinfo, supperMem.getId());
            }
        }
    }


    public void initMemParentAgent(MemBaseinfo memBaseinfo, Long parentId) {
        LambdaQueryWrapper<MemAgent> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MemAgent::getMemId, parentId)
                .eq(MemAgent::getIsDel, false);
        MemAgent parentAgent = memAgentMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(wrapper)) {
            throw new BizException("该邀请人未成为代理");
        }
        String subUserIds = StringUtils.isBlank(parentAgent.getSubUserIds()) ?
                memBaseinfo.getId() + "" : parentAgent.getSubUserIds() + "," + memBaseinfo.getId();
        parentAgent.setSubUserIds(subUserIds);
        parentAgent.setTeamNum(parentAgent.getTeamNum() + 1);
        memAgentMapper.updateById(parentAgent);
    }

    @Override
    public boolean editMemBaseInfo(MemEditReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(req.getId());
        BeanUtils.copyProperties(req, memBaseinfo);
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public boolean updateMemLevel(Long memId, Integer memLevel) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setMemLevel(memLevel);
        memBaseinfo.setId(
                memId);
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public MemBaseDetailVO getMemBaseInfo(Long uid) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(uid);
        MemBaseDetailVO memBaseDetailVO = new MemBaseDetailVO();
        BeanUtils.copyProperties(memBaseinfo, memBaseDetailVO);
        return memBaseDetailVO;
    }

    @Override
    public MemBaseDetailVO getMemBaseInfoByAccount(String account) {
        MemBaseinfo memBaseinfo = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, account));
        MemBaseDetailVO memBaseDetailVO = new MemBaseDetailVO();
        BeanUtils.copyProperties(memBaseinfo, memBaseDetailVO);
        return memBaseDetailVO;
    }

    @Override
    public boolean editStatus(MemEditStatusReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(req.getId());
        BeanUtils.copyProperties(req, memBaseinfo);
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public boolean resetPassword(Long memId) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(memId);
        memBaseinfo.setPasswordMd5(MD5.md5("12345678"));
        return baseMapper.updateById(memBaseinfo) > 0;
    }

    @Override
    public List<Long> findIdListByCreateTime(Date date) {
        return memBaseInfoMapper.findIdListByCreateTime(DateUtils.format(date, DateUtils.webFormat));
    }
}
