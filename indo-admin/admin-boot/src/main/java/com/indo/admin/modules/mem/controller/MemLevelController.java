package com.indo.admin.modules.mem.controller;

import com.indo.admin.modules.mem.req.MemLevelAddReq;
import com.indo.admin.modules.mem.req.MemLevelPageReq;
import com.indo.admin.modules.mem.req.MemLevelUpdateReq;
import com.indo.admin.modules.mem.service.IMemLevelService;
import com.indo.admin.modules.mem.vo.MemLevelVo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户等级表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-08-26
 */
@Api(tags = "用户等级接口")
@RestController
@RequestMapping("/api/v1/memLevel")
public class MemLevelController {


    @Resource
    private IMemLevelService memLevelService;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public Result<PageResult<MemLevelVo>> page(@RequestBody MemLevelPageReq req) {
        return Result.success(memLevelService.selectByPage(req));
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Result create(@Validated @RequestBody MemLevelAddReq req) {
        return Result.judge(memLevelService.saveOne(req));
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Result update(@Validated @RequestBody MemLevelUpdateReq req) {
        memLevelService.updateOne(req);
        return Result.success(HttpStatus.OK);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        memLevelService.removeById(id);
        return Result.success(HttpStatus.OK);
    }

}
