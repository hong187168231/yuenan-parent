package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.mem.MemGameRecordReq;
import com.indo.user.pojo.vo.MemGameRecordVo;
import com.indo.user.service.IMemGameRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "游戏记录接口")
@RestController
@RequestMapping("/api/v1/game")
@Slf4j
@AllArgsConstructor
public class MemGameRecordController {

    @Autowired
    private IMemGameRecordService memGameRecordService;

    @ApiOperation(value = "游戏记录", httpMethod = "POST")
    @PostMapping(value = "/record")
    @AllowAccess
    public Result<PageResult<MemGameRecordVo>> record(@RequestBody MemGameRecordReq req) {
        return Result.success(memGameRecordService.record(req));
    }
}
