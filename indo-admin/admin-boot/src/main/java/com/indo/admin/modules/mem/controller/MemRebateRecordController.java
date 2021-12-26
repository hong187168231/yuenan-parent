package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemRebateRecord;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import com.indo.admin.modules.mem.service.IMemRebateRecordService;
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
 *  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/mem/rebate-record")
public class MemRebateRecordController {


    @Autowired
    private IMemRebateRecordService memRebateRecordService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public Result<List<MemRebateRecord>> getPage(@RequestBody MemRebateRecordPageReq req) {
        Page<MemRebateRecord> result = memRebateRecordService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }
}
