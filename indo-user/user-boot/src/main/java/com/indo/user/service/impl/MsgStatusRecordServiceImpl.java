package com.indo.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.pojo.entity.MsgStatusRecord;
import com.indo.user.mapper.MsgStatusRecordMapper;
import com.indo.user.pojo.dto.MsgPushRecordDto;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import com.indo.user.service.IMsgStatusRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 消息状态表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-07-20
 */
@Service
public class MsgStatusRecordServiceImpl extends ServiceImpl<MsgStatusRecordMapper, MsgStatusRecord> implements IMsgStatusRecordService {

    @Override
    public Page<MsgPushRecordVO> findSysMsgInfoPage(MsgPushRecordDto msgPushRecordDto, LoginInfo loginInfo) {
        Page<MsgPushRecordVO> page = new Page<>(msgPushRecordDto.getPage(), msgPushRecordDto.getLimit());
        return baseMapper.findSysMsgInfo(page,loginInfo.getId(),msgPushRecordDto.getDeviceType());
    }

    @Override
    public Integer findSysMsgInfoCount(MsgPushRecordDto msgPushRecordDto, LoginInfo loginInfo) {
        return baseMapper.findSysMsgInfoCount(loginInfo.getId(),msgPushRecordDto.getDeviceType());
    }

    @Override
    public void insertMsgStatusRecord(Long msgId, Integer msgType, LoginInfo loginInfo) {
        MsgStatusRecord msgStatusRecord = new MsgStatusRecord();
        msgStatusRecord.setMsgId(msgId);
        if(msgType.equals(2)){
            msgStatusRecord.setMsgType(msgType);
        }
        msgStatusRecord.setMemId(loginInfo.getId());
        msgStatusRecord.setStates(1);
        msgStatusRecord.setCreateTime(new Date());
        baseMapper.insert(msgStatusRecord);
    }
}
