package com.indo.job.executor.jobhandler;

import com.indo.admin.pojo.vo.mem.MemBetVo;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.ViewUtil;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.AgentRebateRecord;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.job.common.util.DateUtil;
import com.indo.job.mapper.AgentRebateRecordMapper;
import com.indo.job.mapper.GameTxnsMapper;
import com.indo.job.pojo.dto.BeforeDayBetDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class AgentRebateJob {


    @Autowired
    private GameTxnsMapper gameTxnsMapper;

    @Autowired
    private AgentRebateRecordMapper agentRebateRecordMapper;

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;

    @XxlJob("agentRebateJob")
    public void agentRebateJob() {
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        List<BeforeDayBetDTO> dataList = gameTxnsMapper.beforeDayBetList(DateUtil.yesterdayFirstDate(),DateUtil.yesterdayLastDate());
        for(BeforeDayBetDTO BeforeData:dataList){
            if(StringUtils.isEmpty(BeforeData.getSuperior())){
                continue;
            }
            AtomicReference<BigDecimal> rebateAmount = new AtomicReference<>();
            list.forEach(l->{
                if(BeforeData.getRealBetAmount().intValue()>=l.getSubTotalBet()){
                    rebateAmount.set(new BigDecimal(l.getRebateValue()));
                }
            });
            if(rebateAmount==null||rebateAmount.get().intValue()<=0){
                continue;
            }
            BigDecimal amount = ViewUtil.getTradeOffAmount(rebateAmount.get());

            AgentRebateRecord agentRebateRecord = new AgentRebateRecord();
            agentRebateRecord.setMemId(BeforeData.getParentId());
            agentRebateRecord.setAccount(BeforeData.getAccount());
            agentRebateRecord.setCreateUser("job");
            agentRebateRecord.setRebateAmout(amount);
            agentRebateRecord.setMemLevel(BeforeData.getMemLevel());
            agentRebateRecord.setRealName(BeforeData.getRealName());
            agentRebateRecord.setSuperior(BeforeData.getSuperior());
            agentRebateRecord.setTeamNum(BeforeData.getTeamNum());
            agentRebateRecord.setTeamAmout(BeforeData.getRealBetAmount());
            agentRebateRecordMapper.insert(agentRebateRecord);

            MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
            agentRebateChange.setChangeAmount(amount);
            agentRebateChange.setTradingEnum(TradingEnum.INCOME);
            agentRebateChange.setGoldchangeEnum(GoldchangeEnum.DLFY);
            agentRebateChange.setUserId(BeforeData.getParentId());
            iMemGoldChangeService.updateMemGoldChange(agentRebateChange);
        }
    }


    private Map<String, BigDecimal> getSubRebateAmount(List<MemBetVo> list, BigDecimal teamAndRemainAmount) {
        Map rebateMap = new HashMap();
        BigDecimal rebateValue = ViewUtil.getTradeOffAmount(null);
        BigDecimal todayRemain = ViewUtil.getTradeOffAmount(null);
        for (int i = 0; i < list.size(); i++) {
            if (teamAndRemainAmount.intValue() < list.get(0).getSubTotalBet()) {
                rebateValue = ViewUtil.getTradeOffAmount(null);
                todayRemain = ViewUtil.getTradeOffAmount(teamAndRemainAmount);
                break;
            }
            MemBetVo betBo = list.get(i);
            if (teamAndRemainAmount.intValue() > betBo.getSubTotalBet()) {
                rebateValue = new BigDecimal(betBo.getRebateValue());
                todayRemain = teamAndRemainAmount.subtract(new BigDecimal(betBo.getSubTotalBet()));
                continue;
            }
        }
        rebateMap.put("rebateValue", ViewUtil.getTradeOffAmount(rebateValue));
        rebateMap.put("todayRemain", ViewUtil.getTradeOffAmount(todayRemain));
        return rebateMap;
    }

}
