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
import com.indo.core.mapper.LoanRecordMapper;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.ActivityConfig;
import com.indo.core.pojo.entity.LoanRecord;
import com.indo.core.pojo.entity.MemLevel;
import com.indo.core.service.ILoanRecordService;
import com.indo.core.service.IMemGoldChangeService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 借款记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-07-28
 */
@Service
public class LoanRecordServiceImpl extends ServiceImpl<LoanRecordMapper, LoanRecord> implements ILoanRecordService {
   @Resource
   private ActivityConfigMapper activityConfigMapper;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    @Resource
    private MemLevelMapper memLevelMapper;
    @Override
    public Page<LoanRecord> findLoanRecordPageByMemId(LoanRecordDTO loanRecordDTO,LoginInfo loginInfo) {
        Page<LoanRecord> page = new Page<>(loanRecordDTO.getPage(), loanRecordDTO.getLimit());
        LambdaQueryWrapper<LoanRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        return baseMapper.selectPage(page,wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void loanMoney(LoginInfo loginInfo) {
        MemLevel memLevel = memLevelMapper.selectById(loginInfo.getMemLevel());
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityConfig::getTypes,2);
        ActivityConfig activityConfig = activityConfigMapper.selectOne(wrapper);
        if(activityConfig==null){
            throw new BizException("无相关活动配置，无法参加活动");
        }
        JSONObject json = JSONObject.parseObject(activityConfig.getConfigInfo());
        BigDecimal money = json.getBigDecimal("vip"+memLevel.getLevel());
        if(money==null||money.compareTo(BigDecimal.ZERO)==0){
            throw new BizException("无借呗配置，请与管理员联系");
        }
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        loanWrapper.eq(LoanRecord::getStates,1);
        LoanRecord loanRecord = baseMapper.selectOne(loanWrapper);
        if(loanRecord!=null){
            throw new BizException("有欠款未偿还，拒绝提供服务");
        }
        LoanRecord lr = new LoanRecord();
        lr.setLoanAmount(money);
        lr.setMemId(loginInfo.getId());
        lr.setStates(1);
        lr.setCreateTime(new Date());
        lr.setBackTime(LocalDateTime.now().minus(-30, ChronoUnit.DAYS));
        baseMapper.insert(lr);
        MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
        agentRebateChange.setChangeAmount(money);
        agentRebateChange.setTradingEnum(TradingEnum.INCOME);
        agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
        agentRebateChange.setUserId(loginInfo.getId());
        iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void automaticbBackMoney() {
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getStates,1);
        loanWrapper.lt(LoanRecord::getBackTime,LocalDateTime.now());
        List<LoanRecord> list =baseMapper.selectList(loanWrapper);
        list.forEach(l->{
            MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
            agentRebateChange.setChangeAmount(l.getLoanAmount());
            agentRebateChange.setTradingEnum(TradingEnum.SPENDING);
            agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
            agentRebateChange.setUserId(l.getMemId());
            iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
            l.setBackMoney(l.getLoanAmount());
            l.setStates(2);
            l.setUpdateTime(new Date());
            baseMapper.updateById(l);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void activeBackMoney(LoginInfo loginInfo) {
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getStates,1);
        loanWrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        LoanRecord loanRecord =baseMapper.selectOne(loanWrapper);
        if(loanRecord==null){
            throw new BizException("无欠款");
        }
        MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
        agentRebateChange.setChangeAmount(loanRecord.getLoanAmount());
        agentRebateChange.setTradingEnum(TradingEnum.SPENDING);
        agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
        agentRebateChange.setUserId(loginInfo.getId());
        iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
        loanRecord.setBackMoney(loanRecord.getLoanAmount());
        loanRecord.setStates(2);
        loanRecord.setUpdateTime(new Date());
        baseMapper.updateById(loanRecord);
    }
}
