package com.indo.admin.modules.mem.controller;


import com.indo.admin.modules.mem.req.MemRebateAddReq;
import com.indo.admin.modules.mem.service.IMemRebateService;
import com.indo.admin.modules.mem.vo.MemRebateVo;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
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
@Api(tags = "返点配置")
@RestController
@RequestMapping("/api/v1/mem/rebate")
public class MemRebateController {
    @Autowired
    private IMemRebateService memRebateService;

    @ApiOperation(value = "返点配置查询")
    @GetMapping(value = "query")
    public Result<MemRebateVo> list() {
        MemRebateVo result = memRebateService.queryMemRabate();
        return Result.success(result);
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/add")
    public Result add(@RequestBody MemRebateAddReq req) {
        memRebateService.saveOne(req);
        return Result.success();
    }


}
