package com.indo.admin.modules.msg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.msg.mapper.MsgPushRecordMapper;
import com.indo.admin.modules.msg.service.IMsgPushRecordService;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.MsgPushRecordDTO;
import com.indo.user.pojo.entity.MsgPushRecord;
import com.indo.user.pojo.vo.MsgPushRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public PageResult<MsgPushRecordVO> queryList(MsgPushRecordDTO pushRecordDTO) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != pushRecordDTO.getPage() && null != pushRecordDTO.getLimit()){
            pageNum = pushRecordDTO.getPage();
            pageSize = pushRecordDTO.getLimit();
        }
        Page<MsgPushRecordVO> page =  new Page<>(pageNum, pageSize);
        List<MsgPushRecordVO> list = pushRecordMapper.queryList(page, pushRecordDTO);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public void add(MsgPushRecord pushRecord) {
        // 向客户端推送 todo

        // 保存到记录表
        pushRecord.setCreateTime(new Date());
        pushRecordMapper.insert(pushRecord);
    }
}
