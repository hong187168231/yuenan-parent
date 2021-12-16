package com.indo.admin.task;

import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.modules.stat.entity.StatUserRetention;
import com.indo.admin.modules.stat.service.IUserRetentionService;
import com.indo.common.constant.AppConstants;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.DateUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
public class RetentionJob {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private IMemBaseinfoService iMemBaseinfoService;
    @Resource
    private IUserRetentionService userRetentionService;

    //3.添加定时任务
    @Scheduled(cron = "0 15 21 ? * *")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        Date yesterday = DateUtils.addDay(new Date(), -1);

        DateUtils.format(yesterday, DateUtils.shortFormat);
        Set<Object> idSet = redisUtils.sGet(AppConstants.USER_DAILY_VISIT_LOG + yesterday);
        if (idSet == null || idSet.size() == 0) {
            System.err.println("用户留存从redis未查到数据");
            return;
        }
        List<Long> memIds = new ArrayList<>();
        for (Object memId : idSet) {
            memIds.add((Long) memId);
        }
        StatUserRetention statUserRetention = new StatUserRetention();
        List<Long> idListBeforeYesterday0Day = iMemBaseinfoService.findIdListByCreateTime(DateUtils.addDay(yesterday, 0));
        statUserRetention.setNewUsers(idListBeforeYesterday0Day.size());
        List<Long> idListBeforeYesterday1Day = iMemBaseinfoService.findIdListByCreateTime(DateUtils.addDay(yesterday, -1));
        statUserRetention.setNextDay(Double.valueOf(receiveCollectionList(memIds, idListBeforeYesterday1Day).size()) / Double.valueOf(memIds.size()));
        List<Long> idListBeforeYesterday3Day = iMemBaseinfoService.findIdListByCreateTime(DateUtils.addDay(yesterday, -3));
        statUserRetention.setThreeDay(Double.valueOf(receiveCollectionList(memIds, idListBeforeYesterday3Day).size()) / Double.valueOf(memIds.size()));
        List<Long> idListBeforeYesterday7Day = iMemBaseinfoService.findIdListByCreateTime(DateUtils.addDay(yesterday, -7));
        statUserRetention.setSevevDay(Double.valueOf(receiveCollectionList(memIds, idListBeforeYesterday7Day).size()) / Double.valueOf(memIds.size()));
        List<Long> idListBeforeYesterday30Day = iMemBaseinfoService.findIdListByCreateTime(DateUtils.addDay(yesterday, -30));
        statUserRetention.setThirtyDay(Double.valueOf(receiveCollectionList(memIds, idListBeforeYesterday30Day).size()) / Double.valueOf(memIds.size()));

        userRetentionService.save(statUserRetention);
        System.err.println("留存任务结束");
    }

    /**
     * @param firstArrayList  第一个ArrayList
     * @param secondArrayList 第二个ArrayList
     * @return resultList 交集ArrayList
     * @方法描述：获取两个ArrayList的交集
     */
    public static List<Long> receiveCollectionList(List<Long> firstArrayList, List<Long> secondArrayList) {
        List<Long> resultList = new ArrayList();
        LinkedList<Long> result = new LinkedList(firstArrayList);// 大集合用linkedlist
        HashSet<Long> othHash = new HashSet(secondArrayList);// 小集合用hashset
        Iterator<Long> iter = result.iterator();// 采用Iterator迭代器进行数据的操作
        while (iter.hasNext()) {
            if (!othHash.contains(iter.next())) {
                iter.remove();
            }
        }
        resultList = new ArrayList(result);
        return resultList;
    }

}
