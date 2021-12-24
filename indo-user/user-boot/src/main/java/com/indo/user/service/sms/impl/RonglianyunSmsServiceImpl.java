package com.indo.user.service.sms.impl;

//import com.caipiao.live.common.config.SmsConfig;
//import com.caipiao.live.common.enums.StatusCode;
//import com.caipiao.live.common.enums.SysParameterEnum;
//import com.caipiao.live.common.exception.BusinessException;
//import com.caipiao.live.common.mybatis.entity.SysParameter;
//import com.caipiao.live.common.service.sys.SysParamService;
//import com.caipiao.live.common.util.StringUtils;
//import com.caipiao.live.common.util.sms.SmsSendResult;
//import com.cloopen.rest.sdk.BodyType;
//import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.indo.common.utils.sms.SmsSendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;


/**
 * 容联云发送短信服务类
 *
 * @author puff
 */
@Component
public class RonglianyunSmsServiceImpl {


    public void sendYtxSms(SmsSendResult sendResult) {
        sendSms(sendResult);
    }

    /**
     * 容联云发送短信
     *
     * @param sendResult 短信发送参数及结果
     */
    private void sendSms(SmsSendResult sendResult) {
//        try {
//            HashMap<String, Object> result = null;
//            CCPRestSmsSDK restApi = new CCPRestSmsSDK();
//            restApi.init("app.cloopen.com", "8883");
//            restApi.setAccount(getKey(SMS_RONGLIANYUN_ACCOUNTSID), getKey(SMS_RONGLIANYUN_ACCOUNTTOKEN));
//            restApi.setAppId(getKey(SMS_RONGLIANYUN_APPID));
//            restApi.setBodyType(BodyType.Type_JSON);
//
//            SysParameter bsYanzheng = this.sysParamService.getByCode(SysParameterEnum.SMS_VALIDATE.name());
//            if (bsYanzheng == null || StringUtils.isEmpty(bsYanzheng.getSysparamvalue())) {
//                throw new BusinessException(StatusCode.LIVE_ERROR_103.getCode(), "系统参数(sms_validate)异常");
//            }
//            String smsTime = bsYanzheng.getSysparamvalue();
//            result = restApi.sendTemplateSMS(sendResult.getPhone(), template, new String[]{sendResult.getCaptchaCode(), smsTime + "分钟"});
//            if (RLY_SUCCESS.equals(result.get(RLY_STATUS_CODE))) {
//                sendResult.setSuccess(true);
//                sendResult.setBizId("ronglianyun");
//                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
//                Set<String> keySet = data.keySet();
//                for (String key : keySet) {
//                    Object object = data.get(key);
//                    logger.info(key + " = " + object);
//                }
//            } else {
//                logger.error("容联云发送短信失败。手机号为:{},{}", sendResult.getPhone(),
//                        "错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
//            }
//        } catch (Exception e) {
//            logger.error("容联云发送短信出错。手机号为:{},{}", sendResult.getPhone(), e);
//        }

    }


}
