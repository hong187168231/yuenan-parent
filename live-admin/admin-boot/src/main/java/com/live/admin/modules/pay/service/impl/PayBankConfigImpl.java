package com.live.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.mapper.PayBankConfigMapper;
import com.live.admin.modules.pay.service.IPayBankConfigService;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayBankConfigDto;
import com.live.pay.pojo.entity.PayBankConfig;
import com.live.pay.pojo.vo.PayBankConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 银行支付配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Service
public class PayBankConfigImpl extends SuperServiceImpl<PayBankConfigMapper, PayBankConfig> implements IPayBankConfigService {

    @Autowired
    private PayBankConfigMapper payBankConfigMapper;


    @Override
    public List<PayBankConfigVO> queryList(Page<PayBankConfigVO> page, PayBankConfigDto dto) {
        return payBankConfigMapper.queryList(page, dto);
    }

    @Override
    public PayBankConfigVO querySingle(PayBankConfigDto dto) {
        return payBankConfigMapper.querySingle(dto);
    }

    @Override
    public Result insertPayBank(PayBankConfigDto dto) {

        payBankConfigMapper.insertPayBank(getPayBankConfig(dto));

        return Result.success();
    }

    @Override
    public Result updatePayBank(PayBankConfigDto dto) {
        payBankConfigMapper.updatePayBank(getPayBankConfig(dto));

        return Result.success();
    }

    @Override
    public Result deletePayBank(PayBankConfigDto dto) {
        payBankConfigMapper.deletePayBank(dto);

        return Result.success();
    }

    @Override
    public Result stopStartPayBank(PayBankConfigDto dto) {
        payBankConfigMapper.stopStartPayBank(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayBankConfig getPayBankConfig(PayBankConfigDto dto){
        PayBankConfig payBankConfig = new PayBankConfig();
        if(Objects.nonNull(dto.getPayBankId())) payBankConfig.setPayBankId(dto.getPayBankId());
        payBankConfig.setBankId(dto.getBankId());
        payBankConfig.setOpenAccount(dto.getOpenAccount());
        payBankConfig.setBankCardNo(dto.getBankCardNo());
        payBankConfig.setPayUrl(dto.getPayUrl());
        payBankConfig.setMinAmount(dto.getMinAmount());
        payBankConfig.setMaxAmount(dto.getMaxAmount());
        payBankConfig.setBranchAddress(dto.getBranchAddress());
        payBankConfig.setRemark(dto.getRemark());
        payBankConfig.setGroupId(dto.getGroupId());
        payBankConfig.setTips(dto.getTips());
        payBankConfig.setSortBy(dto.getSortBy());
        payBankConfig.setIsDel(dto.getIsDel());
        payBankConfig.setCreateUser(dto.getCreateUser());
        payBankConfig.setUpdateUser(dto.getUpdateUser());
        return payBankConfig;
    }
}
