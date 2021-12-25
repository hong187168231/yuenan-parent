package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.user.pojo.entity.MemGiftReceive;
import com.indo.user.pojo.req.gift.GiftReceiveReq;

/**
 * <p>
 * 礼金领取 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-22
 */
public interface IMemGiftReceiveService extends IService<MemGiftReceive> {

    boolean saveMemGiftReceive(GiftReceiveReq giftReceiveReq, LoginInfo loginInfo);

}
