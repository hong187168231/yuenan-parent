package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemInviteCode;
import com.indo.admin.modules.mem.req.MemInviteCodeSwitchStatusReq;
import com.indo.admin.modules.mem.req.MeminviteCodePageReq;
import com.indo.admin.modules.mem.vo.MemInviteCodeVo;
import com.indo.common.mybatis.base.PageResult;

/**
 * <p>
 * 会员邀请码 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
public interface IMemInviteCodeService extends IService<MemInviteCode> {

    PageResult<MemInviteCodeVo> queryList(MeminviteCodePageReq req);

    void switchStatus(MemInviteCodeSwitchStatusReq req);
}
