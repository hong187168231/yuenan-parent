package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBank;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.req.mem.BankCardPageReq;
import com.indo.user.pojo.vo.MemBankVo;
import com.indo.user.pojo.vo.PayBankVO;
import com.indo.user.service.IMemBankRelationService;
import com.indo.user.service.IPayBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户绑定银行卡信息表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-11-17
 */
@Api(tags = "银行卡接口")
@RestController
@RequestMapping("/api/v1/users/bank")
public class MemBankRelationController {

    @Autowired
    private IMemBankRelationService memBankRelationService;

    @Resource
    private IPayBankService iPayBankService;

    @ApiOperation(value = "添加银行卡", httpMethod = "POST")
    @PostMapping(value = "/add")
    public Result addBankCard(@RequestBody AddBankCardReq req, @LoginUser LoginInfo loginUser) {
        memBankRelationService.addBankCard(req, loginUser);
        return Result.success();
    }

    @ApiOperation(value = "查询个人银行卡列表", httpMethod = "POST")
    @PostMapping(value = "/list")
    public Result<List<MemBankVo>> findPage(@LoginUser LoginInfo loginUser) {
        return Result.success(memBankRelationService.findPage(loginUser));
    }

    @ApiOperation(value = "会员可绑定银行列表")
    @AllowAccess
    @GetMapping("/bindList")
    public Result<List<PayBankVO>> bindList() {
        return Result.success(iPayBankService.bankList());
    }

}
