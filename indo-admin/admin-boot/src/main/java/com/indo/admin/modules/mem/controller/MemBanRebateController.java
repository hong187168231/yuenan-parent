package com.indo.admin.modules.mem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemBanRebateService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemBanRebate;
import com.indo.user.pojo.dto.MemBanRebateDto;
import com.indo.user.pojo.vo.MemBanRebateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 禁止返点表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-08-30
 */
@Api(tags = "禁止返点接口")
@RestController
@RequestMapping("/api/v1/memBanRebate")
public class MemBanRebateController {


    @Resource
    private IMemBanRebateService memBanRebateService;


    @ApiOperation(value = "查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "levelId", value = "用户等级", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long",required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long",required = true)
    })
    @GetMapping(value = "/list")
    public Result<PageResult<MemBanRebateVo>> getBanRebates(MemBanRebateDto dto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != dto.getPage() && null != dto.getLimit()) {
            pageNum = dto.getPage();
            pageSize = dto.getLimit();
        }
        Page<MemBanRebateVo> p =  new Page<>(pageNum, pageSize);
        List<MemBanRebateVo> list = memBanRebateService.selectMemBanRebate(p,dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)", required = true)
    @GetMapping(value = "/export")
    public void excelExport(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
        memBanRebateService.excelExport(response,ids);
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Result create(@Validated @RequestBody MemBanRebateDto memBanRebateDTO) {
        memBanRebateService.saveAccounts(memBanRebateDTO.getAccounts(), memBanRebateDTO.getRemark());
        return Result.success(HttpStatus.CREATED);
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Result update(@Validated @RequestBody MemBanRebate memBanRebate) {
        memBanRebate.setUpdateTime(new Date());
        memBanRebateService.saveOrUpdate(memBanRebate);
        return Result.success(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Result delete(@Validated @RequestBody List<Long> ids) {
        memBanRebateService.removeByIds(ids);
        return Result.success(HttpStatus.OK);
    }

}
