package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemGiftReceiveService;
import com.indo.admin.modules.mem.service.IMemTradingService;
import com.indo.admin.pojo.dto.CapitalDTO;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.admin.pojo.vo.mem.MemTradingVO;
import com.indo.common.enums.ChangeCategoryEnum;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 交易明细 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Api(value = "资金明细controller", tags = {"资金明细"})
@RestController
@RequestMapping("/api/v1/capital")
public class MemTradingController {

    @Autowired
    private IMemTradingService iMemTradingService;

    @ApiOperation(value = "资金明细")
    @GetMapping(value = "/list")
    public Result<List<MemTradingVO>> list(CapitalDTO queryDto) {
        Page relust = iMemTradingService.capitalList(queryDto);
        return Result.success(relust.getRecords(), relust.getTotal());
    }

    @ApiOperation(value = "交易类型列表")
    @GetMapping(value = "/changeList")
    public Result<List<Map>> changeList() {
        return Result.success(ChangeCategoryEnum.toList());
    }
}
