package com.indo.game.task;

import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.DateUtils;
import com.indo.game.service.ug.UgService;
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
public class UgScheduledTasks {

    @Autowired
    private RedissonClient redissonClient;
//    @Value("${task.scheduler.run}")
    private boolean taskRun;
    @Autowired
    private UgService ugService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.TIME_PATTERN);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0/1 * * * ?")
    public void reportCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "ugTask" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisUtils.get(key) == null) {
                        RedisUtils.set(key, "1", 60L);
                        logger.info("=============== ugPullOrder pullOrder pull is begin {} ===============", dateFormat.format(new Date()));
                        ugService.ugPullOrder();
                        logger.info("=============== ugPullOrder pullOrder pull is end {} =============", dateFormat.format(new Date()));
                    }
                }
            } catch (Exception e) {
                logger.error("AEScheduledTasks pullOrder is error {} ", e);
            }
        }
    }
}
