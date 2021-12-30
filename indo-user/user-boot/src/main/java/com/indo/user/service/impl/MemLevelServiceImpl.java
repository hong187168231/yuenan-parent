package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GiftEnum;
import com.indo.common.enums.GiftTypeEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.web.exception.BizException;
import com.indo.user.common.kaptcha.LoginProperties;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.mapper.MemGiftReceiveMapper;
import com.indo.user.mapper.MemLevelMapper;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.level.Gift;
import com.indo.user.pojo.vo.level.LevelInfo;
import com.indo.user.pojo.vo.level.MemLevelVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
import com.indo.user.service.IMemGiftReceiveService;
import com.indo.user.service.IMemLevelService;
import com.indo.user.service.MemBaseInfoService;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
public class MemLevelServiceImpl extends ServiceImpl<MemLevelMapper, MemLevel> implements IMemLevelService {

    @Autowired
    private MemBaseInfoService memBaseInfoService;

    @Autowired
    private IMemGiftReceiveService iMemGiftReceiveService;

    @Override
    public MemLevelVo findInfo(LoginInfo loginInfo) {
        Long memId = loginInfo.getId();

        MemLevelVo memLevelVo = new MemLevelVo();
        MemTradingVo memTradingVo = memBaseInfoService.tradingInfo(loginInfo.getId());
        memLevelVo.setTradingVo(memTradingVo);

        List<LevelInfo> levelInfoList = new ArrayList<>();
        List<MemLevel> list = getLevelList();

        for (MemLevel memLevel : list) {
            List<Gift> giftList = new ArrayList<>();
            LevelInfo levelInfo = new LevelInfo();

            levelInfo.setLevel(memLevel.getLevel());
            levelInfo.setPromotionGift(memLevel.getReward().intValue());
            levelInfo.setNeedDeposit(memLevel.getNeedDeposit().intValue());
            levelInfo.setNeedBet(memLevel.getNeedBet().intValue());

            if (memLevel.getReward() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.reward);
                gift.setGiftName(GiftEnum.reward.getName());
                gift.setAmount(memLevel.getReward().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countRewardReceive(memId, GiftEnum.reward.name(), memLevel.getLevel());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            if (memLevel.getEverydayGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.everyday);
                gift.setGiftName(GiftEnum.everyday.getName());
                gift.setAmount(memLevel.getEverydayGift().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countTodayReceive(memId, GiftEnum.everyday.name());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            if (memLevel.getWeekGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.week);
                gift.setGiftName(GiftEnum.week.getName());
                gift.setAmount(memLevel.getWeekGift().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countWeekReceive(memId, GiftEnum.week.name());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            if (memLevel.getMonthGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.month);
                gift.setGiftName(GiftEnum.month.getName());
                gift.setAmount(memLevel.getMonthGift().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countMonthReceive(memId, GiftEnum.month.name());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            if (memLevel.getYearGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.year);
                gift.setGiftName(GiftEnum.year.getName());
                gift.setAmount(memLevel.getYearGift().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countYearReceive(memId, GiftEnum.year.name());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            if (memLevel.getBirthdayGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.birthday);
                gift.setGiftName(GiftEnum.birthday.getName());
                gift.setAmount(memLevel.getBirthdayGift().intValue());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    int count = iMemGiftReceiveService.countBirthdayReceive(memId, GiftEnum.birthday.name());
                    gift.setReceiveStatus(count == 0 ? 1 : 2);
                }
                giftList.add(gift);
            }
            levelInfo.setGiftList(giftList);
            levelInfoList.add(levelInfo);
        }
        memLevelVo.setLevelList(levelInfoList);
        return memLevelVo;
    }


    private List<MemLevel> getLevelList() {
        List<MemLevel> cacheList = UserBusinessRedisUtils.get(RedisKeys.SYS_LEVEL_KEY);
        if (CollectionUtil.isNotEmpty(cacheList)) {
            return cacheList;
        }
        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper<>();
        List<MemLevel> list = this.baseMapper.selectList(wrapper);
        return list;
    }
}
