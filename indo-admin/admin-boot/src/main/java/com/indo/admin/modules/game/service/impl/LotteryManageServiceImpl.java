package com.indo.admin.modules.game.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.game.mapper.GameLotteryWinningNumberMapper;
import com.indo.admin.modules.game.service.ILotteryManageService;
import com.indo.admin.pojo.criteria.GameDownloadQueryCriteria;
import com.indo.admin.pojo.dto.game.manage.GameLotteryWinningNumberQueryDto;
import com.indo.admin.pojo.dto.game.manage.GameLotteryWinningNumberReqDto;
import com.indo.common.constant.RedisConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.QueryHelpPlus;
import com.indo.core.pojo.entity.GameLotteryWinningNumber;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class LotteryManageServiceImpl extends ServiceImpl<GameLotteryWinningNumberMapper, GameLotteryWinningNumber> implements ILotteryManageService {

    public List<GameLotteryWinningNumber> rgwn(){
        //查询数据库最大记录日期，没有数据时新增当天288期开奖号码，有数据时增加一天288期开奖号码
        String maxDateStr = "";
        String dateStr = DateUtils.format(new Date(), DateUtils.shortFormat);
        if(null!=maxDateStr&&!"".equals(maxDateStr)){//增加1天开奖号码
            String maxDate = maxDateStr.substring(0,8);//截取日期
            try {
                dateStr = DateUtils.getTomorrowDateString(maxDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<GameLotteryWinningNumber> list = new ArrayList<>();
        for(int i=1;i<=288;i++){//24小时X60分钟/5分钟=288次
            GameLotteryWinningNumber gameLotteryWinningNumber = new GameLotteryWinningNumber();
            //开奖期号
            String lotteryDate = dateStr+String.format("%03d",i);
            gameLotteryWinningNumber.setLotteryDate(lotteryDate);
            //生成3位随机开奖号码
            int one = this.getRandom();
            gameLotteryWinningNumber.setOneWinNum(one);
            int two = this.getRandom();
            gameLotteryWinningNumber.setTwoWinNum(two);
            int three = this.getRandom();
            gameLotteryWinningNumber.setThreeWinNum(three);
            gameLotteryWinningNumber.setCreateTime(DateUtils.format(new Date(), DateUtils.longFormat));
            this.addGameLotteryWinningNumber(gameLotteryWinningNumber);
            list.add(gameLotteryWinningNumber);
        }
        return list;
    }

    public List<GameLotteryWinningNumber> queryAllGameLotteryWinningNumber(GameLotteryWinningNumberQueryDto queryDto) {
        Map<Object, Object> map = new HashMap<>();
        List<GameLotteryWinningNumber> list = new ArrayList<>();
        if(null==queryDto){
            map = RedisUtils.hmget(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY);
            if(ObjectUtil.isEmpty(map)){
                GameDownloadQueryCriteria criteria = new GameDownloadQueryCriteria();
                list = baseMapper.selectList(QueryHelpPlus.getPredicate(GameLotteryWinningNumber.class, criteria));
            }else {
                list = new ArrayList(map.values());
            }
        }else if(null!=queryDto.getLotteryDate()&&!"".equals(queryDto.getLotteryDate())){
            list = new ArrayList<>();
            GameLotteryWinningNumber gameLotteryWinningNumber;
            gameLotteryWinningNumber = (GameLotteryWinningNumber)RedisUtils.hget(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY,queryDto.getLotteryDate());
            if(null==gameLotteryWinningNumber){
                LambdaQueryWrapper<GameLotteryWinningNumber> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(GameLotteryWinningNumber::getLotteryDate, queryDto.getLotteryDate());
                gameLotteryWinningNumber = baseMapper.selectOne(wrapper);
            }
            list.add(gameLotteryWinningNumber);
        }else if(null!=queryDto.getWinningDate()&&!"".equals(queryDto.getWinningDate())){
            list = new ArrayList<>();
            for(int i=1;i<=288;i++){//24小时X60分钟/5分钟=288次
                String lotteryDate = queryDto.getWinningDate()+String.format("%03d",i);//日期+数字不足3位补0
                GameLotteryWinningNumber gameLotteryWinningNumber;
                gameLotteryWinningNumber = (GameLotteryWinningNumber) RedisUtils.hget(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY, lotteryDate);
                if (null == gameLotteryWinningNumber) {
                    LambdaQueryWrapper<GameLotteryWinningNumber> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(GameLotteryWinningNumber::getLotteryDate, lotteryDate);
                    gameLotteryWinningNumber = baseMapper.selectOne(wrapper);
                }
                list.add(gameLotteryWinningNumber);
            }
        }


        list.sort(Comparator.comparing(GameLotteryWinningNumber::getLotteryDate).reversed());//倒序排序
        return list;
    }

    public boolean deleteBatchGameLotteryWinningNumber(List<String> list) {
        if (this.baseMapper.deleteBatchIds(list) > 0) {
            list.forEach(id -> {
                AdminBusinessRedisUtils.hdel(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY, id + "");
            });
            return true;
        }
        return false;
    }
    public boolean addGameLotteryWinningNumber(GameLotteryWinningNumber gameLotteryWinningNumber) {
        if (this.baseMapper.insert(gameLotteryWinningNumber) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY, gameLotteryWinningNumber.getLotteryDate() + "", gameLotteryWinningNumber);
            return true;
        }
        return false;
    }

    public  boolean modifyRgwn(GameLotteryWinningNumberReqDto req){
        GameLotteryWinningNumber gameLotteryWinningNumber = new GameLotteryWinningNumber();
        BeanUtils.copyProperties(req, gameLotteryWinningNumber);
        return this.modifyGameLotteryWinningNumber(gameLotteryWinningNumber);
    }

    public boolean modifyGameLotteryWinningNumber(GameLotteryWinningNumber gameLotteryWinningNumber) {
        if (this.baseMapper.updateById(gameLotteryWinningNumber) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.GAME_LOTTERY_WIN_NUM_KEY, gameLotteryWinningNumber.getLotteryDate() + "", gameLotteryWinningNumber);
            return true;
        }
        return false;
    }

    //生成随机数
    public int getRandom(){
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    public static void main(String s[]){

        System.out.println();
        System.out.println(String.format("%05d",11111));
    }
}
