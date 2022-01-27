package com.indo.pay.service.proxyWithdraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.constant.RedisConstants;
import com.indo.common.enums.ThirdPayChannelEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.utils.DateUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.http.HttpClient;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.core.pojo.entity.PayWithdrawConfig;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.mapper.TakeCashMapper;
import com.indo.pay.pojo.bo.PayTakeCashBO;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.EasyPayReq;
import com.indo.pay.pojo.resp.withdraw.HuaRenWithdrawCallbackReq;
import com.indo.pay.service.ITakeCashService;
import com.indo.pay.util.SignMd5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author puff
 * @Description: 支付业务类
 * @date 2021/9/5 15:57
 */
@Component
@Slf4j
public class ProxyWithdrawService {


    @Autowired
    private ITakeCashService iTakeCashService;

    @Autowired
    private PayChannelMapper payChannelMapper;

    @Autowired
    private TakeCashMapper takeCashMapper;


    /**
     * 发起代付请求
     *
     * @param payTakeCash
     * @return
     */
    public boolean withdrawRequest(PayTakeCashBO payTakeCash) {
        boolean flag = false;
        BigDecimal todayAmount = takeCashMapper.countTodayAmount();
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.PAY_WITHDRAW_KEY);
        List<PayWithdrawConfig> configList = new LinkedList(map.values());
        Iterator<PayWithdrawConfig> iter = configList.iterator();
        while (iter.hasNext()) {
            PayWithdrawConfig item = iter.next();
            if (todayAmount.longValue() > item.getTodayAmount()) {
                iter.remove();
                continue;
            }
        }
        configList.sort(Comparator.comparing(PayWithdrawConfig::getSortBy));
        if (CollectionUtil.isNotEmpty(configList)) {
            throw new BizException("无出款通道");
        }
        PayWithdrawConfig payWithdrawConfig = configList.get(0);
        PayChannelConfig channelConfig = payChannelMapper.selectById(payWithdrawConfig.getPayChannelId());
        ThirdPayChannelEnum payChannel = ThirdPayChannelEnum.valueOf(channelConfig.getChannelCode());
        switch (payChannel) {
            case HUAREN:
                flag = huarenProxyWithdraw(payTakeCash, channelConfig, payWithdrawConfig);
                break;
            case EASY:
                flag = easyProxyWithdraw(payTakeCash, channelConfig, payWithdrawConfig);
                break;
            default:
                throw new BizException("请选择正确的支付方式");
        }
        return flag;
    }


    /**
     *  huaren代付
     * @param payTakeCash
     * @param channelConfig
     * @param payWithdrawConfig
     * @return
     */
    private boolean huarenProxyWithdraw(PayTakeCashBO payTakeCash, PayChannelConfig channelConfig, PayWithdrawConfig payWithdrawConfig) {
        Map<String, String> reqMap = new TreeMap<>();
        try {
            log.info("paylog huaren支付 创建订单");
            reqMap.put("mch_id", channelConfig.getMerchantNo());
            reqMap.put("mch_transferId", payTakeCash.getOrderNo());
            reqMap.put("transfer_amount", payTakeCash.getActualAmount().toString());
            reqMap.put("apply_date", DateUtils.getNewFormatDateString(new Date()));
            reqMap.put("bank_code", payTakeCash.getIfscCode());
            reqMap.put("receive_name", payTakeCash.getBankName());
            reqMap.put("receive_account", payTakeCash.getBankCardNo());
            String sign = SignMd5Utils.createSmallSign(reqMap, payWithdrawConfig.getSecretKey());
            reqMap.put("sign_type", "MD5");
            reqMap.put("sign", sign);
            // http请求
            String httpRet = "";
            try {
                httpRet = HttpClient.formPost(payWithdrawConfig.getWithdrawUrl(), reqMap);
                log.info("paylog hr支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
            } catch (Exception e) {
                log.error("paylog hr支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
                return false;
            }
            log.info("paylog hr支付 创建订单返回 {}", this.getClass().getName(), httpRet);
            JSONObject jsonObject = JSONObject.parseObject(httpRet);
            if (httpRet.contains("respCode")) {
                String respCode = jsonObject.getString("respCode");
                String errorMsg = jsonObject.getString("errorMsg");
                if ("FAIL".equals(respCode)) {
                    log.info("paylog hr代付 创建订单失败返回，错误信息  =====> {}", errorMsg);
                    return false;
                } else if ("SUCCESS".equals(respCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("paylog huaren支付 创建订单失败 {} huarenPay {}", JSON.toJSONString(reqMap), e);
        }
        return false;
    }


    /**
     * easy代付
     * @param payTakeCash
     * @param channelConfig
     * @param payWithdrawConfig
     * @return
     */
    private boolean easyProxyWithdraw(PayTakeCashBO payTakeCash, PayChannelConfig channelConfig, PayWithdrawConfig payWithdrawConfig) {
        EasyPayReq req = new EasyPayReq();
        try {
            log.info("paylog easyPay支付 创建订单");
            // 生成验签
            Map<String, String> reqMap = new TreeMap<>();

            reqMap.put("merchantId", channelConfig.getMerchantNo());
            reqMap.put("outTradeNo", payTakeCash.getOrderNo());
            reqMap.put("coin", payTakeCash.getActualAmount().toString());
            reqMap.put("notifyUrl", payWithdrawConfig.getNotifyUrl());
            reqMap.put("transferCategory", "IMPS");

            reqMap.put("bankAccountName", payTakeCash.getBankAccount());
            reqMap.put("bankCardNum", payTakeCash.getBankCardNo());
            reqMap.put("ifscCode", payTakeCash.getIfscCode());
            reqMap.put("bankName", payTakeCash.getBankName());
            reqMap.put("bankBranchName", payTakeCash.getBankBranch());
            reqMap.put("province", payTakeCash.getBankProvince());
            reqMap.put("city", payTakeCash.getBankCity());

            String sign = SignMd5Utils.createSmallSign(reqMap, payWithdrawConfig.getSecretKey());
            reqMap.put("sign", sign.toUpperCase());

            // http请求
            JSONObject httpJsonObj = new JSONObject();
            try {
                httpJsonObj = HttpClient.doPost(payWithdrawConfig.getWithdrawUrl() + "/v1/df/createOrder", JSON.toJSONString(reqMap));
                log.info("paylog easy支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
            } catch (Exception e) {
                log.error("paylog easy支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
                return false;
            }
            log.info("paylog easy支付 创建订单返回 {} callPayService {}", this.getClass().getName(), httpJsonObj);
            if (!ObjectUtils.isEmpty(httpJsonObj)) {
                if (httpJsonObj.get("code").equals(0)) {
                    return true;
                } else {
                    String msg = httpJsonObj.getString("message");
                    log.info("paylog hr代付 创建订单失败返回，错误信息  =====> {}", msg);
                    return false;
                }
            } else {
                log.error("paylog easy支付 创建订单返回错误 {} callPayService {}",
                        this.getClass().getName(), JSON.toJSONString(httpJsonObj));
                return false;
            }
        } catch (Exception e) {
            log.error("paylog easy支付 创建订单失败 {} easyPay {}", this.getClass().getName(), JSON.toJSONString(req), e);
        }
        return false;
    }


}
