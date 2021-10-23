package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.MemBaseInfoPageReq;
import com.indo.user.pojo.vo.MemBaseInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
