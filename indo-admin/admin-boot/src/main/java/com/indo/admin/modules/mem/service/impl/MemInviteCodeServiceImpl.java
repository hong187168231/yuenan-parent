package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemInviteCode;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.modules.mem.req.MemInviteCodeSwitchStatusReq;
import com.indo.admin.modules.mem.req.MeminviteCodePageReq;
import com.indo.admin.modules.mem.service.IMemInviteCodeService;
import com.indo.admin.modules.mem.vo.MemInviteCodeVo;
import com.indo.common.result.PageResult;
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
    public PageResult<MemInviteCodeVo> queryList(MeminviteCodePageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemInviteCodeVo> page = new Page<>(pageNum, pageSize);
        List<MemInviteCodeVo> list = memInviteCodeMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public void switchStatus(MemInviteCodeSwitchStatusReq req) {
        MemInviteCode memInviteCode = new MemInviteCode();
        memInviteCode.setId(req.getId());
        memInviteCode.setStatus(req.getStatus());
        baseMapper.updateById(memInviteCode);
    }
}
