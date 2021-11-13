package com.indo.game.task;

import com.indo.game.game.RedisBaseUtil;
import com.indo.common.utils.DateUtils;
import com.indo.game.services.es.ESGameService;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.management.MalformedObjectNameException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//@EnableScheduling
@Component
public class ESScheduledTasks {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private ESGameService esGameService;
    @Autowired
    private RedissonClient redissonClient;
    @Value("${task.scheduler.run}")
    private boolean taskRun;

    /**
     * 35秒拉取一次
     *
     * @throws MalformedObjectNameException TODO 暂使用属性 taskRun 控制业务代码执行
     * @Scheduled(cron = "0 0/1 * * * ?")
     */
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void reportCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {

            String key = "esTask" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        log.info("=============== esganme pullOrder pull is begin {} ===============", dateFormat.format(new Date()));
                        esGameService.pullOrder();
                        log.info("=============== esganme pullOrder pull is end {} =============", dateFormat.format(new Date()));
                    }
                }
            } catch (Exception e) {
                log.error("ESScheduledTasks pullOrder is error {} ", e);
            }
        }
    }
}
