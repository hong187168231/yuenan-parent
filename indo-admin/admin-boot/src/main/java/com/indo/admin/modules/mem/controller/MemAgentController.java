package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.admin.modules.mem.req.SubordinateReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员下级表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@RestController
@RequestMapping("/mem/agent")
public class MemAgentController {

    @Autowired
    private IMemAgentService memAgentService;

    @ApiOperation(value = "代理列表", httpMethod = "POST")
    @PostMapping(value = "/page")
    @AllowAccess
    public Result<PageResult<MemAgent>> page(@RequestBody MemAgentPageReq req) {
        return Result.success(memAgentService.getPage(req));
    }

    @ApiOperation(value = "下级列表", httpMethod = "POST")
    @PostMapping(value = "/subordinatePage")
    @AllowAccess
    public Result<PageResult<MemAgent>> subordinatePage(@RequestBody SubordinateReq req) {

        return Result.success(memAgentService.subordinatePage(req));
    }
}
