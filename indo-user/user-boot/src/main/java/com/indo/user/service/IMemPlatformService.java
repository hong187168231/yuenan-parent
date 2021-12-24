package com.indo.user.service;

import com.indo.common.result.PageResult;
import com.indo.user.pojo.req.mem.MemPlatformReq;
import com.indo.user.pojo.vo.mem.MemPlatformRecordVo;

public interface IMemPlatformService {
    PageResult<MemPlatformRecordVo> record(MemPlatformReq req);
}
