package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.pay.mapper.PayGroupConfigMapper;
import com.indo.admin.modules.pay.service.IPayGroupConfigService;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayGroupConfigDto;
import com.indo.pay.pojo.entity.PayGroupConfig;
import com.indo.pay.pojo.vo.PayGroupConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 支付层级配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Service
public class PayGroupConfigImpl extends SuperServiceImpl<PayGroupConfigMapper, PayGroupConfig> implements IPayGroupConfigService {

    @Autowired
    private PayGroupConfigMapper payGroupConfigMapper;


    @Override
    public List<PayGroupConfigVO> queryList(Page<PayGroupConfigVO> page, PayGroupConfigDto dto) {
        return payGroupConfigMapper.queryList(page, dto);
    }

    @Override
    public PayGroupConfigVO querySingle(PayGroupConfigDto dto) {
        return payGroupConfigMapper.querySingle(dto);
    }

    @Override
    public Result insertPayGroup(PayGroupConfigDto dto) {

        payGroupConfigMapper.insertPayGroup(getPayGroupConfig(dto));

        return Result.success();
    }

    @Override
    public Result updatePayGroup(PayGroupConfigDto dto) {
        payGroupConfigMapper.updatePayGroup(getPayGroupConfig(dto));

        return Result.success();
    }

    @Override
    public Result deletePayGroup(PayGroupConfigDto dto) {
        payGroupConfigMapper.deletePayGroup(dto);

        return Result.success();
    }

    @Override
    public Result stopStartPayGroup(PayGroupConfigDto dto) {
        payGroupConfigMapper.stopStartPayGroup(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayGroupConfig getPayGroupConfig(PayGroupConfigDto dto){
        PayGroupConfig payGroupConfig = new PayGroupConfig();
        if(Objects.nonNull(dto.getPayGroupId())) payGroupConfig.setPayGroupId(dto.getPayGroupId());
        payGroupConfig.setGroupName(dto.getGroupName());
        payGroupConfig.setGroupVal(dto.getGroupVal());
        payGroupConfig.setSingleMaxAmount(dto.getSingleMaxAmount());
        payGroupConfig.setTodayMaxAmount(dto.getTodayMaxAmount());
        payGroupConfig.setSingleCashMaxAmount(dto.getSingleCashMaxAmount());
        payGroupConfig.setTodayCashMaxAmount(dto.getTodayCashMaxAmount());
        payGroupConfig.setTodayCashMaxNum(dto.getTodayCashMaxNum());
        payGroupConfig.setTodayMaxNotPay(dto.getTodayMaxNotPay());
        payGroupConfig.setRemark(dto.getRemark());
        payGroupConfig.setIsDel(dto.getIsDel());
        payGroupConfig.setCreateUser(dto.getCreateUser());
        payGroupConfig.setUpdateUser(dto.getUpdateUser());
        return payGroupConfig;
    }
}
