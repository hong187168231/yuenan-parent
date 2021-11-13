package com.indo.game.task;

import com.indo.game.game.RedisBaseUtil;
import com.indo.common.utils.DateUtils;
import com.indo.game.services.ky.KYService;
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
public class KYScheduledTasks {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private KYService kyService;
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
        if (taskRun) {
            String key = "pullKYBetOrder" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                // 写锁（等待时间100s，超时时间10S[自动解锁]，单位：秒）【设定超时时间，超时后自动释放锁，防止死锁】
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        log.info("=============== pullKYBetOrder order pull is begin " + dateFormat.format(new Date())
                                + " ===============");
                        kyService.pullKYBetOrder();
                        log.info("=============== pullKYBetOrder order pull is end " + dateFormat.format(new Date())
                                + " =============");
                    }
                }
            } catch (Exception e) {
                log.error("KYScheduledTasks pullKYBetOrder is error {} ", e);
            }
        }
    }

    /**
     * 补偿机制， 每10分钟检查一次，最近一个小时没有拉取的数据
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void reportOneHour() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "kyTaskOneHour" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(20, 10, TimeUnit.SECONDS);
                if (bool) {
                    log.info("=============== reportOneHour order pull is begin {}", dateFormat.format(new Date()));
                    kyService.pullKYBetOrderOfMissing();
                    log.info("=============== reportOneHour order pull is end {}", dateFormat.format(new Date()));
                }
            } catch (Exception e) {
                log.error("KYScheduledTasks pullKYBetOrder is error {} ", e);
            }
        }
    }

    /**
     * @Override public void run() { RedissonClient redissonClient =
     *           (RedissonClient)
     *           SpringContextUtil.getBeanByClass(RedissonClient.class);
     *           RReadWriteLock lock = redissonClient.getReadWriteLock("kyTask");
     *           try { boolean bool = lock.writeLock().tryLock(35, 35,
     *           TimeUnit.SECONDS); if (bool) { log.error("===============
     *           pullKYBetOrder order pull is begin "+dateFormat.format(new
     *           Date())+" ==============="); KYService kyService = (KYService)
     *           SpringContextUtil.getBeanByClass(KYService.class);
     *           kyService.pullKYBetOrder(); log.error("===============
     *           pullKYBetOrder order pull is end "+dateFormat.format(new Date())+"
     *           ============="); } } catch (Exception e) {
     *           log.error("KYScheduledTasks pullKYBetOrder is error {} ", e); }
     *           finally { // 不主动释放锁，保证只有一台服务器运行task // lock.writeLock().unlock(); }
     *           }
     **/
}
