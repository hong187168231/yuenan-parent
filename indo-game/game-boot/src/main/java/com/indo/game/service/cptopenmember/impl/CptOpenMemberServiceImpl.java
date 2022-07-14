package com.indo.game.service.cptopenmember.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.game.mapper.cptopenmember.CptOpenMemberMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.service.cptopenmember.CptOpenMemberService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CptOpenMemberServiceImpl implements CptOpenMemberService {


    @Autowired
    private CptOpenMemberMapper cptOpenMemberMapper;

    @Override
    public CptOpenMember getCptOpenMember(Integer userId, String type) {
        CptOpenMember cptOpenMember = null;
        if (userId != null && StringUtils.isNotBlank(type)) {
            LambdaQueryWrapper<CptOpenMember> wrapper = new LambdaQueryWrapper();
            wrapper.eq(CptOpenMember::getUserId, userId);
            wrapper.eq(CptOpenMember::getType, type);
            cptOpenMember = cptOpenMemberMapper.selectOne(wrapper);
        }
        return cptOpenMember;
    }

    @Override
    public CptOpenMember getCptOpenMember(String userAcct, String type) {
        CptOpenMember cptOpenMember = null;
        if (userAcct != null && StringUtils.isNotBlank(type)) {
            LambdaQueryWrapper<CptOpenMember> wrapper = new LambdaQueryWrapper();
            wrapper.eq(CptOpenMember::getUserName, userAcct);
            wrapper.eq(CptOpenMember::getType, type);
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
    public CptOpenMember quertCptOpenMember(String token, String type) {
        CptOpenMember cptOpenMember = null;
        if (StringUtils.isNotEmpty(token) && StringUtils.isNotBlank(type)) {
            LambdaQueryWrapper<CptOpenMember> wrapper = new LambdaQueryWrapper();
            wrapper.eq(CptOpenMember::getPassword, token);
            wrapper.eq(CptOpenMember::getType, type);
            cptOpenMember = cptOpenMemberMapper.selectOne(wrapper);
        }
        return cptOpenMember;
    }


}
