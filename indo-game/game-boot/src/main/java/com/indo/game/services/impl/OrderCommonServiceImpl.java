package com.indo.game.services.impl;

import com.indo.common.enums.SysParameterEnum;
import com.indo.game.services.OrderCommonService;
import com.indo.game.services.SysParamService;
import org.springframework.stereotype.Service;
import com.indo.admin.pojo.entity.SysParameter;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderCommonServiceImpl implements OrderCommonService {

    @Resource
    private SysParamService sysParamService;

    @Override
    public BigDecimal calcNoWithdrawalAmount(BigDecimal noWithdrawalAmount) {
        if (null == noWithdrawalAmount) {
            return BigDecimal.ZERO;
        }
        //计算有效投注额度
        SysParameter sysParameter = sysParamService.getByCode(SysParameterEnum.WITHDRAWAL_AMOUNT);
        if (null != sysParameter) {
            noWithdrawalAmount = noWithdrawalAmount.multiply(new BigDecimal(sysParameter.getParamValue()));
        }
        return noWithdrawalAmount;
    }
}
