package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.msg.mapper.MsgStationLetterMapper;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.modules.pay.mapper.PayGroupConfigMapper;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MsgStationLetterDTO;
import com.indo.user.pojo.dto.StationLetterAddDTO;
import com.indo.user.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.vo.MsgStationLetterVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 站内信 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Service
public class MsgStationLetterServiceImpl extends ServiceImpl<MsgStationLetterMapper, MsgStationLetter> implements IMsgStationLetterService {

    @Autowired
    private MemBaseinfoMapper memBaseInfoMapper;
    @Autowired
    private MemLevelMapper levelMapper;
    @Autowired
    private MsgStationLetterMapper letterMapper;
    @Autowired
    private PayGroupConfigMapper payGroupConfigMapper;

    @Override
    public Page<MsgStationLetterVO> queryList(MsgStationLetterDTO letterDTO) {
        Page<MsgStationLetterVO> page = new Page<>(letterDTO.getPage(), letterDTO.getLimit());
        List<MsgStationLetterVO> list = letterMapper.queryList(page, letterDTO);
        page.setRecords(list);
        return page;
    }

    @Override
    public int add(StationLetterAddDTO letterDTO) {
        MsgStationLetter letter = new MsgStationLetter();
        BeanUtils.copyProperties(letterDTO, letter);
        letter.setCreateTime(new Date());
        if (letterDTO.getSendType() == 1) {
            List<String> nickNames = memBaseInfoMapper.selectNickNameByAccounts(letterDTO.getReceiver());
            letter.setReceiver(StringUtils.strip(letterDTO.getReceiver().toString(), "[]"));
        }
        // 发送到客户端 todo
        return letterMapper.insert(letter);
    }
}
