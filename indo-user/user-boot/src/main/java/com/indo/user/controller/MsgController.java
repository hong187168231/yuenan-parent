package com.indo.user.controller;

import com.indo.admin.api.MsgFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgPushRecordVO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.admin.pojo.vo.msg.MsgTotalVO;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "消息接口")
@RestController
@RequestMapping("/api/v1/msg")
@Slf4j
@AllArgsConstructor
public class MsgController {

    @Resource
    private MsgFeignClient msgFeignClient;

    @ApiOperation(value = "个人消息接口", httpMethod = "GET")
    @GetMapping(value = "/personal")
    public Result<List<MsgStationLetterVO>> loginDo(@LoginUser LoginInfo loginInfo) {
        MsgDTO dto = new MsgDTO();
        dto.setMemId(loginInfo.getId());
        Result result = msgFeignClient.getPersonalMsg(dto);
        if (Result.success().getCode().equals(result.getCode())) {
            List<MsgStationLetterVO> data = (List<MsgStationLetterVO>) result.getData();
            return Result.success(data);
        } else {
            throw new BizException("远程调用异常");
        }
    }


    @ApiOperation(value = "系统消息接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型 ", paramType = "query", dataType = "int", required = true)
    })
    @GetMapping(value = "/sys")
    public Result<List<MsgPushRecordVO>> register(@RequestParam("deviceType") Integer deviceType) {
        MsgDTO dto = new MsgDTO();
        dto.setDeviceType(deviceType);
        Result result = msgFeignClient.getSysMsg(dto);
        if (Result.success().getCode().equals(result.getCode())) {
            List<MsgPushRecordVO> data = (List<MsgPushRecordVO>) result.getData();
            return Result.success(data);
        } else {
            throw new BizException("远程调用异常");
        }
    }


    @ApiOperation(value = "消息条数接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型 ", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "beginTime", value = "开始时间 ", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间 ", paramType = "query", dataType = "string", required = true)
    })
    @GetMapping(value = "/total")
    public Result<MsgTotalVO> msgTotal(@RequestParam("beginTime") String beginTime,
                                       @RequestParam("endTime") String endTime,
                                       @RequestParam("deviceType") Integer deviceType,
                                       @LoginUser LoginInfo loginInfo) {
        MsgDTO dto = new MsgDTO();
        dto.setDeviceType(deviceType);
        dto.setBeginTime(beginTime);
        dto.setEndTime(endTime);
        dto.setMemId(loginInfo.getId());
        Result result = msgFeignClient.msgTotal(dto);
        if (Result.success().getCode().equals(result.getCode())) {
            MsgTotalVO data = (MsgTotalVO) result.getData();
            return Result.success(data);
        } else {
            throw new BizException("远程调用异常");
        }
    }


}
