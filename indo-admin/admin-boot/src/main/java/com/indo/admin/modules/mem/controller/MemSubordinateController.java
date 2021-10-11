package com.indo.admin.modules.mem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemSubordinateService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.entity.MemSubordinate;
import com.indo.user.pojo.dto.MemSubordinateDto;
import com.indo.user.pojo.vo.MemSubordinateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
 * 用户邀请表 前端控制器
 * </p>
 *
 * @author xxx
 * @since 2021-08-30
 */
@Api(tags = "用户邀请接口")
@RestController
@RequestMapping("/api/v1/memSubordinate")
public class MemSubordinateController {


    @Resource
    private IMemSubordinateService memSubordinateService;


    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<MemSubordinateVo>> getBanRebates(MemSubordinateDto dto) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != dto.getPage() && null != dto.getLimit()) {
            pageNum = dto.getPage();
            pageSize = dto.getLimit();
        }
        Page<MemSubordinateVo> page =  new Page<>(pageNum, pageSize);
        List<MemSubordinateVo> memSubordinateVos = memSubordinateService.selectMemSubordinate(page, dto);
        page.setRecords(memSubordinateVos);
        return Result.success(PageResult.getPageResult(page));
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParam(name = "ids", value = "用户ID，逗号拼接(1,2)")
    @GetMapping(value = "/export")
    public void excelExport(HttpServletResponse response, @RequestParam(required = false) List<Long> ids) throws IOException {
        memSubordinateService.excelExport(response,ids);
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Result create(@Validated @RequestBody MemSubordinate memSubordinate) {
        memSubordinate.setCreateTime(new Date());
        memSubordinateService.save(memSubordinate);
        return Result.success(HttpStatus.CREATED);
    }

    @ApiOperation(value = "修改")
    @PutMapping(value = "/update")
    public Result update(@Validated @RequestBody MemSubordinate memSubordinate) {
        memSubordinate.setUpdateTime(new Date());
        memSubordinateService.saveOrUpdate(memSubordinate);
        return Result.success(HttpStatus.NO_CONTENT);
    }

}
