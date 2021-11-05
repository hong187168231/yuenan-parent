package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.service.IMemNoticeService;
import com.indo.common.result.Result;
import com.indo.admin.modules.mem.req.MemNoticeAddReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员站内信 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/api/v1/mem/notice")
public class MemNoticeController {

    @Autowired
    private IMemNoticeService noticeService;

    @ApiOperation(value = "新增站内信")
    @PostMapping(value = "/add")
    public Result add(@RequestBody @Validated MemNoticeAddReq req) {
        int count = noticeService.add(req);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
}
