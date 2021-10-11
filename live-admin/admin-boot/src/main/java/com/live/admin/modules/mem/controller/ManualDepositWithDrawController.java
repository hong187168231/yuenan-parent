package com.live.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.mem.service.IManualDepositWithDrawService;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.user.pojo.dto.ManualDepositWithDrawDto;
import com.live.user.pojo.vo.ManualDepositWithDrawVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 人工存提 前端控制器
 * </p>
 *
 * @author boyd
 * @since 2021-09-07
 */
@Api(tags = "人工存提")
@RestController
@RequestMapping("/api/v1/manaul")
public class ManualDepositWithDrawController {

    @Resource
    private IManualDepositWithDrawService manualService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/list")
    public Result<PageResult<ManualDepositWithDrawVO>> getManualDepositWithDrawList(ManualDepositWithDrawDto dto) {
        Page<ManualDepositWithDrawVO> p =  new Page<>(dto.getPage(), dto.getLimit());
        List<ManualDepositWithDrawVO> list = manualService.queryList(p, dto);
        p.setRecords(list);
        return Result.success(PageResult.getPageResult(p));
    }

    @ApiOperation(value="新增人工存提")
    @PostMapping(value = "/insert")
    public Result insert(ManualDepositWithDrawDto dto){
        if (Objects.isNull(dto.getMemIds())) {
            return Result.failed("用户不能为空");
        }

        return manualService.insert(dto);
    }



}
