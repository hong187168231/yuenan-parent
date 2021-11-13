package com.indo.game.services.impl;

import com.indo.game.mapper.CptOpenMemberMapper;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.services.CptOpenMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CptOpenMemberServiceImpl implements CptOpenMemberService {

    @Autowired
    private CptOpenMemberMapper cptOpenMemberMapper;

    @Override
    public int updateByPrimaryKeySelective(CptOpenMember gameOpenMember) {
        return  cptOpenMemberMapper.updateById(gameOpenMember);
    }
}
