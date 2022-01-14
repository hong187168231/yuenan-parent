package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.msg.mapper.MsgPushRecordMapper;
import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.MsgPushRecordVO;
import com.indo.admin.pojo.vo.MsgStationLetterVO;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.MsgPushRecord;
import com.indo.user.pojo.dto.PushRecordAddDTO;
import com.indo.user.pojo.dto.PushRecordQueryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台推送记录表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-09-07
 */
@Service
public class MsgPushRecordServiceImpl extends ServiceImpl<MsgPushRecordMapper, MsgPushRecord> implements IMsgPushRecordService {

    @Autowired
    private MsgPushRecordMapper pushRecordMapper;

    @Override
    public Page<MsgPushRecordVO> queryList(PushRecordQueryDTO queryDTO) {
        Page<MsgPushRecordVO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        List<MsgPushRecordVO> list = pushRecordMapper.queryList(page, queryDTO);
        page.setRecords(list);
        return page;
    }

    @Override
    public void add(PushRecordAddDTO pushRecordAddDTO) {
        // 向客户端推送 todo
        MsgPushRecord pushRecord = new MsgPushRecord();
        pushRecord.setCreateUser(JwtUtils.getUsername());
        BeanUtils.copyProperties(pushRecordAddDTO, pushRecord);
        // 保存到记录表
        pushRecordMapper.insert(pushRecord);
    }


    @Override
    public List<MsgPushRecordVO> getSysMsg(MsgDTO msgDTO) {
        Page<MsgPushRecord> page = new Page<>(msgDTO.getPage(), msgDTO.getLimit());
        LambdaQueryWrapper<MsgPushRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgPushRecord::getDeviceType, msgDTO.getDeviceType());
        Page<MsgPushRecord> pageList = baseMapper.selectPage(page, wrapper);
        return DozerUtil.convert(pageList.getRecords(), MsgPushRecordVO.class);
    }
}
