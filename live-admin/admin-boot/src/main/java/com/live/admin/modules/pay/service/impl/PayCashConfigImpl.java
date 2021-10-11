package com.live.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.live.admin.modules.pay.mapper.PayCashConfigMapper;
import com.live.admin.modules.pay.service.IPayCashConfigService;
import com.live.common.mybatis.base.service.impl.SuperServiceImpl;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.entity.PayCashConfig;
import com.live.pay.pojo.vo.PayCashConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 出款配置业务服务
 * </p>
 *
 * @author boyd
 * @since 2021-09-08
 */
@Service
public class PayCashConfigImpl extends SuperServiceImpl<PayCashConfigMapper, PayCashConfig> implements IPayCashConfigService {

    @Autowired
    private PayCashConfigMapper payCashConfigMapper;



    @Override
    public List<PayCashConfigVO> queryList(Page<PayCashConfigVO> page, PayCashConfigDto dto) {
        return payCashConfigMapper.queryList(page, dto);
    }

    @Override
    public PayCashConfigVO querySingle(PayCashConfigDto dto) {
        return payCashConfigMapper.querySingle(dto);
    }

    @Override
    public Result insert(PayCashConfigDto dto) {

        payCashConfigMapper.insertCash(getPayCashConfig(dto));

        return Result.success();
    }

    @Override
    public Result updateCash(PayCashConfigDto dto) {
        payCashConfigMapper.updateCash(getPayCashConfig(dto));

        return Result.success();
    }

    @Override
    public Result deleteCash(PayCashConfigDto dto) {
        payCashConfigMapper.deleteCash(dto);

        return Result.success();
    }

    /**
     * 转换请求参数对象
     * @param dto
     * @return
     */
    private PayCashConfig getPayCashConfig(PayCashConfigDto dto){
        PayCashConfig payCashConfig = new PayCashConfig();
        if(Objects.nonNull(dto.getPayCashId())) payCashConfig.setPayCashId(dto.getPayCashId());
        payCashConfig.setCashName(dto.getCashName());
        payCashConfig.setCashTypeCode(dto.getCashTypeCode());
        payCashConfig.setSortBy(dto.getSortBy());
        payCashConfig.setIsDel(dto.getIsDel());
        payCashConfig.setDesc(dto.getDesc());
        payCashConfig.setCurrency(dto.getCurrency());
        payCashConfig.setCashFee(dto.getCashFee());
        payCashConfig.setBindAccountNum(dto.getBindAccountNum());
        payCashConfig.setMinCashAmount(dto.getMinCashAmount());
        payCashConfig.setMaxCashAmount(dto.getMaxCashAmount());
        return payCashConfig;
    }
}
