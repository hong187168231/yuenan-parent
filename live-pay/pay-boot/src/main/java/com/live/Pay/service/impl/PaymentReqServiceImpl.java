package com.live.Pay.service.impl;

import com.live.Pay.mapper.PaymentReqMapper;
import com.live.Pay.service.IPaymentReqService;
import com.live.pay.pojo.req.PayOnlineReq;
import com.live.pay.pojo.resp.PayOnlineResp;
import com.live.pay.pojo.vo.PayBankConfigVO;
import com.live.pay.pojo.vo.PayWayConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PaymentReqServiceImpl implements IPaymentReqService {

    @Autowired
    private PaymentReqMapper paymentReqMapper;


    @Override
    public List<PayOnlineResp> getPayWayList(PayOnlineReq payOnlineReq) {

        // 会员层级信息
        // PayGroupConfigVO payGroupConfigVO = paymentReqMapper.queryGroup(payOnlineReq.getMemId());

        List<PayOnlineResp> result = new ArrayList<>();
        List<PayWayConfigVO> list = paymentReqMapper.queryWayList(payOnlineReq.getWayType(), payOnlineReq.getMemId());
        list.stream().forEach(payBankConfigVO -> {

            PayOnlineResp payOnlineResp = new PayOnlineResp();
            payOnlineResp.setMemId(payOnlineReq.getMemId());
            payOnlineResp.setPayWayId(payOnlineResp.getPayWayId());
            payOnlineResp.setWayType(payOnlineResp.getWayType());
            payOnlineResp.setWayTypeCode(payOnlineResp.getWayTypeCode());
            payOnlineResp.setWayName(payOnlineResp.getWayName());
            payOnlineResp.setWayAccount(payOnlineResp.getWayAccount());
            payOnlineResp.setQrCode(payOnlineResp.getQrCode());
            payOnlineResp.setRemark(payOnlineResp.getRemark());
            result.add(payOnlineResp);
        });

        // 先校验
        return result;
    }

    @Override
    public List<PayOnlineResp> getPayBankList(PayOnlineReq payOnlineReq) {

        List<PayOnlineResp> result = new ArrayList<>();
        List<PayBankConfigVO> list = paymentReqMapper.queryBankList(payOnlineReq.getBankId(), payOnlineReq.getMemId());
        list.stream().forEach(payWayConfigVO -> {

            PayOnlineResp payOnlineResp = new PayOnlineResp();
            payOnlineResp.setRemark(payWayConfigVO.getRemark());
            payOnlineResp.setMemId(payOnlineReq.getMemId());
            payOnlineResp.setPayBankId(payOnlineResp.getPayBankId());
            payOnlineResp.setBankName(payOnlineResp.getBankName());
            payOnlineResp.setOpenAccount(payOnlineResp.getOpenAccount());
            payOnlineResp.setBankCardNo(payOnlineResp.getBankCardNo());
            payOnlineResp.setPayUrl(payOnlineResp.getPayUrl());
            payOnlineResp.setMaxAmount(payOnlineResp.getMaxAmount());
            payOnlineResp.setMinAmount(payOnlineResp.getMinAmount());
            payOnlineResp.setBranchAddress(payOnlineResp.getBranchAddress());
            result.add(payOnlineResp);
        });


        return result;
    }
}
