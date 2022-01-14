package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.MeminviteCodePageReq;
import com.indo.admin.pojo.vo.MemInviteCodeVo;
import com.indo.admin.pojo.req.mem.InviteCodeSwitchReq;
import com.indo.core.pojo.entity.MemInviteCode;

/**
 * <p>
 * 会员邀请码 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
public interface IMemInviteCodeService extends IService<MemInviteCode> {

    Page<MemInviteCodeVo> queryList(MeminviteCodePageReq req);

    void switchStatus(InviteCodeSwitchReq req);
}
