package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.req.MemGrantRebateReq;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;
import com.indo.admin.modules.mem.service.IMemPendingRebateService;
import com.indo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/mem/pending-rebate")
public class MemPendingRebateController {

    @Autowired
    private IMemPendingRebateService memPendingRebateService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public Result<List<MemPendingRebate>> getPage(@RequestBody MemPendingRebatePageReq req) {
        Page<MemPendingRebate> result = memPendingRebateService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "发放返点金额")
    @PostMapping(value = "/grantRebate")
    public Result grantRebate(@RequestBody MemGrantRebateReq req) {
        memPendingRebateService.grantRebate(req);
        return Result.success();
    }
}
