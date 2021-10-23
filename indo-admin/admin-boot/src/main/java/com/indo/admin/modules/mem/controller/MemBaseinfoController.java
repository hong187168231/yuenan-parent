package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.indo.user.pojo.req.mem.MemAddReq;
import com.indo.user.pojo.req.mem.MemBaseInfoPageReq;
import com.indo.user.pojo.req.mem.MemEditReq;
import com.indo.user.pojo.vo.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemBaseDetailVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 会员基础信息表 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
@RestController
@RequestMapping("/api/v1/memBaseinfo")
public class MemBaseinfoController {

    @Resource
    private IMemBaseinfoService memBaseinfoService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/listByPage")
    public Result<PageResult<MemBaseInfoVo>> getMemBaseInfo(@RequestBody MemBaseInfoPageReq baseInfoPageReq) {
        PageResult<MemBaseInfoVo> result = memBaseinfoService.queryList(baseInfoPageReq);
        return Result.success(result);
    }

    @ApiOperation(value = "新增会员信息")
    @PostMapping(value = "/add")
    public Result addMemBaseInfo(@Validated MemAddReq memAddReq) {
        int count = memBaseinfoService.addMemBaseInfo(memAddReq);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }


    @ApiOperation(value = "编辑会员信息")
    @PostMapping(value = "/edit")
    public Result editMemBaseInfo(@Validated MemEditReq memEditReq) {
        int count = memBaseinfoService.editMemBaseInfo(memEditReq);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "查询会员详情")
    @GetMapping(value = "/listByPage")
    public Result<MemBaseDetailVO> getMemBaseInfo(@ApiParam("会员uid") @RequestParam("uid") Long uid) {
        MemBaseDetailVO detailVO = memBaseinfoService.getMemBaseInfo(uid);
        return Result.success(detailVO);
    }
}
