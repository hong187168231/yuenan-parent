package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.msg.mapper.MsgPushRecordMapper;
import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.admin.pojo.dto.MsgDTO;
import com.indo.admin.pojo.vo.msg.MsgPushRecordVO;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.MsgPushRecord;
import com.indo.core.pojo.entity.MsgStationLetter;
import com.indo.user.pojo.req.msg.PushRecordAddReq;
import com.indo.user.pojo.req.msg.PushRecordQueryReq;
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
    public Page<MsgPushRecordVO> queryList(PushRecordQueryReq queryDTO) {
        Page<MsgPushRecordVO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        List<MsgPushRecordVO> list = pushRecordMapper.queryList(page, queryDTO);
        page.setRecords(list);
        return page;
    }

    @Override
    public void add(PushRecordAddReq pushRecordAddDTO) {
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
        // 等于0是全部设备类型
        wrapper.eq(MsgPushRecord::getDeviceType, msgDTO.getDeviceType())
               .or()
               .eq(MsgPushRecord::getDeviceType, 0);
        wrapper.eq(MsgPushRecord::getIsDel,false);
        Page<MsgPushRecord> pageList = baseMapper.selectPage(page, wrapper);
        return DozerUtil.convert(pageList.getRecords(), MsgPushRecordVO.class);
    }

    @Override
    public int sysMsgTotal(MsgDTO msgDTO) {
        LambdaQueryWrapper<MsgPushRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgPushRecord::getDeviceType, msgDTO.getDeviceType());
        wrapper.eq(MsgPushRecord::getIsDel,false);
        if (StringUtils.isNotBlank(msgDTO.getBeginTime())) {
            wrapper.ge(MsgPushRecord::getCreateTime, msgDTO.getBeginTime());
        }
        if (StringUtils.isNotBlank(msgDTO.getEndTime())) {
            wrapper.le(MsgPushRecord::getCreateTime, msgDTO.getEndTime());
        }
        Integer total = baseMapper.selectCount(wrapper);
        return total;
    }

    @Override
    public void deleteMsg(MsgDTO msgDTO) {
       if(msgDTO.getMsgId()==null){
           throw new BizException("主要参数不可为空");
       }
        MsgPushRecord msgPushRecord = baseMapper.selectById(msgDTO.getMsgId());
       if(msgPushRecord==null){
           throw new BizException("数据不存在");
       }
        msgPushRecord.setIsDel(true);
        baseMapper.updateById(msgPushRecord);
    }
}
