package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.PageResult;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.entity.MemNotice;
import com.indo.user.pojo.req.mem.MemNoticePageReq;

/**
 * <p>
 * 会员站内信 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
public interface IMemNoticeService extends IService<MemNotice> {

    PageResult<MemNotice> getPage(MemNoticePageReq req);

}
