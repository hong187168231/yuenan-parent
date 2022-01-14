package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.pojo.req.MeminviteCodePageReq;
import com.indo.admin.modules.mem.service.IMemInviteCodeService;
import com.indo.admin.pojo.vo.MemInviteCodeVo;
import com.indo.admin.pojo.req.mem.InviteCodeSwitchReq;
import com.indo.core.pojo.entity.MemInviteCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员邀请码 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
@Service
public class MemInviteCodeServiceImpl extends ServiceImpl<MemInviteCodeMapper, MemInviteCode> implements IMemInviteCodeService {

    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;

    @Override
    public Page<MemInviteCodeVo> queryList(MeminviteCodePageReq req) {
        Page<MemInviteCodeVo> page = new Page<>(req.getPage(), req.getLimit());
        List<MemInviteCodeVo> list = memInviteCodeMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public void switchStatus(InviteCodeSwitchReq req) {
        MemInviteCode memInviteCode = new MemInviteCode();
        memInviteCode.setId(req.getId());
        memInviteCode.setStatus(req.getStatus());
        baseMapper.updateById(memInviteCode);
    }
}
