package com.indo.admin.modules.mem.controller;

import com.indo.admin.modules.mem.req.MemInviteCodeSwitchStatusReq;
import com.indo.admin.modules.mem.req.MeminviteCodePageReq;
import com.indo.admin.modules.mem.service.IMemInviteCodeService;
import com.indo.admin.modules.mem.vo.MemInviteCodeVo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员邀请码 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
@RestController
@RequestMapping("/api/v1/mem/invite-code")
public class MemInviteCodeController {

    @Autowired
    private IMemInviteCodeService memInviteCodeService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public Result<PageResult<MemInviteCodeVo>> page(@RequestBody MeminviteCodePageReq req) {
        PageResult<MemInviteCodeVo> result = memInviteCodeService.queryList(req);
        return Result.success(result);
    }

    @ApiOperation(value = "启用、禁用")
    @PostMapping(value = "/switchStatus")
    public Result switchStatus(@RequestBody MemInviteCodeSwitchStatusReq req) {
        memInviteCodeService.switchStatus(req);
        return Result.success();
    }
}
