package com.indo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.user.mapper.MemGiftReceiveMapper;
import com.indo.user.pojo.entity.MemGiftReceive;
import com.indo.user.pojo.req.gift.GiftReceiveReq;
import com.indo.user.service.IMemGiftReceiveService;
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

    @Override
    public boolean saveMemGiftReceive(GiftReceiveReq giftReceiveReq, LoginInfo loginInfo) {
        MemGiftReceive memGiftReceive = new MemGiftReceive();
        memGiftReceive.setMemId(loginInfo.getId());
        memGiftReceive.setGiftType(giftReceiveReq.getGiftTypeEnum().getCode());
        memGiftReceive.setGiftCode(giftReceiveReq.getGiftNameEnum().name());
        memGiftReceive.setGiftName(giftReceiveReq.getGiftNameEnum().getName());
        return this.baseMapper.insert(memGiftReceive) > 0;
    }
}
