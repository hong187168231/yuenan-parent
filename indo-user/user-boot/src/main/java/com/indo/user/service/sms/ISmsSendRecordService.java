package com.indo.user.service.sms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.SmsSendRecord;
import com.indo.user.pojo.req.VerifyCodeReq;

/**
 * <p>
 * 短信发送记录表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-23
 */
public interface ISmsSendRecordService extends IService<SmsSendRecord> {

    Integer getLimit(SmsSendRecord smsSendRecord);

    Integer getCountDown(VerifyCodeReq req);

    boolean saveSmsSendRecord(SmsSendRecord smsSendRecord);

}
