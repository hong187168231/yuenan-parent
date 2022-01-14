package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemLevelMapper;
import com.indo.admin.modules.msg.mapper.MsgStationLetterMapper;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.req.msg.StationLetterAddReq;
import com.indo.user.pojo.req.msg.StationLetterQueryReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private DozerUtil dozerUtil;

    @Override
    public Page<MsgStationLetterVO> queryList(StationLetterQueryReq queryDTO) {
        Page<MsgStationLetterVO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        List<MsgStationLetterVO> list = letterMapper.queryList(page, queryDTO);
        page.setRecords(list);
        return page;
    }

    @Override
    public int add(StationLetterAddReq letterDTO) {
        MsgStationLetter letter = new MsgStationLetter();
        BeanUtils.copyProperties(letterDTO, letter);
//        letter.setCreateTime(new Date());
        if (letterDTO.getSendType() == 1) {
            List<String> list = letterDTO.getReceiver();
            list.forEach(item -> {
                letter.setReceiver(item);
                letterMapper.insert(letter);
            });
        }
        return 1;
    }

    @Override
    public List<MsgStationLetterVO> getPersonalMsg(MsgDTO msgDTO) {
        Page<MsgStationLetter> page = new Page<>(msgDTO.getPage(), msgDTO.getLimit());
        LambdaQueryWrapper<MsgStationLetter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgStationLetter::getMemId, msgDTO.getMemId());
        Page<MsgStationLetter> pageList = baseMapper.selectPage(page, wrapper);
        return DozerUtil.convert(pageList.getRecords(), MsgStationLetterVO.class);
    }
}
