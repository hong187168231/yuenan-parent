package com.indo.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.GiftReceiveDTO;
import com.indo.admin.pojo.vo.mem.MemGiftReceiveVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.DateUtils;
import com.indo.user.pojo.entity.MemGiftReceive;
import com.indo.user.pojo.req.gift.GiftReceiveReq;

import java.util.Date;

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

    MemGiftReceive findGiftByCodeAndMemId(String giftCode, Long memId);

    Integer countRewardReceive(Long memId, String giftCode, Integer upLevel);

    Integer countTodayReceive(Long memId, String giftCode);

    Integer countWeekReceive(Long memId, String giftCode);

    Integer countMonthReceive(Long memId, String giftCode);

    Integer countYearReceive(Long memId, String giftCode);

    Integer countBirthdayReceive(Long memId, String giftCode);


}
