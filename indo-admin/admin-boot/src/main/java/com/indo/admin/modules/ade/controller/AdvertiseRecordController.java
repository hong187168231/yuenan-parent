package com.indo.admin.modules.ade.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.ade.service.IAdvertiseRecordService;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.vo.AdvertiseRecordVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.AdvertiseRecordDTO;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.dto.StationLetterAddDTO;
import com.indo.user.pojo.vo.MsgStationLetterVO;
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
@RestController
@RequestMapping("/api/v1/ade/advertiseRecord")
public class AdvertiseRecordController {


    @Autowired
    private IAdvertiseRecordService iAdvertiseRecordService;

    @ApiOperation(value = "分页查询广告")
    @GetMapping(value = "/list")
    public Result<List<AdvertiseRecordVO>> list(AdvertiseRecordDTO advertiseRecordDTO) {
        return iAdvertiseRecordService.queryList(advertiseRecordDTO);
    }

    @ApiOperation(value = "增加广告")
    @PostMapping(value = "/add")
    public Result add(AdvertiseRecordDTO advertiseRecordDTO) {
        return Result.judge(iAdvertiseRecordService.add(advertiseRecordDTO));
    }


    @ApiOperation(value = "编辑广告")
    @PostMapping(value = "/edit")
    public Result edit(AdvertiseRecordDTO advertiseRecordDTO) {
        return Result.judge(iAdvertiseRecordService.edit(advertiseRecordDTO));
    }

}
