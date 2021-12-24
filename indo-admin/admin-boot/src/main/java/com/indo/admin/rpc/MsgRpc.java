package com.indo.admin.rpc;


import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.entity.MsgPushRecord;
import com.indo.admin.pojo.entity.MsgStationLetter;
import com.indo.common.result.PageResult;
import com.indo.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 系统参数 rpc控制器
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/rpc/msg")
public class MsgRpc {


    @Resource
    private IMsgStationLetterService iMsgStationLetterService;

    @Resource
    private IMsgPushRecordService iMsgPushRecordService;

    /**
     * 根据参数code 获取系统参数
     *
     * @param msgDTO
     * @return
     */
    @PostMapping("/personal")
    public Result<List<MsgStationLetter>> getPersonalMsg(@RequestBody MsgDTO msgDTO) {
        List<MsgStationLetter> pageResult = iMsgStationLetterService.getPersonalMsg(msgDTO);
        return Result.success(pageResult);
    }


    /**
     * 根据参数code 获取系统参数
     *
     * @return
     */
    @PostMapping("/sys")
    public Result<List<MsgPushRecord>> getSysMsg(@RequestBody MsgDTO msgDTO) {
        return Result.success(iMsgPushRecordService.getSysMsg(msgDTO));
    }

}    
    