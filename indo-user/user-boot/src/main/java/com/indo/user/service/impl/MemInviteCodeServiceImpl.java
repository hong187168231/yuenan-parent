package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.pojo.entity.MemInviteCode;
import com.indo.user.mapper.MemInviteCodeMapper;
import com.indo.user.pojo.vo.Invite.InviteCodeVo;
import com.indo.user.service.IMemInviteCodeService;
import org.springframework.stereotype.Service;

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

    @Override
    public InviteCodeVo findInviteCode(Long memId) {
        LambdaQueryWrapper<MemInviteCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemInviteCode::getMemId, memId);
        MemInviteCode memInviteCode = this.baseMapper.selectOne(wrapper);
        if (memInviteCode == null) {
            return null;
        }
        InviteCodeVo codeVo = new InviteCodeVo();
        codeVo.setInviteCode(memInviteCode.getInviteCode());
        return codeVo;
    }
}
