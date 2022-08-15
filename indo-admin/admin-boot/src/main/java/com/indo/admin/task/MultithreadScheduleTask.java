package com.indo.admin.task;

import com.indo.admin.common.util.DateUtil;
import com.indo.admin.modules.agent.mapper.AgentRebateRecordMapper;
import com.indo.admin.modules.game.mapper.AdminTxnsMapper;
import com.indo.admin.pojo.dto.BeforeDayDTO;
import com.indo.core.pojo.vo.mem.MemBetVo;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.ViewUtil;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.AgentRebateRecord;
import com.indo.core.service.IMemGoldChangeService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@EnableScheduling
@EnableAsync
public class MultithreadScheduleTask {
    @Resource
    private AgentRebateRecordMapper agentRebateRecordMapper;
    @Resource
    private AdminTxnsMapper adminTxnsMapper;
    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    /**
     * 每天凌晨1点结算前一天代理返佣（因JOB目前处于不可使用的情况所以先在此处理）
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void agentRebateJob() {
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        List<BeforeDayDTO> dataList = adminTxnsMapper.beforeDayBetList(DateUtil.yesterdayFirstDate(),DateUtil.yesterdayLastDate());
        for(BeforeDayDTO BeforeData:dataList){
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
        }
    }
}
