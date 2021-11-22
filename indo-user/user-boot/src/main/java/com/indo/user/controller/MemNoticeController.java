package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemNotice;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.MemNoticePageReq;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.IMemNoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员站内信 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/mem/notice")
public class MemNoticeController {

    @Autowired
    private IMemNoticeService memNoticeService;

    @ApiOperation(value = "个人消息列表", httpMethod = "POST")
    @PostMapping(value = "/page")
    @AllowAccess
    public Result<PageResult<MemNotice>> page(@RequestBody MemNoticePageReq req) {
        return Result.success(memNoticeService.getPage(req));
    }
}
