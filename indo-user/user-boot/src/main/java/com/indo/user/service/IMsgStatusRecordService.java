package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.entity.MsgStatusRecord;
import com.indo.user.pojo.dto.MsgPushRecordDto;
import com.indo.user.pojo.vo.MsgPushRecordVO;

/**
 * <p>
 * 消息状态表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-07-20
 */
public interface IMsgStatusRecordService extends IService<MsgStatusRecord> {
    /**
     * 查询用户有效系统消息分页
     * @return
     */
    Page<MsgPushRecordVO> findSysMsgInfoPage(MsgPushRecordDto msgPushRecordDto,LoginInfo loginInfo);

    /**
     * 询用户有效系统消息数量
     * @param msgPushRecordDto
     * @param loginInfo
     * @return
     */
    Integer findSysMsgInfoCount(MsgPushRecordDto msgPushRecordDto,LoginInfo loginInfo);

    /**
     * 添加消息状态-用户删除消息用
     * @param msgId
     * @param msgType
     * @param loginInfo
     */
    void insertMsgStatusRecord(Long msgId,Integer msgType ,LoginInfo loginInfo);
}
