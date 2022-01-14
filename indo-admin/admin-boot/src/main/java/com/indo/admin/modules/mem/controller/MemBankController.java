package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.mem.MemBankPageReq;
import com.indo.admin.pojo.req.mem.MemBankSwitchReq;
import com.indo.admin.modules.mem.service.IMemBankService;
import com.indo.admin.pojo.vo.mem.MemBankVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 前端控制器
 * </p>
 *
 * @author kevin
 * @since 2021-11-06
 */
@Api(tags = "会员银行卡")
@RestController
@RequestMapping("/api/v1/mem/bank-relation")
public class MemBankController {

    @Autowired
    private IMemBankService memBankRelationService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public Result<List<MemBankVO>> page(@RequestBody MemBankPageReq req) {
        Page<MemBankVO> result = memBankRelationService.queryList(req);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "启用、禁用")
    @PostMapping(value = "/switchStatus")
    public Result switchStatus(@RequestBody MemBankSwitchReq req) {
        memBankRelationService.switchStatus(req);
        return Result.success();
    }
}
