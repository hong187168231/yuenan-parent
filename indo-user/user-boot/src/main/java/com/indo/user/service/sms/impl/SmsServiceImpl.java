package com.indo.user.service.sms.impl;

import com.indo.admin.pojo.entity.SmsSendRecord;
import com.indo.admin.pojo.entity.SysParameter;
import com.indo.common.enums.SysParameterEnum;
import com.indo.common.enums.VerifCodeTypeEnum;
import com.indo.common.utils.CommonFunction;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.sms.SmsSendResult;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.IPUtils;
import com.indo.user.common.constant.UserConstants;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.req.VerifyCodeReq;
import com.indo.user.pojo.vo.sms.SmsCodeVo;
import com.indo.user.service.ISysParameterService;
import com.indo.user.service.MemBaseInfoService;
import com.indo.user.service.sms.ISmsSendRecordService;
import com.indo.user.service.sms.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private ISysParameterService iSysParameterService;

    @Autowired
    private ISmsSendRecordService iSmsSendRecordService;

    @Autowired
    private MemBaseInfoService memBaseInfoService;

    @Autowired
    private SmsSendTemplate smsSendTemplate;

    @Override
    public List<SmsCodeVo> smsList() {
        SmsCodeVo smsCodeVo = new SmsCodeVo();
        smsCodeVo.setSmsCode("086");
        smsCodeVo.setCountry("中国");

        SmsCodeVo smsCodeVo2 = new SmsCodeVo();
        smsCodeVo2.setSmsCode("0855");
        smsCodeVo2.setCountry("柬埔寨");

        List<SmsCodeVo> list = new ArrayList<>();
        list.add(smsCodeVo);
        list.add(smsCodeVo2);
        return list;
    }

    @Override
    public Integer sendSmsCode(VerifyCodeReq req, HttpServletRequest request) {
        String ipAddress = IPUtils.getIpAddr(request);
        log.info("ip: {} ", ipAddress);
        SysParameter ipMsgLimit = iSysParameterService.getByCode(SysParameterEnum.IP_MSG_LIMIT.name());
        int iplimit = Integer.parseInt(ipMsgLimit.getParamValue());
        SmsSendRecord smsSendRecord = new SmsSendRecord();
        smsSendRecord.setIpAddress(ipAddress);
        Integer ipCount = iSmsSendRecordService.getLimit(smsSendRecord);
        if (ipCount >= iplimit) {
            log.info("ip: {} ,短信条数 {}", ipAddress, ipCount);
            throw new BizException("今天短信条数超过限制");
        }
        // 验证这个手机号是否超过限制
        smsSendRecord = new SmsSendRecord();
        smsSendRecord.setMobile(req.getPhone());
        Integer mobileCount = iSmsSendRecordService.getLimit(smsSendRecord);
        if (mobileCount >= iplimit) {
            log.info("mobile: {} ,短信条数 {}", req.getPhone(), mobileCount);
            throw new BizException("今天短信条数超过限制");
        }
        MemBaseinfo ml = memBaseInfoService.findByMobile(req.getPhone());
        // 验证这个手机号是否已经存在 如果是发送注册
        if (req.getVerifCodeTypeEnum().equals(VerifCodeTypeEnum.register)) {
            // 手机号 不存在 才 3注册；
            if (ml != null) {
                throw new BizException("该手机号已注册");
            }
        } else {
            if (ml == null) {
                throw new BizException("该手机号不存在");
            }
            if (ml.getStatus() != UserConstants.ACCOUNT_NORMAL) {
                throw new BizException("非法账号");
            }
        }

        // 验证这个手机号 在 倒计时 smsCountdownYanzheng 秒 内
        SysParameter bs = this.iSysParameterService.getByCode(SysParameterEnum.SMS_SENDWAIT.name());
        int sendWait = Integer.parseInt(bs.getParamValue());
        req.setCountDown(sendWait);
        Integer countInteger = iSmsSendRecordService.getCountDown(req);
        log.info("countInteger: {}", countInteger);
        if (countInteger == null || countInteger > 0) {
            // 可以发送短信
            String smsCode = CommonFunction.getFourRandomSmsCode();
            SmsSendRecord sysShortmsg = new SmsSendRecord();
//            sysShortmsg.setCountDown(sendwait);
            sysShortmsg.setMobile(req.getPhone());
            sysShortmsg.setSmsType(req.getSendType());
            sysShortmsg.setSmsCode(smsCode);
            sysShortmsg.setIpAddress(ipAddress);
            StringBuilder sms_message = new StringBuilder();
            // 1短信登陆 ;2找回密码 ;3注册；4.修改密码
            sms_message.append("验证码：");
            sms_message.append(smsCode);

            SysParameter smsValidity = this.iSysParameterService.getByCode(SysParameterEnum.SMS_VALIDATE.name());
            Integer validityMin = Integer.parseInt(smsValidity.getParamValue());
            sms_message.append("，验证码有效期为" + validityMin + "分钟，请勿向他人泄漏。");
            sysShortmsg.setContent(sms_message.toString());
            int inputLine = 1;
            // 根据地区 调短信发送接口 默认 中国大陆
            if (StringUtils.isEmpty(req.getAreaCode()) || UserConstants.AREACODE_CHINA_MAINLAND_86.equals(req.getAreaCode())
                    || UserConstants.AREACODE_CHINA_MAINLAND_086.equals(req.getAreaCode())) {
                SysParameter smsSwitch = this.iSysParameterService.getByCode(SysParameterEnum.SMS_ONOFF.name());
                if (smsSwitch == null || StringUtils.isEmpty(smsSwitch.getParamValue()) || Integer.parseInt(smsSwitch.getParamValue()) != 1) {
                    throw new BizException("系统参数(SMS_ONOFF)异常");
                }
                //发送短信验证码组件
                SmsSendResult smsSendResult = smsSendTemplate.sendSms(req.getPhone(), smsCode, sysShortmsg.getSmsType());
                if (smsSendResult.isSuccess()) {
                    // 发送成功
                    // logger.info("{}发送短信验证码{} ",req.getTel(),smsCode);
                    log.info("{}发送短信成功 ", req.getPhone());
                    sysShortmsg.setStatus(UserConstants.STATUS_SUCCESS);
                    iSmsSendRecordService.saveSmsSendRecord(sysShortmsg);
                } else {
                    sysShortmsg.setStatus(UserConstants.STATUS_FAIL);
                    iSmsSendRecordService.saveSmsSendRecord(sysShortmsg);
                    throw new BizException("短信发送失败");
                }

            }
        } else {
            // 明确提示
            String s = String.valueOf(countInteger).replaceAll("-", "");
            throw new BizException(s + "秒后才能发送");
        }
        return null;
    }


}
