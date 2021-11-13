package com.indo.game.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.constant.GameMoneyRecordEnum;
import com.indo.game.mapper.CptOpenMemberMapper;
import com.indo.game.mapper.GameMoneyRecordMapper;
import com.indo.game.mapper.MemberBeanMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.GameMoneyRecord;
import com.indo.game.pojo.entity.GameMoneyRecordExample;
import com.indo.game.services.ExternalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalServiceImpl implements ExternalService {


    @Autowired
    private CptOpenMemberMapper cptOpenMemberMapper;

    @Autowired
    private GameMoneyRecordMapper gameMoneyRecordMapper;

    @Override
    public CptOpenMember getCptOpenMember(Integer userId, String type) {
        CptOpenMember cptOpenMember = null;
        if (userId != null && StringUtils.isNotBlank(type)) {
            LambdaQueryWrapper<CptOpenMember> wrapper = new LambdaQueryWrapper();
            wrapper.eq(CptOpenMember::getUserId,userId);
            wrapper.eq(CptOpenMember::getType,type);
            cptOpenMember = cptOpenMemberMapper.selectOne(wrapper);
        }
        return cptOpenMember;
    }

    @Override
    public void saveCptOpenMember(CptOpenMember cptOpenMember) {
        cptOpenMemberMapper.insert(cptOpenMember);
    }

    @Override
    public void updateCptOpenMember(CptOpenMember cptOpenMember) {
        cptOpenMemberMapper.updateById(cptOpenMember);
    }

    @Override
    public GameMoneyRecord getGameMoneyRecord(int type, Integer memberId) {
        GameMoneyRecordExample example = new GameMoneyRecordExample();
        example.setOrderByClause("`update_time` desc");
        GameMoneyRecordExample.Criteria criteria = example.createCriteria();
        //用户id
        criteria.andUserIdEqualTo(memberId);
        //游戏类型
        criteria.andTypeEqualTo(type);
        GameMoneyRecord gameMoneyRecord = gameMoneyRecordMapper.selectOneByExample(example);
        return gameMoneyRecord;
    }

    @Override
    public Map<String, Object> getRecordBoolean(Integer value, Integer userId) {
        GameMoneyRecord record = this.getGameMoneyRecord(value, userId);
        if (null == record) {
            return new HashMap<>();
        }
        Integer operate = record.getOperate();
        String opStatus = record.getOpStatus();
        Map<String, Object> booleanMap = new HashMap<>();
        //上分成功
        if (operate.equals(GameMoneyRecordEnum.OPERATE_IN.getValue()) && opStatus.equals(GameMoneyRecordEnum.SUCCESS.getKey())) {
            booleanMap.put(Constants.IN_SUCCESS, true);
            booleanMap.put("result", record);
        }
        //上分失败
        if (operate.equals(GameMoneyRecordEnum.OPERATE_IN.getValue()) && opStatus.equals(GameMoneyRecordEnum.FAIL.getKey())) {
            booleanMap.put(Constants.IN_FAIL, true);
            booleanMap.put("result", record);
        }
        //下分成功
        if (operate.equals(GameMoneyRecordEnum.OPERATE_OUT.getValue()) && opStatus.equals(GameMoneyRecordEnum.SUCCESS.getKey())) {
            booleanMap.put(Constants.OUT_SUCCESS, true);
            booleanMap.put("result", record);
        }
        //下分失败
        if (operate.equals(GameMoneyRecordEnum.OPERATE_OUT.getValue()) && opStatus.equals(GameMoneyRecordEnum.FAIL.getKey())) {
            booleanMap.put(Constants.OUT_FAIL, true);
            booleanMap.put("result", record);
        }
        return booleanMap;
    }


}
