package com.indo.code.modules.sms.service.impl;

import com.indo.code.modules.sms.entity.SendRecord;
import com.indo.code.modules.sms.mapper.SendRecordMapper;
import com.indo.code.modules.sms.service.ISendRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SendRecordServiceImpl extends ServiceImpl<SendRecordMapper, SendRecord> implements ISendRecordService {

}
