package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.mapper.PayChannelConfigMapper;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayChannelConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 支付渠道配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Service
public class PayChannelConfigImpl extends SuperServiceImpl<PayChannelConfigMapper, PayChannelConfig> implements IPayChannelConfigService {

    @Autowired
    private PayChannelConfigMapper payChannelConfigMapper;


    @Override
    public List<PayChannelConfigVO> queryList(Page<PayChannelConfigVO> page, PayChannelConfigDto dto) {
        return payChannelConfigMapper.queryList(page, dto);
    }

    @Override
    public PayChannelConfigVO querySingle(PayChannelConfigDto dto) {
        return payChannelConfigMapper.querySingle(dto);
    }

    @Override
    public Result insertPayChannel(PayChannelConfigDto dto) {

        payChannelConfigMapper.insertPayChannel(getPayChannelConfig(dto));

        return Result.success();
    }

    @Override
    public Result updatePayChannel(PayChannelConfigDto dto) {
        payChannelConfigMapper.updatePayChannel(getPayChannelConfig(dto));

        return Result.success();
    }

    @Override
    public Result deletePayChannel(PayChannelConfigDto dto) {
        payChannelConfigMapper.deletePayChannel(dto);

        return Result.success();
    }

    @Override
    public Result stopStartPayChannel(PayChannelConfigDto dto) {
        payChannelConfigMapper.stopStartPayChannel(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayChannelConfig getPayChannelConfig(PayChannelConfigDto dto){
        PayChannelConfig payChannelConfig = new PayChannelConfig();
        if(Objects.nonNull(dto.getPayChannelId())) payChannelConfig.setPayChannelId(dto.getPayChannelId());
        payChannelConfig.setChannelName(dto.getChannelName());
        payChannelConfig.setChannelDesc(dto.getChannelDesc());
        payChannelConfig.setChanneType(dto.getChanneType());
        payChannelConfig.setPayUrl(dto.getPayUrl());
        payChannelConfig.setRemark(dto.getRemark());
        payChannelConfig.setSortBy(dto.getSortBy());
        payChannelConfig.setIsDel(dto.getIsDel());
        payChannelConfig.setCreateUser(dto.getCreateUser());
        payChannelConfig.setUpdateUser(dto.getUpdateUser());
        return payChannelConfig;
    }
}
