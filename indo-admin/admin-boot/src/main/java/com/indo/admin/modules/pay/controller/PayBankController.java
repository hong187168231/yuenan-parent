package com.indo.admin.modules.pay.controller;


import com.indo.admin.modules.pay.service.IPayBankService;
import com.indo.admin.pojo.req.pay.PayBankAddReq;
import com.indo.admin.pojo.req.pay.PayBankQueryReq;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankDTO;
import com.indo.pay.pojo.vo.PayBankVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 支付银行表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Api(tags = "银行管理")
@RestController
@RequestMapping("/pay/bank")
public class PayBankController {

    @Autowired
    private IPayBankService iPayBankService;

    @ApiOperation(value = "支付银行管理列表")
    @GetMapping(value = "/list")
    public Result<List<PayBankVO>> list(PayBankQueryReq queryReq) {
        return iPayBankService.bankList(queryReq);
    }

    @ApiOperation(value = "增加支付银行")
    @PostMapping(value = "/add")
    public Result add(@RequestBody PayBankAddReq addReq) {
        boolean flag = iPayBankService.addBank(addReq);
        return Result.judge(flag);
    }

    @ApiOperation(value = "修改支付银行状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "支付银行状态 0 关闭  1 开启", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "bankId", value = "支付银行id", paramType = "query", dataType = "int", required = true)
    })
    @PutMapping(value = "/editStatus")
    public Result editStatus(Integer status, Long bankId) {
        boolean flag = iPayBankService.editStatus(status, bankId);
        return Result.judge(flag);
    }

    @ApiOperation(value = "删除支付银行")
    @ApiImplicitParam(name = "ids", value = "主键ID集合，以,分割拼接字符串", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable String ids) {
        boolean status = iPayBankService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.judge(status);
    }
}
