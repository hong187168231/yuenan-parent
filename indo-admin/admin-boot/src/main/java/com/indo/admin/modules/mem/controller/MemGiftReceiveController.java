package com.indo.admin.modules.mem.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.service.IMemGiftReceiveService;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 礼金领取 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Api(value = "礼金controller", tags = {"礼金领取"})
@RestController
@RequestMapping("/api/v1/memGift")
public class MemGiftReceiveController {

    @Autowired
    private IMemGiftReceiveService iMemGiftReceiveService;

    @ApiOperation(value = "礼金领取记录")
    @GetMapping(value = "/list")
    public Result<List<MemGiftReceiveVO>> list(GiftReceiveDTO queryDto) {
        Page relust = iMemGiftReceiveService.receiveList(queryDto);
        return Result.success(relust.getRecords(), relust.getTotal());
    }
}
