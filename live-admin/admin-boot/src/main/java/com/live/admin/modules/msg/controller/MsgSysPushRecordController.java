package com.live.admin.modules.msg.controller;


import com.live.admin.modules.msg.service.IMsgPushRecordService;
import com.live.common.mybatis.base.PageResult;
import com.live.common.result.Result;
import com.live.user.pojo.dto.MsgPushRecordDTO;
import com.live.user.pojo.entity.MsgPushRecord;
import com.live.user.pojo.vo.MsgPushRecordVO;
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
    public Result list(MsgPushRecordDTO pushRecordDTO){
        PageResult<MsgPushRecordVO> result = pushRecordService.queryList(pushRecordDTO);
        return Result.success(result);
    }

    @ApiOperation(value = "增加后台推送信息记录")
    @PostMapping(value = "/add")
    public Result add(@Validated MsgPushRecord msgPushRecord) {
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
