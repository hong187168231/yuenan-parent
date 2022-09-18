package com.indo.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.mapper.ActivityConfigMapper;
import com.indo.core.mapper.LoanRecordMapper;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.core.pojo.dto.LoanRecordDTO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.ActivityConfig;
import com.indo.core.pojo.entity.LoanRecord;
import com.indo.core.pojo.entity.MemLevel;
import com.indo.core.pojo.vo.LoanRecordVo;
import com.indo.core.service.ILoanRecordService;
import com.indo.core.service.IMemGoldChangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        wrapper.orderByDesc(LoanRecord::getCreateTime);
        return baseMapper.selectPage(page,wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void loanMoney(BigDecimal amount,LoginInfo loginInfo, HttpServletRequest request) {
        MemLevel memLevel = memLevelMapper.selectById(loginInfo.getMemLevel());
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityConfig::getTypes,2);
        ActivityConfig activityConfig = activityConfigMapper.selectOne(wrapper);
        String countryCode = request.getHeader("countryCode");
        if(activityConfig==null){
            throw new BizException(MessageUtils.get(ResultCode.ACTIVITY_NOT_CONFIGURATION.getCode(),countryCode));
        }
        JSONObject json = JSONObject.parseObject(activityConfig.getConfigInfo());
        BigDecimal money = json.getBigDecimal("vip"+memLevel.getLevel());
        if(money==null||money.compareTo(BigDecimal.ZERO)==0){
            throw new BizException(MessageUtils.get(ResultCode.ACTIVITY_NOT_CONFIGURATION.getCode(),countryCode));
        }
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        loanWrapper.eq(LoanRecord::getStates,1).or().eq(LoanRecord::getStates,3);
        List<LoanRecord> loanRecord = baseMapper.selectList(loanWrapper);
        AtomicReference<BigDecimal> backMoney = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> loanAmount = new AtomicReference<>(new BigDecimal(0));
        loanRecord.forEach(l->{
            if(l.getBackMoney()!=null){
                backMoney.set(backMoney.get().add(l.getBackMoney()));
            }
            if(l.getLoanAmount()!=null){
                loanAmount.set(loanAmount.get().add(l.getLoanAmount()));
            }
        });
        if((money.subtract((loanAmount.get().subtract(backMoney.get())))).compareTo(amount)==-1){
            throw new BizException(MessageUtils.get(ResultCode.BALANCE_BU.getCode(),countryCode));
        }
        LoanRecord lr = new LoanRecord();
        lr.setLoanAmount(amount);
        lr.setMemId(loginInfo.getId());
        lr.setStates(1);
        lr.setCreateTime(new Date());
        lr.setBackTime(LocalDateTime.now().minus(-30, ChronoUnit.DAYS));
        baseMapper.insert(lr);
        MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
        agentRebateChange.setChangeAmount(amount);
        agentRebateChange.setTradingEnum(TradingEnum.INCOME);
        agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
        agentRebateChange.setUserId(loginInfo.getId());
        iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
    }

//    @Async
//    @Scheduled(cron = "0 0 1 * * ?")
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void automaticbBackMoney() {
//        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
//        loanWrapper.eq(LoanRecord::getStates,1);
//        loanWrapper.lt(LoanRecord::getBackTime,LocalDateTime.now());
//        List<LoanRecord> list =baseMapper.selectList(loanWrapper);
//        list.forEach(l->{
//            MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
//            agentRebateChange.setChangeAmount(l.getLoanAmount());
//            agentRebateChange.setTradingEnum(TradingEnum.SPENDING);
//            agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
//            agentRebateChange.setUserId(l.getMemId());
//            iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
//            l.setBackMoney(l.getLoanAmount());
//            l.setStates(2);
//            l.setUpdateTime(new Date());
//            baseMapper.updateById(l);
//        });
//    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void activeBackMoney(BigDecimal amount,LoginInfo loginInfo, HttpServletRequest request) {
        String countryCode = request.getHeader("countryCode");
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new BizException(MessageUtils.get(ResultCode.REPAYMENT_AMOUNT_ERROR.getCode(),countryCode));
        }
        if(loginInfo.getBalance().compareTo(amount)==-1){
            throw new BizException(MessageUtils.get(ResultCode.BALANCE_BU.getCode(),countryCode));
        }
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getStates,1);
        loanWrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        List<LoanRecord> loanRecordList = baseMapper.selectList(loanWrapper);
        LambdaQueryWrapper<LoanRecord> loanWrappers = new LambdaQueryWrapper<>();
        loanWrappers.eq(LoanRecord::getStates,3);
        loanWrappers.eq(LoanRecord::getMemId,loginInfo.getId());
        List<LoanRecord> loanRecordLists = baseMapper.selectList(loanWrappers);
        if(loanRecordList==null&&loanRecordLists==null){
            throw new BizException(MessageUtils.get(ResultCode.NO_ARREARS.getCode(),countryCode));
        }
        BigDecimal repayment = new BigDecimal(0).add(amount);
        //还部分优先清账
        for(LoanRecord ls:loanRecordLists){
            if(amount.compareTo(BigDecimal.ZERO)<=0){
                break;
            }
            BigDecimal surplus =ls.getLoanAmount().subtract(ls.getBackMoney());
            amount=amount.subtract(surplus);
            if(amount.compareTo(BigDecimal.ZERO)>=0){
                ls.setBackMoney(ls.getBackMoney().add(surplus));
                ls.setStates(2);
                ls.setUpdateTime(new Date());
            }else{
                ls.setBackMoney(ls.getBackMoney().add((amount.add(surplus))));
                ls.setUpdateTime(new Date());
                amount=BigDecimal.ZERO;
            }
            baseMapper.updateById(ls);
        }
        //未还款
        for(LoanRecord l:loanRecordList){
            if(amount.compareTo(BigDecimal.ZERO)<=0){
                break;
            }
            BigDecimal surplus =l.getLoanAmount().subtract(l.getBackMoney());
            amount=amount.subtract(surplus);
            if(amount.compareTo(BigDecimal.ZERO)>=0){
                l.setBackMoney(l.getBackMoney().add(surplus));
                l.setStates(2);
                l.setUpdateTime(new Date());
            }else{
                l.setBackMoney(l.getBackMoney().add((amount.add(surplus))));
                l.setStates(3);
                l.setUpdateTime(new Date());
                amount=BigDecimal.ZERO;
            }
            baseMapper.updateById(l);
        }
        MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
        if(amount.compareTo(BigDecimal.ZERO)==0){
            agentRebateChange.setChangeAmount(repayment);
        }
        if(amount.compareTo(BigDecimal.ZERO)>0){
            agentRebateChange.setChangeAmount(repayment.subtract(amount));
        }
        agentRebateChange.setTradingEnum(TradingEnum.SPENDING);
        agentRebateChange.setGoldchangeEnum(GoldchangeEnum.LOAN);
        agentRebateChange.setUserId(loginInfo.getId());
        iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
    }

    @Override
    public LoanRecordVo findMemLoanInfo(LoginInfo loginInfo, HttpServletRequest request) {
        MemLevel memLevel = memLevelMapper.selectById(loginInfo.getMemLevel());
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityConfig::getTypes,2);
        ActivityConfig activityConfig = activityConfigMapper.selectOne(wrapper);
        String countryCode = request.getHeader("countryCode");
        if(activityConfig==null){
            throw new BizException(MessageUtils.get(ResultCode.ACTIVITY_NOT_CONFIGURATION.getCode(),countryCode));
        }
        JSONObject json = JSONObject.parseObject(activityConfig.getConfigInfo());
        BigDecimal money = json.getBigDecimal("vip"+memLevel.getLevel());
        if(money==null||money.compareTo(BigDecimal.ZERO)==0){
            throw new BizException(MessageUtils.get(ResultCode.LOANRECORD_NOT_CONFIGURATION.getCode(),countryCode));
        }
        LambdaQueryWrapper<LoanRecord> loanWrapper = new LambdaQueryWrapper<>();
        loanWrapper.eq(LoanRecord::getMemId,loginInfo.getId());
        loanWrapper.eq(LoanRecord::getStates,1).or().eq(LoanRecord::getStates,3);
        List<LoanRecord> loanRecord = baseMapper.selectList(loanWrapper);
        //已还金额
        AtomicReference<BigDecimal> backMoney = new AtomicReference<>(new BigDecimal(0));
        //借款金额
        AtomicReference<BigDecimal> loanAmount = new AtomicReference<>(new BigDecimal(0));
        loanRecord.forEach(l->{
            if(l.getBackMoney()!=null){
                backMoney.set(backMoney.get().add(l.getBackMoney()));
            }
            if(l.getLoanAmount()!=null){
                loanAmount.set(loanAmount.get().add(l.getLoanAmount()));
            }
        });

        LoanRecordVo loanRecordVo = new LoanRecordVo();

        BigDecimal arrears = loanAmount.get().subtract(backMoney.get());
        loanRecordVo.setArrears(arrears);
        loanRecordVo.setBalance(money.subtract(arrears));
        return loanRecordVo;
    }
}
