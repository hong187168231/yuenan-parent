package com.indo.job.executor.jobhandler;

import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.ViewUtil;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.AgentRebateRecord;
import com.indo.core.pojo.vo.mem.MemBetVo;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.job.common.util.DateUtil;
import com.indo.job.mapper.JobAgentRebateRecordMapper;
import com.indo.job.mapper.GameTxnsMapper;
import com.indo.job.pojo.dto.BeforeDayBetDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class AgentRebateJob {


    @Resource
    private GameTxnsMapper gameTxnsMapper;

    @Resource
    private JobAgentRebateRecordMapper agentRebateRecordMapper;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;
    /**
     * 每天凌晨1点结算前一天代理返佣（因JOB目前处于不可使用的情况所以先在此处理）
     */
    @XxlJob("agentRebateJob")
    public void agentRebateJob() {
        log.info("执行代理返佣任务时间: " + LocalDateTime.now());
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        List<BeforeDayBetDTO> dataList = gameTxnsMapper.beforeDayBetList(DateUtil.yesterdayFirstDate(),DateUtil.yesterdayLastDate());
        for(BeforeDayBetDTO BeforeData:dataList){
            if(StringUtils.isEmpty(BeforeData.getSuperior())){
                continue;
            }
            AtomicReference<BigDecimal> rebateAmount = new AtomicReference<>();
            list.forEach(l->{
                if(BeforeData.getRealBetAmount().intValue()>=l.getSubTotalBet()){
                    BigDecimal  ratio = new BigDecimal(l.getRebateValue()).divide(new BigDecimal(100));
                    rebateAmount.set(new BigDecimal(l.getSubTotalBet()).multiply(ratio));
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
            log.info("代理返佣任务结束");
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
