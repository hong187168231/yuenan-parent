package com.indo.user.service.sms;

import com.indo.user.pojo.req.VerifyCodeReq;
import com.indo.user.pojo.vo.sms.SmsCodeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
public interface ISmsService {

    List<SmsCodeVo> smsList();

    Integer sendSmsCode(VerifyCodeReq req, HttpServletRequest request);

}
