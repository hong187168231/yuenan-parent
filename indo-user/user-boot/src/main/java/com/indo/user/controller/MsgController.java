package com.indo.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.api.MsgFeignClient;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.admin.pojo.vo.msg.MsgTotalVO;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.user.pojo.dto.MsgPushRecordDto;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import com.indo.user.service.IMsgStatusRecordService;
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

    @Resource
    private IMsgStatusRecordService msgStatusRecordService;

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
            @ApiImplicitParam(name = "deviceType", value = "设备类型:1ios,2android ", paramType = "query", dataType = "int", required = true)
    })
    @GetMapping(value = "/sys")
    public Result<Page<MsgPushRecordVO>> register(@RequestParam MsgPushRecordDto msgPushRecordDto, @LoginUser LoginInfo loginInfo) {
      return Result.success(msgStatusRecordService.findSysMsgInfoPage(msgPushRecordDto,loginInfo));
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
    @ApiOperation(value = "删除消息接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgType", value = "消息类型 1个人消息，2系统消息 ", dataType = "int", required = true),
            @ApiImplicitParam(name = "msgId", value = "消息id ", dataType = "Long", required = true)
    })
    @GetMapping(value = "/delete")
    public Result<List<MsgPushRecordVO>> delete(@RequestParam("msgType") Integer msgType,@RequestParam("msgId") Long msgId,@LoginUser LoginInfo loginInfo) {
        if(msgType.equals(2)){
            msgStatusRecordService.insertMsgStatusRecord(msgId,msgType,loginInfo);
            return Result.success();
        }else {
            MsgDTO dto = new MsgDTO();
            dto.setMsgId(msgId);
            dto.setMsgType(msgType);
            Result result = msgFeignClient.deleteMsg(dto);
            if (Result.success().getCode().equals(result.getCode())) {
                return Result.success();
            } else {
                throw new BizException("远程调用异常");
            }
        }
    }

}
