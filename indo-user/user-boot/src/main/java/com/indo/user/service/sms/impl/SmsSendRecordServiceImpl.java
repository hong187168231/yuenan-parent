package com.indo.user.service.sms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.pojo.entity.SmsSendRecord;
import com.indo.user.mapper.SmsSendRecordMapper;
import com.indo.user.pojo.req.VerifyCodeReq;
import com.indo.user.service.sms.ISmsSendRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信发送记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-23
 */
@Service
public class SmsSendRecordServiceImpl extends ServiceImpl<SmsSendRecordMapper, SmsSendRecord> implements ISmsSendRecordService {

    @Autowired
    private SmsSendRecordMapper smsSendRecordMapper;


    @Override
    public Integer getLimit(SmsSendRecord smsSendRecord) {
        return smsSendRecordMapper.getLimit(smsSendRecord);
    }

    @Override
    public Integer getCountDown(VerifyCodeReq req) {
        return smsSendRecordMapper.getCountDown(req);
    }

    @Override
    public boolean saveSmsSendRecord(SmsSendRecord smsSendRecord) {
        return this.baseMapper.insert(smsSendRecord) > 0;
    }


}
