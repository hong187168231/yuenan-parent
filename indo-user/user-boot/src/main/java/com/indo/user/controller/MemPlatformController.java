package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.mem.MemPlatformReq;
import com.indo.user.pojo.vo.mem.MemPlatformRecordVo;
import com.indo.user.service.IMemPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "平台记录接口")
@RestController
@RequestMapping("/api/v1/platform")
@Slf4j
@AllArgsConstructor
public class MemPlatformController {

    @Autowired
    private IMemPlatformService memPlatformService;

    @ApiOperation(value = "平台记录", httpMethod = "POST")
    @PostMapping(value = "/record")
    @AllowAccess
    public Result<PageResult<MemPlatformRecordVo>> record(@RequestBody MemPlatformReq req) {
        return Result.success(memPlatformService.record(req));
    }
}
