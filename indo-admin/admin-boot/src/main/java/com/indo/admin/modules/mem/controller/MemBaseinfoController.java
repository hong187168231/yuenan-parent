package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.pojo.dto.MemBetInfoDTO;
import com.indo.admin.pojo.vo.mem.MemBaseDetailVO;
import com.indo.admin.pojo.vo.mem.MemBetInfoVo;
import com.indo.common.result.Result;
import com.indo.admin.pojo.req.mem.MemAddReq;
import com.indo.admin.pojo.req.mem.MemBaseInfoReq;
import com.indo.admin.pojo.req.mem.MemEditStatusReq;
import com.indo.admin.pojo.req.mem.MemEditReq;
import com.indo.admin.pojo.vo.mem.MemBaseInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public Result<List<MemBaseInfoVo>> getMemBaseInfo(@RequestBody MemBaseInfoReq baseInfoPageReq, HttpServletRequest request) {
        Page<MemBaseInfoVo> result = memBaseinfoService.queryList(baseInfoPageReq,request);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "新增会员信息")
    @PostMapping(value = "/add")
    public Result addMemBaseInfo(@RequestBody @Validated MemAddReq memAddReq,HttpServletRequest request) {
        memBaseinfoService.addMemBaseInfo(memAddReq,request);
        return Result.success();
    }


    @ApiOperation(value = "编辑会员信息")
    @PostMapping(value = "/edit")
    public Result editMemBaseInfo(@RequestBody @Validated MemEditReq memEditReq,HttpServletRequest request) {
        boolean flag = memBaseinfoService.editMemBaseInfo(memEditReq,request);
        return Result.judge(flag);

    }

    @ApiOperation(value = "更改会员状态")
    @PostMapping(value = "/editStatus")
    public Result editStatus(@RequestBody @Validated MemEditStatusReq frozenStatusReq,HttpServletRequest request) {
        boolean flag = memBaseinfoService.editStatus(frozenStatusReq,request);
        return Result.judge(flag);
    }

    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "用户ID", required = true, paramType = "path", dataType = "Long")
    })
    @GetMapping(value = "/resetPassword/{memId}")
    public Result resetPassword(@PathVariable Long memId,HttpServletRequest request) {
        boolean flag = memBaseinfoService.resetPassword(memId,request);
        return Result.judge(flag);
    }


    @ApiOperation(value = "修改会员等级")
    @PutMapping(value = "/updateMemLevel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "用户ID", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "memLevel", value = "用户等级", required = true, paramType = "query", dataType = "int")
    })
    public Result updateMemLevel(@RequestParam("memId") Long memId, @RequestParam("memLevel") Integer memLevel,HttpServletRequest request) {
        boolean flag = memBaseinfoService.updateMemLevel(memId, memLevel,request);
        return Result.judge(flag);

    }


    @ApiOperation(value = "查询会员详情")
    @GetMapping(value = "/getByAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户账号", required = true, paramType = "query", dataType = "String")
    })
    public Result<MemBaseDetailVO> getMemBaseInfo(@RequestParam("account") String account,HttpServletRequest request) {
        MemBaseDetailVO detailVO = memBaseinfoService.getMemBaseInfoByAccount(account,request);
        return Result.success(detailVO);
    }
    @ApiOperation(value = "查询重复IP用户信息")
    @PostMapping(value = "/findIpRepeatPage")
    public Result findIpRepeatPage(@RequestBody MemBaseInfoReq baseInfoPageReq) {
        return Result.success(memBaseinfoService.findIpRepeatPage(baseInfoPageReq));
    }

    @ApiOperation(value = "会员打码量信息查询")
    @GetMapping(value = "/findMemBetInfoPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "memAccount", value = "会员账号", required = false, paramType = "query", dataType = "String")
    })
    public Result<Page<MemBetInfoVo>> findMemBetInfoPage(MemBetInfoDTO memBetInfoDTO) {
        return Result.success(memBaseinfoService.findMemBetInfoPage(memBetInfoDTO));
    }
}
