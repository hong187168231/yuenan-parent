package com.live.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.live.admin.modules.mem.mapper.MemBaseInfoMapper;
import com.live.admin.modules.mem.mapper.MemLevelMapper;
import com.live.admin.modules.msg.mapper.MsgStationLetterMapper;
import com.live.admin.modules.msg.service.IMsgStationLetterService;
import com.live.admin.modules.pay.mapper.PayGroupConfigMapper;
import com.live.common.mybatis.base.PageResult;
import com.live.user.pojo.dto.MsgStationLetterDTO;
import com.live.user.pojo.entity.MsgStationLetter;
import com.live.user.pojo.vo.MsgStationLetterVO;
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
    private MemBaseInfoMapper memBaseInfoMapper;
    @Autowired
    private MemLevelMapper levelMapper;
    @Autowired
    private MsgStationLetterMapper letterMapper;
    @Autowired
    private PayGroupConfigMapper payGroupConfigMapper;

    @Override
    public PageResult<MsgStationLetterVO> queryList(MsgStationLetterDTO letterDTO) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != letterDTO.getPage() && null != letterDTO.getLimit()){
            pageNum = letterDTO.getPage();
            pageSize = letterDTO.getLimit();
        }
        Page<MsgStationLetterVO> page =  new Page<>(pageNum, pageSize);
        List<MsgStationLetterVO> list = letterMapper.queryList(page, letterDTO);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public int add(MsgStationLetterDTO letterDTO) {
        MsgStationLetter letter = new MsgStationLetter();
        BeanUtils.copyProperties(letterDTO,letter);
        letter.setCreateTime(new Date());

        // 按收件人发送
        if(letterDTO.getType() == 1){
            List<String> nickNames = memBaseInfoMapper.selectNickNameByAccounts(letterDTO.getReceiver());
            letter.setReceiver(StringUtils.strip(letterDTO.getReceiver().toString(),"[]"));
            letter.setReceiverName(StringUtils.strip(nickNames.toString(),"[]"));
        }
        // 按会员等级发送
        if(letterDTO.getType() == 2){
            List<String> names = levelMapper.selectNameByIds(letterDTO.getReceiver());
            letter.setReceiver(StringUtils.strip(letterDTO.getReceiver().toString(),"[]"));
            letter.setReceiverName(StringUtils.strip(names.toString(),"[]"));
        }

        // 按支付层级发送
        if(letterDTO.getType() == 3){
            List<String> names = payGroupConfigMapper.selectNameByIds(letterDTO.getReceiver());
            letter.setReceiver(StringUtils.strip(letterDTO.getReceiver().toString(),"[]"));
            letter.setReceiverName(StringUtils.strip(names.toString(),"[]"));
        }

        // 发送到客户端 todo

        return letterMapper.insert(letter);
    }
}
