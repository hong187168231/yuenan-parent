package com.indo.user.service.sms.impl;

import com.indo.common.enums.SmsChannelEnum;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.utils.sms.SmsSendResult;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.SysParameter;
import com.indo.user.common.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 短信发送公共服务类
 *
 * @author puff
 */
@Service
@Slf4j
public class SmsSendTemplate {

//    @Resource
//    private ISysParameterService sysParamService;

    @Resource
    private RonglianyunSmsServiceImpl sendYtxService;


    /**
     * 发送短信 根据对应配置的渠道发送短信
     *
     * @param phone       手机号
     * @param captchaCode 验证码
     * @param captchaType 验证码类型
     * @return
     */
    public SmsSendResult sendSms(String phone, String captchaCode, int captchaType, HttpServletRequest request) {
        if (realFlag()) {
            return SmsSendResult.getResult(true);
        }
        SmsSendResult result = new SmsSendResult(phone, captchaType, captchaCode);
        List<String> smsParam = SmsChannelEnum.getAll();
        List<SysParameter> sysList = getSmsParamList(smsParam, request);
        SysParameter sendSms = null;
        Collections.sort(sysList, Comparator.comparing(SysParameter::getSortBy));

        for (SysParameter sysParameter : sysList) {
            sendSms = sysParameter;
            SmsChannelEnum smsEnum = SmsChannelEnum.valueOf(sendSms.getParamValue());

            switch (smsEnum) {

                case RONGLIANYUN:
                    sendYtxService.sendYtxSms(result);
                    break;

            }
            if (result.isSuccess()) {
                break;
            }
        }

        log.info("send sms by conf:{}; result:{}", sendSms.toString(), result);
        return result;
    }


    /**
     * 获取所有短信渠道
     *
     * @param list
     * @return smsParamList
     */
    private List<SysParameter> getSmsParamList(List<String> list, HttpServletRequest request) {
        List<SysParameter> smsParamList = new LinkedList<>();
        for (String s : list) {
//            SysParameter shortmsg = this.sysParamService.getByCode(s);
//            if (null != shortmsg || !StringUtils.isEmpty(shortmsg.getParamValue())) {
//                smsParamList.add(shortmsg);
//            }
        }
        List<SysParameter> filterList = smsParamList.stream().filter(s -> UserConstants.OPEN.equals(s.getStatus())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterList)) {
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get("u170036", countryCode));
        }
        return filterList;
    }


    /**
     * 是否真实发送短信，用于测试的时候
     *
     * @return
     */
    public boolean realFlag() {
//        SysParameter bsReal = sysParamService.getByCode(SysParameterEnum.SMS_REAL_SEND.name());
//        if (UserConstants.OPEN.equals(bsReal.getStatus()) && UserConstants.SMS_REAL_OFF.equals(bsReal.getParamValue())) {
//            return true;
//        }
        return false;
    }

}
