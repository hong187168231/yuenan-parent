package com.indo.admin.modules.mem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IWithdrawService;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.WithdrawDto;
import com.indo.user.pojo.vo.WithdrawVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 提现表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-08-30
 */
@Api(tags = "提现接口")
@RestController
@RequestMapping("/api/v1/withdrawal")
public class WithdrawController {


    @Resource
    private IWithdrawService withdrawalService;


    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<WithdrawVo>> getBanRebates(WithdrawDto dto) {
        Page<WithdrawVo> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<WithdrawVo> list = withdrawalService.queryList(p,dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
        withdrawalService.export(response,ids);
    }

    @ApiOperation(value = "提现申请查询")
    @GetMapping(value = "/list/apply")
    public Result<PageResult<WithdrawVo>> getApplyWithDraw(WithdrawDto dto) {
        Page<WithdrawVo> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<WithdrawVo> list = withdrawalService.queryApplyList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

}
