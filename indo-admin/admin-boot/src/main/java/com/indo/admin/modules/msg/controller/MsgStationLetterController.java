package com.indo.admin.modules.msg.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.vo.MsgStationLetterVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.dto.StationLetterAddDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 站内信 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Api(tags = "站内信接口")
@RestController
@RequestMapping("/api/v1/stationLetter")
@Slf4j
@AllArgsConstructor
public class MsgStationLetterController {

    @Autowired
    private IMsgStationLetterService stationLetterService;

    @ApiOperation(value = "分页查询站内信")
    @GetMapping(value = "/list")
    public Result<List<MsgStationLetterVO>> list(@RequestBody MsgStationLetterDTO letterDTO) {
        Page relust = stationLetterService.queryList(letterDTO);
        return Result.success(relust.getRecords(), relust.getTotal());
    }

    @ApiOperation(value = "增加站内信")
    @PostMapping(value = "/add")
    public Result add(@RequestBody StationLetterAddDTO letterDTO) {
        int count = stationLetterService.add(letterDTO);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "批量删除站内信")
    @DeleteMapping(value = "/delete")
    public Result deleteBrand(@RequestParam("ids") List<Long> ids) {
        Boolean flag = stationLetterService.removeByIds(ids);
        return Result.judge(flag);
    }
}
