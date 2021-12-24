package com.indo.admin.modules.mem.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemNotice;
import com.indo.admin.modules.mem.req.MemNoticeAddReq;

/**
 * <p>
 * 会员站内信 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-02
 */
public interface IMemNoticeService extends IService<MemNotice> {

    int add(MemNoticeAddReq req);
}
