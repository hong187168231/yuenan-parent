package com.indo.admin.rpc;


import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgPushRecordVO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.admin.pojo.vo.msg.MsgTotalVO;
import com.indo.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public Result<List<MsgStationLetterVO>> getPersonalMsg(@RequestBody MsgDTO msgDTO) {
        List<MsgStationLetterVO> pageResult = iMsgStationLetterService.getPersonalMsg(msgDTO);
        return Result.success(pageResult);
    }


    /**
     * 根据参数code 获取系统参数
     *
     * @return
     */
    @PostMapping("/sys")
    public Result<List<MsgPushRecordVO>> getSysMsg(@RequestBody MsgDTO msgDTO) {
        return Result.success(iMsgPushRecordService.getSysMsg(msgDTO));
    }

    /**
     * 根据参数code 获取系统参数
     *
     * @return
     */
    @PostMapping("/total")
    public Result<MsgTotalVO> msgTotal(@RequestBody MsgDTO msgDTO) {
        Integer sysTotal = iMsgPushRecordService.sysMsgTotal(msgDTO);
        Integer personalTotal = iMsgStationLetterService.personalMsgTotal(msgDTO);
        MsgTotalVO totalVO = new MsgTotalVO();
        totalVO.setSysMsgTotal(sysTotal);
        totalVO.setPersonalMsgTotal(personalTotal);
        return Result.success(totalVO);
    }

    /**
     * 删除站内信或后台推送
     * @param msgDTO
     * @return
     */
    @PostMapping("/deleteMsg")
    public Result deleteMsg(@RequestBody MsgDTO msgDTO, HttpServletRequest request) {
        if(msgDTO.getMsgType().equals(1)){
            iMsgStationLetterService.deleteMsg(msgDTO,request);
        }else{
            iMsgPushRecordService.deleteMsg(msgDTO,request);
        }
        return Result.success();
    }

}
    