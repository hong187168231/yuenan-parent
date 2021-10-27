package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.req.mem.MemAddReq;
import com.indo.user.pojo.req.mem.MemBaseInfoPageReq;
import com.indo.user.pojo.req.mem.MemEditFrozenStatusReq;
import com.indo.user.pojo.req.mem.MemEditReq;
import com.indo.user.pojo.vo.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemBaseDetailVO;
import org.springframework.stereotype.Service;

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


    @Override
    public PageResult<MemBaseInfoVo> queryList(MemBaseInfoPageReq memBaseInfoPageReq) {
        return null;
    }

    @Override
    public int addMemBaseInfo(MemAddReq memAddReq) {
        return 0;
    }

    @Override
    public int editMemBaseInfo(MemEditReq memEditReq) {
        return 0;
    }

    @Override
    public MemBaseDetailVO getMemBaseInfo(Long uid) {
        return null;
    }

    @Override
    public int editFrozenStatus(MemEditFrozenStatusReq frozenStatusReq) {
        return 0;
    }
}
