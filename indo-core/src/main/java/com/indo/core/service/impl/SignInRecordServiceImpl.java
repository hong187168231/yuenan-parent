package com.indo.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.exception.BizException;
import com.indo.core.mapper.ActivityConfigMapper;
import com.indo.core.mapper.SignInRecordMapper;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.dto.SignInRecordDTO;
import com.indo.core.pojo.entity.ActivityConfig;
import com.indo.core.pojo.entity.SignInRecord;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.service.ISignInRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 签到记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-07-29
 */
@Service
public class SignInRecordServiceImpl extends ServiceImpl<SignInRecordMapper, SignInRecord> implements ISignInRecordService {
    @Resource
    private ActivityConfigMapper activityConfigMapper;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;
    @Override
    public Page<SignInRecord> findMemSignInRecordPage(SignInRecordDTO signInRecordDTO, LoginInfo loginInfo) {
        Page<SignInRecord> page = new Page<>(signInRecordDTO.getPage(), signInRecordDTO.getLimit());
        LambdaQueryWrapper<SignInRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SignInRecord::getMemId,loginInfo.getId());
        return baseMapper.selectPage(page,wrapper);
    }

    @Override
    public Integer findUserSignInNum(LoginInfo loginInfo) {
        LambdaQueryWrapper<SignInRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SignInRecord::getMemId,loginInfo.getId());
        return baseMapper.selectCount(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void signIn(LoginInfo loginInfo) {
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityConfig::getTypes,1);
        ActivityConfig activityConfig = activityConfigMapper.selectOne(wrapper);
        if(activityConfig==null){
            throw new BizException("无相关活动配置，无法参加活动");
        }
        JSONObject json = JSONObject.parseObject(activityConfig.getConfigInfo());
        LambdaQueryWrapper<SignInRecord> srWrapper = new LambdaQueryWrapper<>();
        srWrapper.eq(SignInRecord::getMemId,loginInfo.getId());
        Integer num = baseMapper.selectCount(srWrapper);
        BigDecimal money = json.getBigDecimal("sign"+((num+1)%7));
        SignInRecord signInRecord = new SignInRecord();
        signInRecord.setMemId(loginInfo.getId());
        signInRecord.setSignMoney(money);
        signInRecord.setCreateTime(new Date());
        baseMapper.insert(signInRecord);
        MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
        agentRebateChange.setChangeAmount(money);
        agentRebateChange.setTradingEnum(TradingEnum.INCOME);
        agentRebateChange.setGoldchangeEnum(GoldchangeEnum.SIGNIN);
        agentRebateChange.setUserId(loginInfo.getId());
        iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
    }

}