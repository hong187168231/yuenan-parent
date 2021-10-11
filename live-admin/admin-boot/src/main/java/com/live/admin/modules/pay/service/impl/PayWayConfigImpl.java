package com.live.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.mapper.PayChannelConfigMapper;
import com.live.admin.modules.pay.mapper.PayWayConfigMapper;
import com.live.admin.modules.pay.service.IPayChannelConfigService;
import com.live.admin.modules.pay.service.IPayWayConfigService;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayChannelConfigDto;
import com.live.pay.pojo.dto.PayWayConfigDto;
import com.live.pay.pojo.entity.PayChannelConfig;
import com.live.pay.pojo.entity.PayWayConfig;
import com.live.pay.pojo.vo.PayChannelConfigVO;
import com.live.pay.pojo.vo.PayWayConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 支付方式配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Service
public class PayWayConfigImpl extends SuperServiceImpl<PayWayConfigMapper, PayWayConfig> implements IPayWayConfigService {

    @Autowired
    private PayWayConfigMapper payWayConfigMapper;


    @Override
    public List<PayWayConfigVO> queryList(Page<PayWayConfigVO> page, PayWayConfigDto dto) {
        return payWayConfigMapper.queryList(page, dto);
    }

    @Override
    public PayWayConfigVO querySingle(PayWayConfigDto dto) {
        return payWayConfigMapper.querySingle(dto);
    }

    @Override
    public Result insertPayWay(PayWayConfigDto dto) {

        payWayConfigMapper.insertPayWay(getPayChannelConfig(dto));

        return Result.success();
    }

    @Override
    public Result updatePayWay(PayWayConfigDto dto) {
        payWayConfigMapper.updatePayWay(getPayChannelConfig(dto));

        return Result.success();
    }

    @Override
    public Result deletePayWay(PayWayConfigDto dto) {
        payWayConfigMapper.deletePayWay(dto);

        return Result.success();
    }

    @Override
    public Result stopStartPayWay(PayWayConfigDto dto) {
        payWayConfigMapper.stopStartPayWay(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayWayConfig getPayChannelConfig(PayWayConfigDto dto){
        PayWayConfig payWayConfig = new PayWayConfig();
        if(Objects.nonNull(dto.getPayWayId())) payWayConfig.setPayWayId(dto.getPayWayId());
        payWayConfig.setWayType(dto.getWayType());
        payWayConfig.setWayTypeCode(dto.getWayTypeCode());
        payWayConfig.setWayName(dto.getWayName());
        payWayConfig.setWayAccount(dto.getWayAccount());
        payWayConfig.setQrCode(dto.getQrCode());
        payWayConfig.setGroupId(dto.getGroupId());
        payWayConfig.setTips(dto.getTips());
        payWayConfig.setAuditStatus(dto.getAuditStatus());
        payWayConfig.setMinAmount(dto.getMinAmount());
        payWayConfig.setMaxAmount(dto.getMaxAmount());
        payWayConfig.setRemark(dto.getRemark());
        payWayConfig.setSortBy(dto.getSortBy());
        payWayConfig.setIsDel(dto.getIsDel());
        payWayConfig.setCreateUser(dto.getCreateUser());
        payWayConfig.setUpdateUser(dto.getUpdateUser());
        return payWayConfig;
    }
}
