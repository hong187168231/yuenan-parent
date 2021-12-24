package com.indo.user.service.impl;

import com.indo.common.result.PageResult;
import com.indo.user.pojo.req.mem.MemGameRecordReq;
import com.indo.user.pojo.vo.MemGameRecordVo;
import com.indo.user.service.IMemGameRecordService;
import org.springframework.stereotype.Service;

@Service
public class MemGameRecordServiceImpl implements IMemGameRecordService {
    @Override
    public PageResult<MemGameRecordVo> record(MemGameRecordReq req) {
        return null;
    }
}
