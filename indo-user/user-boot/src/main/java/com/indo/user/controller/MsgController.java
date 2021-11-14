package com.indo.user.controller;

import com.indo.admin.api.MsgFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

@Api(tags = "消息接口")
@RestController
@RequestMapping("/api/v1/msg")
@Slf4j
@AllArgsConstructor
public class MsgController {

//    @Resource
//    private MsgFeignClient msgFeignClient;
//
//    @ApiOperation(value = "个人消息接口", httpMethod = "GET")
//    @PostMapping(value = "/personal")
//    @AllowAccess
//    public Result<PageResult<MsgStationLetter>> loginDo(@RequestBody MsgDTO msgDTO) {
//        Result result = msgFeignClient.getPersonalMsg(msgDTO);
//        if (Result.success().getCode().equals(result.getCode())) {
//            PageResult<MsgStationLetter> data = (PageResult<MsgStationLetter>) result.getData();
//            return Result.success(data);
//        } else {
//            throw new BizException("远程调用异常");
//        }
//    }
//
//
//    @ApiOperation(value = "系统消息接口", httpMethod = "GET")
//    @PostMapping(value = "/sys")
//    @AllowAccess
//    public Result<PageResult<MsgPushRecord>> register(@RequestBody MsgDTO msgDTO) {
//        Result result = msgFeignClient.getSysMsg(msgDTO);
//        if (Result.success().getCode().equals(result.getCode())) {
//            PageResult<MsgPushRecord> data = (PageResult<MsgPushRecord>) result.getData();
//            return Result.success(data);
//        } else {
//            throw new BizException("远程调用异常");
//        }
//    }


}
