package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.modules.mem.vo.MemBaseDetailVO;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.admin.modules.mem.req.MemAddReq;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.req.MemEditStatusReq;
import com.indo.admin.modules.mem.req.MemEditReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员基础信息表 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
@Api(value = "用户controller", tags = {"会员基本信息接口"})
@RestController
@RequestMapping("/api/v1/memBaseinfo")
public class MemBaseinfoController {

    @Resource
    private IMemBaseinfoService memBaseinfoService;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/listByPage")
    public Result<List<MemBaseInfoVo>> getMemBaseInfo(@RequestBody MemBaseInfoPageReq baseInfoPageReq) {
        Page<MemBaseInfoVo> result = memBaseinfoService.queryList(baseInfoPageReq);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "新增会员信息")
    @PostMapping(value = "/add")
    public Result addMemBaseInfo(@RequestBody @Validated MemAddReq memAddReq) {
        memBaseinfoService.addMemBaseInfo(memAddReq);
        return Result.success();
    }


    @ApiOperation(value = "编辑会员信息")
    @PostMapping(value = "/edit")
    public Result editMemBaseInfo(@RequestBody @Validated MemEditReq memEditReq) {
        boolean flag = memBaseinfoService.editMemBaseInfo(memEditReq);
        return Result.judge(flag);

    }

    @ApiOperation(value = "更改会员状态")
    @PostMapping(value = "/editStatus")
    public Result editStatus(@RequestBody @Validated MemEditStatusReq frozenStatusReq) {
        boolean flag = memBaseinfoService.editStatus(frozenStatusReq);
        return Result.judge(flag);
    }

    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "用户ID", required = true, paramType = "path", dataType = "Long")
    })
    @GetMapping(value = "/resetPassword/{memId}")
    public Result resetPassword(@PathVariable Long memId) {
        boolean flag = memBaseinfoService.resetPassword(memId);
        return Result.judge(flag);
    }


    @ApiOperation(value = "修改会员等级")
    @PutMapping(value = "/updateMemLevel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "用户ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "memLevel", value = "用户等级", required = true, paramType = "query", dataType = "int")
    })
    public Result updateMemLevel(@RequestParam("memId") Long memId, @RequestParam("memLevel") Integer memLevel) {
        boolean flag = memBaseinfoService.updateMemLevel(memId, memLevel);
        return Result.judge(flag);

    }


    @ApiOperation(value = "查询会员详情")
    @GetMapping(value = "/getByAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户账号", required = true, paramType = "query", dataType = "String")
    })
    public Result<MemBaseDetailVO> getMemBaseInfo(@RequestParam("account") String account) {
        MemBaseDetailVO detailVO = memBaseinfoService.getMemBaseInfoByAccount(account);
        return Result.success(detailVO);
    }

}
