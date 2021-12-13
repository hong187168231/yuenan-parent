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
    public PageResult<MemBaseInfoVo> queryList(MemBaseInfoPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemBaseInfoVo> page = new Page<>(pageNum, pageSize);
        List<MemBaseInfoVo> list = memBaseInfoMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public int addMemBaseInfo(MemAddReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        BeanUtils.copyProperties(req, memBaseinfo);
        memBaseinfo.setPasswordMd5(MD5.md5(req.getPassword()));
        if (StringUtils.isNotBlank(req.getSuperAccno())) {
            MemBaseinfo memBaseinfo1 = baseMapper.selectOne(new QueryWrapper<MemBaseinfo>().lambda().eq(MemBaseinfo::getAccountNo, req.getSuperAccno()));
            MemInviteCode memInviteCode = memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getMemId, memBaseinfo1.getId()));
            memBaseinfo.setRInviteCode(memInviteCode.getInviteCode());
        }
        return baseMapper.insert(memBaseinfo);
    }

    @Override
    public int editMemBaseInfo(MemEditReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(req.getId());
        BeanUtils.copyProperties(req, memBaseinfo);
        return baseMapper.updateById(memBaseinfo);
    }

    @Override
    public MemBaseDetailVO getMemBaseInfo(Long uid) {
        MemBaseinfo memBaseinfo = baseMapper.selectById(uid);
        MemBaseDetailVO memBaseDetailVO = new MemBaseDetailVO();
        BeanUtils.copyProperties(memBaseinfo, memBaseDetailVO);
        return memBaseDetailVO;
    }

    @Override
    public int editStatus(MemEditStatusReq req) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setId(req.getId());
        BeanUtils.copyProperties(memBaseinfo, memBaseinfo);
        return baseMapper.updateById(memBaseinfo);
    }
}
