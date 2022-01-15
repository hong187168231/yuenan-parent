package com.indo.user.controller;


import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.mem.AddBankCardReq;
import com.indo.user.pojo.vo.MemBankVo;
import com.indo.user.pojo.vo.PayBankVO;
import com.indo.user.service.AppMemBankService;
import com.indo.user.service.IPayBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户银行卡 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-17
 */
@Api(tags = "银行卡接口")
@RestController
@RequestMapping("/api/v1/users/bank")
public class MemBankRelationController {

    @Autowired
    private AppMemBankService memBankRelationService;

    @Resource
    private IPayBankService iPayBankService;

    @ApiOperation(value = "添加银行卡", httpMethod = "POST")
    @PostMapping(value = "/add")
    public Result addBankCard(@RequestBody AddBankCardReq req, @LoginUser LoginInfo loginUser) {
        boolean flag = memBankRelationService.addBankCard(req, loginUser);
        return Result.judge(flag);
    }

    @ApiOperation(value = "查询个人银行卡列表", httpMethod = "GET")
    @GetMapping(value = "/list")
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
