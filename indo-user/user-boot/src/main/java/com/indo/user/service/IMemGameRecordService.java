package com.indo.user.service;

import com.indo.common.result.PageResult;
import com.indo.user.pojo.req.mem.MemGameRecordReq;
import com.indo.user.pojo.vo.MemGameRecordVo;

public interface IMemGameRecordService {
    PageResult<MemGameRecordVo> record(MemGameRecordReq req);

}
