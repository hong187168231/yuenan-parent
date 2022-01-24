package com.indo.job.executor.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.mem.MemBetVo;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.BrokerMessageStatus;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.ViewUtil;
import com.indo.core.brokery.RabbitBroker;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.AgentRebate;
import com.indo.core.pojo.entity.AgentRebateRecord;
import com.indo.core.pojo.entity.BrokerMessage;
import com.indo.core.pojo.entity.GameTxns;
import com.indo.core.service.IBrokerMessageService;
import com.indo.core.service.IMemGoldChangeService;
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
    private void agentRebateJob() {
        List<MemBetVo> list = RedisUtils.get(RedisKeys.SYS_REBATE_KEY);
        IPage<BeforeDayBetDTO> page = new Page<>();
        List<BeforeDayBetDTO> dataList = gameTxnsMapper.beforeDayBetList(page);
        dataList.forEach(BeforeData -> {
            BigDecimal teamSumBet = gameTxnsMapper.teamSumBet(BeforeData.getAccount());
            BigDecimal yesterdayRemain = agentRebateRecordMapper.yesterdayRemain(BeforeData.getAccount());
            Map<String, BigDecimal> rebateMap = getSubRebateAmount(list, teamSumBet.subtract(yesterdayRemain));
            BigDecimal rebateAmount = rebateMap.get("rebateValue");
            BigDecimal todayRemain = rebateMap.get("todayRemain");

            AgentRebateRecord agentRebateRecord = new AgentRebateRecord();
            agentRebateRecord.setMemId(BeforeData.getMemId());
            agentRebateRecord.setAccount(BeforeData.getAccount());
            agentRebateRecord.setCreateUser("job");
            agentRebateRecord.setRebateAmount(rebateAmount);
            agentRebateRecord.setTodayRemain(todayRemain);
            agentRebateRecord.setMemLevel(BeforeData.getMemLevel());
            agentRebateRecord.setRealName(BeforeData.getRealName());
            agentRebateRecord.setSuperior(BeforeData.getSuperior());
            agentRebateRecord.setTeamNum(BeforeData.getTeamNum());
            agentRebateRecordMapper.insert(agentRebateRecord);

            MemGoldChangeDTO agentRebateChange = new MemGoldChangeDTO();
            agentRebateChange.setChangeAmount(rebateAmount);
            agentRebateChange.setTradingEnum(TradingEnum.INCOME);
            agentRebateChange.setGoldchangeEnum(GoldchangeEnum.DLFY);
            agentRebateChange.setUserId(BeforeData.getMemId());
            iMemGoldChangeService.updateMemGoldChange(agentRebateChange);

        });
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
