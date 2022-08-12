package com.indo.pay.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.utils.QueryHelpPlus;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.mapper.PayWayMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.criteria.PayWayQueryCriteria;
import com.indo.pay.pojo.vo.PayWayVO;
import com.indo.pay.service.IPayWayService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.core.pojo.bo.MemTradingBO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 支付方式配置 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayWayServiceImpl extends ServiceImpl<PayWayMapper, PayWayConfig> implements IPayWayService {

    @Resource
    private RechargeMapper rechargeMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;

    @Override
    public List<PayWayVO> wayList(LoginInfo loginInfo, Long payChannelId) {
//        BigDecimal totalAmount = getMemTotalAmount(loginInfo.getAccount());
//        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.PAY_WAY_KEY);
        List<PayWayConfig> wayList = new LinkedList();
        if (CollectionUtil.isEmpty(wayList)) {
            PayWayQueryCriteria criteria = new PayWayQueryCriteria();
            wayList = baseMapper.selectList(QueryHelpPlus.getPredicate(PayWayConfig.class, criteria));
        }
        if (CollectionUtil.isNotEmpty(wayList)) {
            wayList = wayList.stream()
                    .filter(wayConfig -> wayConfig.getPayChannelId().equals(payChannelId)
                            && wayConfig.getStatus().equals(1))
                    .collect(Collectors.toList());
        }
        Iterator<PayWayConfig> iter = wayList.iterator();
        while (iter.hasNext()) {
            PayWayConfig item = iter.next();
            BigDecimal todayWayAmount = rechargeMapper.countWayTodayAmount(payChannelId, item.getPayWayId());
            if (ObjectUtil.isNotEmpty(item.getTodayAmount()) &&
                    todayWayAmount.longValue() > item.getTodayAmount()) {
                iter.remove();
                continue;
            }
        }
        return DozerUtil.convert(wayList, PayWayVO.class);
    }

    @Override
    public PayWayConfig getPayWayById(Long wayId) {
        PayWayConfig payWayConfig = (PayWayConfig) RedisUtils.hget(RedisConstants.PAY_WAY_KEY, wayId + "");
        if (ObjectUtil.isEmpty(payWayConfig)) {
            return this.baseMapper.selectEnableWayById(wayId);
        } else {
            return GlobalConstants.STATUS_OPEN.equals(payWayConfig.getStatus()) ? payWayConfig : null;
        }
    }

    public BigDecimal getMemTotalAmount(String account) {
        Result<MemTradingBO> result = memBaseInfoFeignClient.getMemTradingInfo(account);
        if (Result.success().getCode().equals(result.getCode())) {
            MemTradingBO memBaseinfo = result.getData();
            return memBaseinfo.getTotalDeposit();
        } else {
            throw new BizException("No client with requested id: " + account);
        }
    }


}
