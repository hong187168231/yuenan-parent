package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.enums.GiftEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.exception.BizException;
import com.indo.user.common.kaptcha.LoginProperties;
import com.indo.user.mapper.MemLevelMapper;
import com.indo.user.pojo.entity.MemLevel;
import com.indo.user.pojo.vo.level.Gift;
import com.indo.user.pojo.vo.level.LevelInfo;
import com.indo.user.pojo.vo.level.MemLevelVo;
import com.indo.user.pojo.vo.mem.MemTradingVo;
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

    @Override
    public MemLevelVo findInfo(LoginInfo loginInfo) {

        MemLevelVo memLevelVo = new MemLevelVo();
        MemTradingVo memTradingVo = memBaseInfoService.tradingInfo(loginInfo.getId());
        memLevelVo.setTradingVo(memTradingVo);

        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper<>();
        List<MemLevel> list = this.baseMapper.selectList(wrapper);
        List<LevelInfo> levelInfoList = new ArrayList<>();
        List<Gift> giftList = new ArrayList<>();
        for (MemLevel memLevel : list) {
            LevelInfo levelInfo = new LevelInfo();
            levelInfo.setLevel(memLevel.getLevel());
            levelInfo.setNeedDeposit(memLevel.getNeedDeposit());
            levelInfo.setNeedBet(memLevel.getNeedBet());

            if (memLevel.getReward() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.reward);
                gift.setAmount(memLevel.getReward());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            if (memLevel.getEverydayGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.everyday);
                gift.setAmount(memLevel.getEverydayGift());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            if (memLevel.getWeekGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.week);
                gift.setAmount(memLevel.getWeekGift());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            if (memLevel.getMonthGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.month);
                gift.setAmount(memLevel.getMonthGift());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            if (memLevel.getYearGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.year);
                gift.setAmount(memLevel.getYearGift());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            if (memLevel.getBirthdayGift() != null) {
                Gift gift = new Gift();
                gift.setGiftEnum(GiftEnum.birthday);
                gift.setAmount(memLevel.getBirthdayGift());
                if (memTradingVo.getMemLevel() >= memLevel.getLevel()) {
                    gift.setReceiveStatus(1);
                }
                giftList.add(gift);
            }
            levelInfo.setGiftList(giftList);

        }
        memLevelVo.setLevelList(levelInfoList);
        return memLevelVo;
    }
}
