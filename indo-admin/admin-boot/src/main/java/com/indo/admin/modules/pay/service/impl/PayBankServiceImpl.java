package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayBankMapper;
import com.indo.admin.modules.pay.service.IPayBankService;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.admin.pojo.req.pay.PayBankAddReq;
import com.indo.admin.pojo.req.pay.PayBankQueryReq;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayBank;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayBankVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 支付银行表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayBankServiceImpl extends ServiceImpl<PayBankMapper, PayBank> implements IPayBankService {

    @Resource
    IPayChannelConfigService payChannelConfigService;

    @Override
    public boolean addBank(PayBankAddReq addReq) {
        PayBank payBank = new PayBank();
        BeanUtils.copyProperties(addReq, payBank);
        return baseMapper.insert(payBank) > 0;
    }

    @Override
    public Result<List<PayBankVO>> bankList(PayBankQueryReq queryReq) {
        Page<PayBank> page = new Page<>(queryReq.getPage(), queryReq.getLimit());
        LambdaQueryWrapper<PayBank> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryReq.getBankName())) {
            wrapper.like(PayBank::getBankName, queryReq.getBankName());
        }
        Page<PayBank> pageList = baseMapper.selectPage(page, wrapper);
        List<PayBankVO> payBankVOList = DozerUtil.convert(pageList.getRecords(), PayBankVO.class);
        if (CollectionUtils.isNotEmpty(payBankVOList)) {
            List<Long> payChannelIdList = payBankVOList.stream().map(PayBankVO::getPayChannelId).collect(Collectors.toList());
            List<PayChannelConfig> channelConfigList = payChannelConfigService.queryByIds(payChannelIdList);
            if (CollectionUtils.isNotEmpty(channelConfigList)) {
                Map<Long, PayChannelConfig> payChannelConfigMap = channelConfigList.stream().collect(Collectors.toMap(PayChannelConfig::getPayChannelId, a -> a,(k1,k2)->k1));
                for (PayBankVO payBankVO : payBankVOList) {
                    PayChannelConfig channelConfig = payChannelConfigMap.get(payBankVO.getPayChannelId());
                    payBankVO.setPayChannelName(channelConfig.getChannelName());
                    payBankVO.setPayChannelCode(channelConfig.getChannelCode());
                }
            }
        }
        return Result.success(payBankVOList, page.getTotal());
    }

    @Override
    public boolean editStatus(Integer status, Long bankId) {
        PayBank payBank = new PayBank();
        payBank.setStatus(status);
        payBank.setBankId(bankId);
        return baseMapper.updateById(payBank) > 0;
    }
}
