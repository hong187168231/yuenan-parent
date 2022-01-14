package com.indo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.indo.user.pojo.entity.MemInviteCode;
import com.indo.user.pojo.vo.Invite.InviteCodeVo;

/**
 * <p>
 * 会员邀请码 服务类
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
public interface IMemInviteCodeService extends IService<MemInviteCode> {

    InviteCodeVo findInviteCode(Long memId);

}
