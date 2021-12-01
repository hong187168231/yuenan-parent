package com.indo.user.controller;

import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.service.IMemAgentApplyService;
import io.swagger.annotations.Api;
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
 * @since 2021-11-19
 */

@Api(tags = "代理申请")
@RestController
@RequestMapping("/mem/agentApply")
public class MemAgentApplyController {

    @Autowired
    private IMemAgentApplyService memAgentApplyService;

    @ApiOperation(value = "申请", httpMethod = "POST")
    @PostMapping(value = "/add")
    @AllowAccess
    public Result add(@RequestBody MemAgentApplyReq req) {
        memAgentApplyService.add(req);
        return Result.success();
    }
}
