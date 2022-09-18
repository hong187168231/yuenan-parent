package com.indo.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.entity.LoanRecord;
import com.indo.core.service.ILoanRecordService;
import com.indo.user.pojo.dto.MsgPushRecordDto;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2022-07-28
 */
@Api(tags = "借款相关")
@RestController
@RequestMapping("/api/v1/loanRecord")
public class LoanRecordController {
    @Resource
    private ILoanRecordService loanRecordService;

    @ApiOperation(value = "查询用户借款记录分页", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long")
    })
    @GetMapping(value = "/findLoanRecordPage")
    public Result<Page<LoanRecord>> findLoanRecordPage(LoanRecordDTO loanRecordDTO, @LoginUser LoginInfo loginInfo) {
        return Result.success(loanRecordService.findLoanRecordPageByMemId(loanRecordDTO,loginInfo));
    }

    @ApiOperation(value = "借款", httpMethod = "GET")
    @GetMapping(value = "/loanMoney")
    @ApiImplicitParam(name = "amount", value = "借款金额", dataType = "Decimal")
    public Result findLoanRecordPage(@RequestParam BigDecimal amount, @LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        loanRecordService.loanMoney(amount,loginInfo,request);
        return Result.success();
    }
    @ApiOperation(value = "主动还款", httpMethod = "GET")
    @GetMapping(value = "/activeBackMoney")
    @ApiImplicitParam(name = "amount", value = "还款金额", dataType = "Decimal")
    public Result activeBackMoney(@RequestParam BigDecimal amount,@LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        loanRecordService.activeBackMoney(amount,loginInfo,request);
        return Result.success();
    }
    @ApiOperation(value = "查询用户借款相关信息", httpMethod = "GET")
    @GetMapping(value = "/findMemLoanInfo")
    public Result findMemLoanInfo(@LoginUser LoginInfo loginInfo, HttpServletRequest request) {
        return Result.success(loanRecordService.findMemLoanInfo(loginInfo,request));
    }
}
