package com.indo.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TaskEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.exception.BizException;
import com.indo.core.mapper.MemTaskRecordMapper;
import com.indo.core.mapper.SysTaskMapper;
import com.indo.core.mapper.SysTaskTypeMapper;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.dto.SysTaskDTO;
import com.indo.core.pojo.entity.MemTaskRecord;
import com.indo.core.pojo.entity.SysTask;
import com.indo.core.pojo.entity.SysTaskType;
import com.indo.core.pojo.vo.TaskModuleVo;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.service.ISysTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements ISysTaskService {
    @Resource
    private IMemGoldChangeService iMemGoldChangeService;
    @Resource
    private SysTaskTypeMapper sysTaskTypeMapper;
    @Resource
    private MemTaskRecordMapper memTaskRecordMapper;

    @Override
    public Page<SysTask> findSysTaskPage(SysTaskDTO sysTaskDTO) {
        Page<SysTask> page = new Page<>(sysTaskDTO.getPage(), sysTaskDTO.getLimit());
        LambdaQueryWrapper<SysTask> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TaskModuleVo> findMemTaskInfo(LoginInfo loginInfo) {
        //任务类型
        LambdaQueryWrapper<SysTaskType> wrapper = new LambdaQueryWrapper<>();
        List<SysTaskType> taskTypeList = sysTaskTypeMapper.selectList(wrapper);
        //任务内容
        LambdaQueryWrapper<SysTask> taskwrapper = new LambdaQueryWrapper<>();
        List<SysTask> taskList = baseMapper.selectList(taskwrapper);
        //会员当日已完成任务记录
        List<MemTaskRecord> memTaskRecordList = memTaskRecordMapper.findTodayTaskRecord(loginInfo.getId());
        //结果集处理
        for (SysTask tl : taskList) {
            MemTaskRecord memTaskRecord = new MemTaskRecord();
            memTaskRecord.setMemId(loginInfo.getId());
            memTaskRecord.setTaskId(tl.getId());
            memTaskRecord.setTypeId(tl.getTypeId());
            memTaskRecord.setTaskCode(tl.getCode());
            memTaskRecord.setRewardAmount(tl.getBonusAmount());
            memTaskRecord.setStates(0);
            tl.setReceive(0);
            for (MemTaskRecord mtrl : memTaskRecordList) {
                if (mtrl.getTaskId().equals(tl.getId())) {
                    if (mtrl.getStates().equals(0)) {
                        tl.setReceive(1);
                        break;
                    }
                    if (mtrl.getStates().equals(1)) {
                        tl.setReceive(2);
                        break;
                    }
                }
            }
            if (!tl.getReceive().equals(0)) {
                continue;
            }
            //限时银行卡转账
            if (tl.getCode().equals(TaskEnum.BANKCARDTRANSFER.getCode())) {
                BigDecimal amount = baseMapper.findMemAmountTransferredToday(loginInfo.getId(),GoldchangeEnum.YHZZ.getCode());
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                tl.setReceive(1);
                memTaskRecordMapper.insert(memTaskRecord);
            }
            //全民推广
            if (tl.getCode().equals(TaskEnum.MEMPOPULARIZED.getCode())) {
                Integer num = baseMapper.findMemSubNum(loginInfo.getId());
                JSONObject json = JSONObject.parseObject(tl.getConditionJson());
                List<MemTaskRecord> extensionList = memTaskRecordMapper.findTaskRecordByCode(loginInfo.getId(),tl.getCode());
                AtomicReference<Integer> unclaimed= new AtomicReference<>(0);
                AtomicReference<Integer> Received= new AtomicReference<>(0);
                extensionList.forEach(l->{
                    if(l.getStates().equals(0)){
                        unclaimed.getAndSet(unclaimed.get() + 1);
                    }
                    if(l.getStates().equals(1)){
                        Received.getAndSet(Received.get() + 1);
                    }
                });
                if(unclaimed.get()>0){
                    tl.setReceive(1);
                }
                if(Received.get()>0){
                    tl.setReceive(2);
                }
                json.keySet().forEach(j -> {
                    AtomicReference<Boolean> un = new AtomicReference<>(true);
                    extensionList.forEach(l->{
                        if(l.getRemark().equals(j)){
                            un.set(false);
                        }
                    });
                    if (num>=Integer.valueOf(j)&&un.get()) {
                        tl.setReceive(1);
                        memTaskRecord.setRemark(j);
                        memTaskRecord.setRewardAmount(json.getBigDecimal(j));
                        memTaskRecordMapper.insert(memTaskRecord);
                    }
                });
            }
            //捕鱼打码达标
            if (tl.getCode().equals(TaskEnum.FISHINGSTANDARD.getCode())) {
                BigDecimal amount = memTaskRecordMapper.findMemBetAmountByGameType(loginInfo.getId(),TaskEnum.FISHING.getCode());
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal condition = new BigDecimal(tl.getCondition());
                if(amount.compareTo(condition)<=0){
                    continue;
                }
                tl.setReceive(1);
                memTaskRecordMapper.insert(memTaskRecord);
            }
            //电子打码达标
            if (tl.getCode().equals(TaskEnum.ELECTRONICSTANDARD.getCode())) {
                BigDecimal amount = memTaskRecordMapper.findMemBetAmountByGameType(loginInfo.getId(),TaskEnum.SLOTS.getCode());
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal condition = new BigDecimal(tl.getCondition());
                if(amount.compareTo(condition)<=0){
                    continue;
                }
                tl.setReceive(1);
                memTaskRecordMapper.insert(memTaskRecord);
            }
            //真人打码达标
            if (tl.getCode().equals(TaskEnum.REALPERSONSTANDARD.getCode())) {
                BigDecimal amount = memTaskRecordMapper.findMemBetAmountByGameType(loginInfo.getId(),TaskEnum.LIVE.getCode());
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal condition = new BigDecimal(tl.getCondition());
                if(amount.compareTo(condition)<=0){
                    continue;
                }
                tl.setReceive(1);
                memTaskRecordMapper.insert(memTaskRecord);
            }
            //棋牌打码达标
            if (tl.getCode().equals(TaskEnum.CHESSSTANDARD.getCode())) {
                BigDecimal amount = memTaskRecordMapper.findMemBetAmountByGameType(loginInfo.getId(),TaskEnum.POKER.getCode());
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal condition = new BigDecimal(tl.getCondition());
                if(amount.compareTo(condition)<=0){
                    continue;
                }
                tl.setReceive(1);
                memTaskRecordMapper.insert(memTaskRecord);
            }
        }
        //结果集封装
        List<TaskModuleVo> list = new ArrayList<>();
        taskTypeList.forEach(l->{
            TaskModuleVo taskModuleVo = new TaskModuleVo();
            taskModuleVo.setTaskTypeName(l.getName());
            List<SysTask> sysTaskList = new ArrayList<>();
            taskList.forEach(tl->{
                if(tl.getTypeId().equals(l.getId())){
                    sysTaskList.add(tl);
                }
            });
            taskModuleVo.setTaskList(sysTaskList);
            list.add(taskModuleVo);
        });
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveTaskReward(LoginInfo loginInfo, Integer taskId) {
        LambdaQueryWrapper<MemTaskRecord> taskwrapper = new LambdaQueryWrapper<>();
        taskwrapper.eq(MemTaskRecord::getMemId,loginInfo.getId());
        taskwrapper.eq(MemTaskRecord::getTaskId,taskId);
        taskwrapper.eq(MemTaskRecord::getStates,0);
        List<MemTaskRecord> memTaskRecordList = memTaskRecordMapper.selectList(taskwrapper);
        if(memTaskRecordList.isEmpty()){
            throw new BizException("错误的领取，无可领取任务奖励");
        }
        memTaskRecordList.forEach(l->{
            MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
            agentRebateChange.setChangeAmount(l.getRewardAmount());
            agentRebateChange.setTradingEnum(TradingEnum.INCOME);
            agentRebateChange.setGoldchangeEnum(GoldchangeEnum.TASK);
            agentRebateChange.setUserId(loginInfo.getId());
            iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
            l.setStates(1);
            l.setUpdateTime(new Date());
            memTaskRecordMapper.insert(l);
        });
    }
}
