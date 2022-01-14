package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemGiftReceiveMapper;
import com.indo.admin.modules.mem.service.IMemGiftReceiveService;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.core.pojo.entity.MemGiftReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 活动类型表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-22
 */
@Service
public class MemGiftReceiveServiceImpl extends ServiceImpl<MemGiftReceiveMapper, MemGiftReceive> implements IMemGiftReceiveService {

    @Autowired
    private MemGiftReceiveMapper memGiftReceiveMapper;


    @Override
    public Page<MemGiftReceiveVO> receiveList(GiftReceiveDTO queryDto) {
        Page<MemGiftReceiveVO> page = new Page<>(queryDto.getPage(), queryDto.getLimit());
        List<MemGiftReceiveVO> list = memGiftReceiveMapper.receiveList(page, queryDto);
        page.setRecords(list);
        return page;
    }
}
