package com.indo.job.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.mybatis.base.service.impl.SuperServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.common.result.Result;
import com.indo.common.utils.BaseUtil;
import com.indo.common.utils.CopyUtils;
import com.indo.common.utils.NameGeneratorUtil;
import com.indo.common.web.exception.BizException;
import com.indo.job.mapper.MemBaseInfoMapper;
import com.indo.job.service.IMemBaseinfoService;
import com.indo.job.service.IMemLevelService;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.entity.MemInviteCode;
import com.indo.user.pojo.req.LogOutReq;
import com.indo.user.pojo.req.LoginReq;
import com.indo.user.pojo.req.RegisterReq;
import com.indo.user.pojo.req.mem.MemInfoReq;
import com.indo.user.pojo.req.mem.UpdateBaseInfoReq;
import com.indo.user.pojo.req.mem.UpdatePasswordReq;
import com.indo.user.pojo.vo.AppLoginVo;
import com.indo.user.pojo.vo.mem.MemBaseInfoVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
