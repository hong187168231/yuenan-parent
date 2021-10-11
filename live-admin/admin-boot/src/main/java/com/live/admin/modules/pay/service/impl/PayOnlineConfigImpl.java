package com.live.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.mapper.PayOnlineConfigMapper;
import com.live.admin.modules.pay.mapper.PayWayConfigMapper;
import com.live.admin.modules.pay.service.IPayOnlineConfigService;
import com.live.admin.modules.pay.service.IPayWayConfigService;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayOnlineConfigDto;
import com.live.pay.pojo.dto.PayWayConfigDto;
import com.live.pay.pojo.entity.PayOnlineConfig;
import com.live.pay.pojo.entity.PayWayConfig;
import com.live.pay.pojo.vo.PayOnlineConfigVO;
import com.live.pay.pojo.vo.PayWayConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 在线支付配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Service
public class PayOnlineConfigImpl extends SuperServiceImpl<PayOnlineConfigMapper, PayOnlineConfig> implements IPayOnlineConfigService {

    @Autowired
    private PayOnlineConfigMapper payOnlineConfigMapper;


    @Override
    public List<PayOnlineConfigVO> queryList(Page<PayOnlineConfigVO> page, PayOnlineConfigDto dto) {
        return payOnlineConfigMapper.queryList(page, dto);
    }

    @Override
    public PayOnlineConfigVO querySingle(PayOnlineConfigDto dto) {
        return payOnlineConfigMapper.querySingle(dto);
    }

    @Override
    public Result insertPayOnline(PayOnlineConfigDto dto) {

        payOnlineConfigMapper.insertPayOnline(getPayOnlineConfig(dto));

        return Result.success();
    }

    @Override
    public Result updatePayOnline(PayOnlineConfigDto dto) {
        payOnlineConfigMapper.updatePayOnline(getPayOnlineConfig(dto));

        return Result.success();
    }

    @Override
    public Result copyPayOnline(PayOnlineConfigDto dto) {

        payOnlineConfigMapper.copyPayOnline(dto);
        return Result.success();
    }

    @Override
    public Result deletePayOnline(PayOnlineConfigDto dto) {
        payOnlineConfigMapper.deletePayOnline(dto);

        return Result.success();
    }

    @Override
    public Result stopStartPayOnline(PayOnlineConfigDto dto) {
        payOnlineConfigMapper.stopStartPayOnline(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayOnlineConfig getPayOnlineConfig(PayOnlineConfigDto dto){
        PayOnlineConfig payOnlineConfig = new PayOnlineConfig();
        if(Objects.nonNull(dto.getPayOnlineId())) payOnlineConfig.setPayOnlineId(dto.getPayOnlineId());
        payOnlineConfig.setPayChannelId(dto.getPayChannelId());
        payOnlineConfig.setPayName(dto.getPayName());
        payOnlineConfig.setMerchantNo(dto.getMerchantNo());
        payOnlineConfig.setMerchantSecret(dto.getMerchantSecret());
        payOnlineConfig.setThirdUrl(dto.getThirdUrl());
        payOnlineConfig.setThirdPublicSecret(dto.getThirdPublicSecret());
        payOnlineConfig.setTerminalNo(dto.getTerminalNo());
        payOnlineConfig.setPayGateway(dto.getPayGateway());
        payOnlineConfig.setCallbackUrl(dto.getCallbackUrl());
        payOnlineConfig.setMinAmount(dto.getMinAmount());
        payOnlineConfig.setMaxAmount(dto.getMaxAmount());
        payOnlineConfig.setRemark(dto.getRemark());
        payOnlineConfig.setIsDel(dto.getIsDel());
        payOnlineConfig.setCreateUser(dto.getCreateUser());
        payOnlineConfig.setUpdateUser(dto.getUpdateUser());
        return payOnlineConfig;
    }

}
