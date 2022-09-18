package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.msg.mapper.MsgStationLetterMapper;
import com.indo.admin.modules.msg.service.IMsgStationLetterService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgStationLetterVO;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.req.msg.StationLetterAddReq;
import com.indo.user.pojo.req.msg.StationLetterQueryReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private MsgStationLetterMapper letterMapper;

    @Resource
    private MemBaseinfoMapper memBaseinfoMapper;

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
                MemBaseInfoBO memBaseInfoBO =memBaseinfoMapper.findMemBaseInfoByAccount(item);
                letter.setMemId(memBaseInfoBO.getId());
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
        wrapper.eq(MsgStationLetter::getIsDel,false);
        Page<MsgStationLetter> pageList = baseMapper.selectPage(page, wrapper);
        return DozerUtil.convert(pageList.getRecords(), MsgStationLetterVO.class);
    }

    @Override
    public int personalMsgTotal(MsgDTO msgDTO) {
        LambdaQueryWrapper<MsgStationLetter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgStationLetter::getMemId, msgDTO.getMemId());
        wrapper.eq(MsgStationLetter::getIsDel,false);
        if (StringUtils.isNotBlank(msgDTO.getBeginTime())) {
            wrapper.ge(MsgStationLetter::getCreateTime, msgDTO.getBeginTime());
        }
        if (StringUtils.isNotBlank(msgDTO.getEndTime())) {
            wrapper.le(MsgStationLetter::getCreateTime, msgDTO.getEndTime());
        }
        Integer total = baseMapper.selectCount(wrapper);
        return total;
    }

    @Override
    public void deleteMsg(MsgDTO msgDTO, HttpServletRequest request) {
        String countryCode = request.getHeader("countryCode");
        if(msgDTO.getMsgId()==null){
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        MsgStationLetter msgStationLetter = baseMapper.selectById(msgDTO.getMsgId());
        if(msgStationLetter==null){
            throw new BizException(MessageUtils.get(ResultCode.DATA_NONENTITY.getCode(),countryCode));
        }
        msgStationLetter.setIsDel(true);
        baseMapper.updateById(msgStationLetter);
    }
}
