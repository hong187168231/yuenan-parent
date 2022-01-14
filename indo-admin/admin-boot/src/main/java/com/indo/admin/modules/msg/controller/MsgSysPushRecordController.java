package com.indo.admin.modules.msg.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.pojo.vo.MsgPushRecordVO;
import com.indo.common.result.Result;
import com.indo.user.pojo.req.msg.PushRecordAddReq;
import com.indo.user.pojo.req.msg.PushRecordQueryReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台推送记录表 前端控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Api(tags = "后台推送接口")
@RestController
@RequestMapping("/api/v1/pushRecord")
@Slf4j
@AllArgsConstructor
public class MsgSysPushRecordController {

    @Autowired
    private IMsgPushRecordService pushRecordService;

    @ApiOperation(value = "分页查询后台推送信息")
    @GetMapping(value = "/list")
    public Result<List<MsgPushRecordVO>> list(PushRecordQueryReq queryDTO) {
        Page result = pushRecordService.queryList(queryDTO);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "增加后台推送信息记录")
    @PostMapping(value = "/add")
    public Result add(PushRecordAddReq msgPushRecord) {
        pushRecordService.add(msgPushRecord);
        return Result.success();
    }

    @ApiOperation(value = "批量删除推送信息记录")
    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam("ids") List<Long> ids) {
        Boolean flag = pushRecordService.removeByIds(ids);
        if (flag) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
}
