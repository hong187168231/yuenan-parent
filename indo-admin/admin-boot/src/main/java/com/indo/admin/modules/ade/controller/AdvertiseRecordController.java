package com.indo.admin.modules.ade.controller;


import com.indo.admin.modules.ade.service.IAdvertiseRecordService;
import com.indo.admin.pojo.vo.AdvertiseRecordVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.AdvertiseQueryDTO;
import com.indo.user.pojo.dto.AdvertiseRecordDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 站内信 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-11-02
 */
@Api(tags = "广告管理")
@RestController
@RequestMapping("/api/v1/ade")
public class AdvertiseRecordController {


    @Autowired
    private IAdvertiseRecordService iAdvertiseRecordService;

    @ApiOperation(value = "分页查询广告")
    @GetMapping(value = "/list")
    public Result<List<AdvertiseRecordVO>> list(AdvertiseQueryDTO queryDTO) {
        return iAdvertiseRecordService.queryList(queryDTO);
    }

    @ApiOperation(value = "增加广告")
    @PostMapping(value = "/add")
    public Result add(@RequestBody AdvertiseRecordDTO advertiseRecordDTO) {
        return Result.judge(iAdvertiseRecordService.add(advertiseRecordDTO));
    }


    @ApiOperation(value = "编辑广告")
    @PostMapping(value = "/edit")
    public Result edit(AdvertiseRecordDTO advertiseRecordDTO) {
        return Result.judge(iAdvertiseRecordService.edit(advertiseRecordDTO));
    }

}
