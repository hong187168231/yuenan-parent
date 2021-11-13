package com.indo.game.task;

import com.indo.game.game.RedisBaseUtil;
import com.indo.common.utils.DateUtils;
import com.indo.game.services.db.DbService;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.management.MalformedObjectNameException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class DbScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private DbService dbService;
    @Autowired
    private RedissonClient redissonClient;
    @Value("${task.scheduler.run}")
    private boolean taskRun;

    /**
     * 60秒拉取一次
     *
     * @throws MalformedObjectNameException TODO 暂使用属性 taskRun 控制业务代码执行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void reportCurrentTime() throws MalformedObjectNameException {
        /*if (taskRun) {*/
            String key = "pullDbBetOrder" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                // 写锁（等待时间100s，超时时间10S[自动解锁]，单位：秒）【设定超时时间，超时后自动释放锁，防止死锁】
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullDbBetOrder order pull is begin " + dateFormat.format(new Date()) + " ===============");
                        dbService.pullDbBetOrder();
                        logger.info("=============== pullDbBetOrder order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error(" pullDbBetOrder is error {} ", e);
            }
        }
   /* }*/
}
