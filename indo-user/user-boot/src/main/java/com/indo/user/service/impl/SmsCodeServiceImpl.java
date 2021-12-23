package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.user.mapper.MemLevelMapper;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.sms.SmsCodeVo;
import com.indo.user.service.IMemLevelService;
import com.indo.user.service.ISmsCodeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
public class SmsCodeServiceImpl implements ISmsCodeService {


    @Override
    public List<SmsCodeVo> smsList() {
        SmsCodeVo smsCodeVo = new SmsCodeVo();
        smsCodeVo.setSmsCode("086");
        smsCodeVo.setCountry("中国");

        SmsCodeVo smsCodeVo2 = new SmsCodeVo();
        smsCodeVo2.setSmsCode("0855");
        smsCodeVo2.setCountry("柬埔寨");

        List<SmsCodeVo> list = new ArrayList<>();
        list.add(smsCodeVo);
        list.add(smsCodeVo2);
        return list;
    }
}
