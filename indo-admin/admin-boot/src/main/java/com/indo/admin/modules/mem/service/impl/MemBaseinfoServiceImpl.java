package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.admin.modules.mem.entity.MemInviteCode;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.result.PageResult;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MemBaseinfoServiceImpl extends ServiceImpl<MemBaseinfoMapper, MemBaseinfo> implements IMemBaseinfoService {

    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;
    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;

    @Override
    public Page<MemBaseInfoVo> queryList(MemBaseInfoPageReq req) {
        Page<MemBaseInfoVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean addMemBaseInfo(MemAddReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        BeanUtils.copyProperties(req, memBaseinfo);
        memBaseinfo.setPasswordMd5(MD5.md5(req.getPassword()));
        if (StringUtils.isNotBlank(req.getSuperAccno())) {
            MemBaseinfo memBaseinfo1 = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccount, req.getSuperAccno()));
            MemInviteCode memInviteCode = memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getMemId, memBaseinfo1.getId()));
            memBaseinfo.setRInviteCode(memInviteCode.getInviteCode());
        }
        if (baseMapper.insert(memBaseinfo) > 0) {
            String code = ShareCodeUtils.idToCode(memBaseinfo.getId());
            MemInviteCode memInviteCode = new MemInviteCode();
            memInviteCode.setMemId(memBaseinfo.getId());
            memInviteCode.setInviteCode(code);
            return memInviteCodeMapper.insert(memInviteCode) > 0;
        }
        return false;
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
