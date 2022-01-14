package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.mapper.PayWayMapper;
import com.indo.pay.pojo.vo.PayWayVO;
import com.indo.pay.service.IPayWayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<PayWayVO> wayList(LoginInfo loginInfo) {
        List<PayWayConfig> configList = baseMapper.wayList();
        return dozerUtil.convert(configList, PayWayVO.class);
    }


}
