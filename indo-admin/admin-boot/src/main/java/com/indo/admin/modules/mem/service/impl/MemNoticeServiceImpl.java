package com.indo.admin.modules.mem.service.impl;


import com.indo.admin.modules.mem.entity.MemNotice;
import com.indo.admin.modules.mem.mapper.MemNoticeMapper;
import com.indo.admin.modules.mem.service.IMemNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.user.pojo.req.mem.MemNoticeAddReq;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员站内信 服务实现类
 * </p>
 *
 * @author kevin
 * @since 2021-11-02
 */
@Service
public class MemNoticeServiceImpl extends ServiceImpl<MemNoticeMapper, MemNotice> implements IMemNoticeService {

    @Override
    public int add(MemNoticeAddReq req) {
        MemNotice notice = new MemNotice();
        notice.setMemId(req.getMemId());
        notice.setTitle(req.getTitle());
        notice.setContent(req.getContent());
        return baseMapper.insert(notice);
    }
}
