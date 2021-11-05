package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.req.MemRebateAddReq;
import com.indo.admin.modules.mem.service.IMemRebateService;
import com.indo.admin.modules.mem.vo.MemRebateVo;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 返点配置表 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
@RestController
@RequestMapping("/api/v1/mem/rebate")
public class MemRebateController {

    @Autowired
    private IMemRebateService memRebateService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/list")
    public Result<List<MemRebateVo>> list() {
        List<MemRebateVo> result = memRebateService.queryAll();
        return Result.success(result);
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Result add(@RequestBody MemRebateAddReq req) {
        memRebateService.saveOne(req);
        return Result.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result add(@PathVariable Integer id) {
        memRebateService.deleteOne(id);
        return Result.success();
    }
}
