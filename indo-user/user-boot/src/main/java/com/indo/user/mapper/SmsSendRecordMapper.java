package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.admin.pojo.entity.SmsSendRecord;
import com.indo.user.pojo.req.VerifyCodeReq;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 短信发送记录表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-23
 */
@Mapper
public interface SmsSendRecordMapper extends BaseMapper<SmsSendRecord> {


    Integer getLimit(SmsSendRecord smsSendRecord);

    Integer getCountDown(VerifyCodeReq req);

}
