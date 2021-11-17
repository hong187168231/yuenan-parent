package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBank;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.BankCardPageReq;
import com.indo.user.service.IMemBankRelationService;
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
 * @author xxx
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/mem/bank-relation")
public class MemBankRelationController {

    @Autowired
    private IMemBankRelationService memBankRelationService;

    @ApiOperation(value = "添加银行卡", httpMethod = "POST")
    @PostMapping(value = "/addbankCard")
    @AllowAccess
    public Result addbankCard(@RequestBody AddBankCardReq req) {
        memBankRelationService.addbankCard(req);
        return Result.success();
    }

    @ApiOperation(value = "查询个人银行卡列表", httpMethod = "POST")
    @PostMapping(value = "/findPage")
    @AllowAccess
    public Result<PageResult<MemBankRelation>> findPage(@RequestBody BankCardPageReq req) {
        return Result.success(memBankRelationService.findPage(req));
    }

    @ApiOperation(value = "查询所有银行", httpMethod = "POST")
    @PostMapping(value = "/findAllBank")
    @AllowAccess
    public Result<List<MemBank>> findAllBank() {
        return Result.success(memBankRelationService.findAllBank());
    }
}
