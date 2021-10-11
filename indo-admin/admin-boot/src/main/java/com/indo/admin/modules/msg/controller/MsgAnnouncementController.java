package com.indo.admin.modules.msg.controller;


import com.indo.admin.modules.msg.service.IMsgPlatformAnnouncementService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.common.result.Result;
import com.indo.user.pojo.dto.MsgPlatformAnnouncementDTO;
import com.indo.user.pojo.vo.MsgPlatformAnnouncementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 平台公告表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-06
 */
@Api(tags = "平台公告接口")
@RestController
@RequestMapping("/api/v1/announcement")
@Slf4j
@AllArgsConstructor
public class MsgAnnouncementController {

    @Autowired
    private IMsgPlatformAnnouncementService announcementService;

    @ApiOperation(value = "分页查询平台公告信息")
    @GetMapping(value = "/list")
    public Result list(MsgPlatformAnnouncementDTO announcementDTO){
        PageResult<MsgPlatformAnnouncementVO> result = announcementService.queryList(announcementDTO);
        return Result.success(result);
    }

    @ApiOperation(value = "查询单个平台公告信息")
    @GetMapping(value = "/queryById/{id}")
    public Result getAnnouncement(@PathVariable(value = "id") Long id) {
        MsgPlatformAnnouncementVO announcementVO = announcementService.queryInfo(id);
        return Result.success(announcementVO);
    }

    @ApiOperation(value = "增加平台公告信息")
    @PostMapping(value = "/add")
    public Result addAnnouncement(@Validated MsgPlatformAnnouncementDTO announcementDTO) {
        int count = announcementService.addInfo(announcementDTO);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "更改平台公告信息")
    @PostMapping(value = "/update")
    public Result updateAnnouncement(MsgPlatformAnnouncementDTO announcementDTO) {
        int count = announcementService.updateInfo(announcementDTO);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "删除平台公告信息")
    @DeleteMapping(value = "/delete")
    public Result deleteBrand(@RequestParam("ids") List<Long> ids) {
        int count = announcementService.deleteInfo(ids);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
}
