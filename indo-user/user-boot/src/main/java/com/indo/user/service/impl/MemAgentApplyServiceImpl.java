package com.indo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.user.mapper.MemAgentApplyMapper;
import com.indo.user.pojo.entity.MemAgentApply;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.service.IMemAgentApplyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Service
public class MemAgentApplyServiceImpl extends ServiceImpl<MemAgentApplyMapper, MemAgentApply> implements IMemAgentApplyService {


    @Override
    public void add(MemAgentApplyReq req) {

    }
}
