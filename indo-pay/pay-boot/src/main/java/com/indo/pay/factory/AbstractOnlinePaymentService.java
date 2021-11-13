package com.indo.pay.factory;

import com.alibaba.fastjson.JSON;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.resp.BasePayResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * @Description 线上支付模板类，XX支付可以继承此类
 * @Date 2019/11/7 15:27
 **/
@Slf4j
public abstract class AbstractOnlinePaymentService implements OnlinePaymentService {

    /**
     * 线上支付流程
     * 1、检查参数
     * 2、持久化支付订单
     * 3、调第三方的支付接口
     * 4、处理返回值
     *
     * @param req 支付需要的参数
     * @param
     * @return
     */
    @Override
    public final <T extends BasePayResp, R extends BasePayReq> T onlinePayment(R req, Class<T> resp) {
        BasePayResp result = new BasePayResp();
        if (checkParams(req)) {
            try {
                //持久化订单
                insertPayment(req);
                //调第三方的支付接口
                return callPayService(req, resp);
            } catch (Exception e) {
                log.error("支付异常，req={} | e={}", JSON.toJSONString(req), e.getMessage());
                result.setMsg("支付异常");
                result.setStatus(500);
                result.setFlag(false);
                return (T) result;
            }
        } else {
            result.setMsg("参数错误");
            result.setStatus(400);
            result.setFlag(false);
            return (T) result;
        }
    }

    protected abstract <T> T callPayService(BasePayReq req, Class<T> clazz);

    /**
     * 支付订单入库
     *
     * @return 成功返回true，失败返回false
     */
    protected abstract <R extends BasePayReq> boolean insertPayment(R req);

    /**
     * 参数检测，如果此方法不符合你的逻辑，可重写此方法
     *
     * @param req
     * @return
     */
    protected boolean checkParams(BasePayReq req) {
        boolean flag = true;
        if (ObjectUtils.isEmpty(req.getAmount())
                || ObjectUtils.isEmpty(req.getMerchantNo()) || ObjectUtils.isEmpty(req.getOrderNo())) {
            flag = false;
        }
        Integer amount = req.getAmount().intValue();
        if (amount.intValue() <= 0) {
            flag = false;
        }
        return flag;
    }


    @Override
    public <T extends BasePayResp, R extends BasePayReq> T onlinePayment(R req) {
        BasePayResp result = new BasePayResp();
        if (checkParams(req)) {
            try {
                //持久化订单
                insertPayment(req);

                //调第三方的支付接口
                String html = callPayService(req, String.class);
                result.setFlag(true);
                result.setStatus(200);
                result.setHtml(html);
                return (T) result;
            } catch (Exception e) {
                log.error("支付异常，req={} | e={}", JSON.toJSONString(req), e.getMessage());
                result.setMsg("支付异常");
                result.setStatus(500);
                result.setFlag(false);
                return (T) result;
            }
        } else {
            result.setMsg("参数错误");
            result.setStatus(400);
            result.setFlag(false);
            return (T) result;
        }
    }

    /**
     * 第三方支付回调处理流程
     * 1、持久化回调日志（解决幂等性）
     * 2、检查参数是否正确
     * 3、账户充值处理
     * 4、返回处理结果
     *
     * @param req 回调参数
     * @param <R>
     * @return
     */
    @Override
    public final <R extends BaseCallBackReq> boolean callBackProcess(R req) {
        String strData = JSON.toJSONString(req);
        log.info("第三方支付回调,req={}", strData);
        try {
            if (checkCallBackParams(req)) {
                return accountRecharge(req);
            }
        } catch (Exception e) {
            log.error("支付回调处理异常,req={} | e={}", JSON.toJSONString(req), e.getMessage());
        }
        return false;
    }

    /**
     * 账户充值
     *
     * @param req
     * @param <R>
     * @return
     */
    protected abstract <R extends BaseCallBackReq> boolean accountRecharge(R req);

    /**
     * 验证参数
     *
     * @param req
     * @param <R>
     * @return
     */
    protected <R extends BaseCallBackReq> boolean checkCallBackParams(R req) {
        return true;
    }

}
