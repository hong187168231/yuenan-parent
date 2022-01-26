package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.mapper.PayWayMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.vo.PayWayVO;
import com.indo.pay.service.IPayWayService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.bo.MemTradingBO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private DozerUtil dozerUtil;

    @Resource
    private RechargeMapper rechargeMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;

    @Override
    public List<PayWayVO> wayList(LoginInfo loginInfo) {
        BigDecimal todayAmount = rechargeMapper.countTodayAmount(loginInfo.getId());
        BigDecimal totalAmount = getMemTotalAmount(loginInfo.getAccount());

        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.PAY_WAY_KEY);
        List<PayWayConfig> wayList = new LinkedList(map.values());
        Iterator<PayWayConfig> iter = wayList.iterator();
        while (iter.hasNext()) {
            PayWayConfig item = iter.next();
            if (todayAmount.longValue() > item.getTodayAmount()) {
                iter.remove();
                continue;
            }
            if (totalAmount.longValue() > item.getTotalAmount()) {
                iter.remove();
                continue;
            }
        }
        return dozerUtil.convert(wayList, PayWayVO.class);
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
