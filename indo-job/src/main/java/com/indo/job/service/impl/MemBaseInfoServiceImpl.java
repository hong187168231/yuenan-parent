package com.indo.job.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.job.mapper.MemBaseInfoMapper;
import com.indo.job.service.IMemBaseinfoService;
import com.indo.job.service.IMemLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemBaseInfoServiceImpl extends SuperServiceImpl<MemBaseInfoMapper, MemBaseinfo> implements IMemBaseinfoService {

    @Resource
    private MemBaseInfoMapper memBaseInfoMapper;

    @Autowired
    private IMemLevelService iMemLevelService;



    @Override
    public void upLevel(String payLoad) {
        Message message = JSONObject.parseObject(payLoad, Message.class);
        Long memId = (Long) message.getAttributes().get("memId");
        MemBaseinfo memBaseinfo = baseMapper.selectById(memId);
        if (memBaseinfo.getTotalDeposit().intValue() > 10000
                && memBaseinfo.getTotalBet().intValue() > 10000) {
            Integer level = iMemLevelService.getLevelByCondition(memBaseinfo.getTotalDeposit(), memBaseinfo.getTotalBet());
            if (level > memBaseinfo.getMemLevel()) {
                memBaseinfo.setMemLevel(level);
                baseMapper.updateById(memBaseinfo);
            }
        }
    }

    @Override
    public List<Long> findIdListByCreateTime(Date addDay) {
        return null;
    }
}
