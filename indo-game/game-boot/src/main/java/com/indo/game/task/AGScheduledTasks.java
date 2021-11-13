package com.indo.game.task;

import com.indo.game.game.RedisBaseUtil;
import com.indo.common.utils.DateUtils;
import com.indo.game.services.ag.AgService;
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
public class AGScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private AgService agService;
    @Autowired
    private RedissonClient redissonClient;
    @Value("${task.scheduler.run}")
    private boolean taskRun;

    /**
     * 60秒读取一次
     *
     * @throws MalformedObjectNameException TODO 暂使用属性 taskRun 控制业务代码执行
     */

    @Scheduled(cron = "0 0/1 * * * ?")
    public void reportCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullAGFTPXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullAGFTPXml order pull is begin " + dateFormat.format(new Date())
                                + " ===============");
                        agService.pullAGFTPXml();
                        logger.info("=============== pullAGFTPXml order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullAGFTPXml is error {} ", e);
            }
        }
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void agNextAginCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullNextAGFTAGINXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullAGAginXml order pull is begin " + dateFormat.format(new Date())
                                + " ===============");
                        agService.pullAGAginXml();
                        logger.info("=============== pullAGAginXml order pull is end " + dateFormat.format(new Date())
                                + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullAGFTPYOPLAYXml is error {} ", e);
            }
        }
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void agCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullAGFTPYOPLAYXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullAGYOPLAYXml order pull is begin " + dateFormat.format(new Date()) + " ===============");
                        agService.pullAGYOPLAYXml();
                        logger.info("=============== pullAGYOPLAYXml order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullAGFTPYOPLAYXml is error {} ", e);
            }
        }
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void agNextCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullNextFTPYOPLAYXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullNextFTPYOPLAYXml order pull is begin " + dateFormat.format(new Date()) + " ===============");
                        agService.pullNextAGYOPLAYXml();
                        logger.info("=============== pullNextFTPYOPLAYXml order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullNextFTPYOPLAYXml is error {} ", e);
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void agHunterCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullAGHunterXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullAGHunterXml order pull is begin " + dateFormat.format(new Date()) + " ===============");
                        agService.pullAGHunterXml();
                        logger.info("=============== pullAGFHunterXml order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullAGFTPYOPLAYXml is error {} ", e);
            }
        }
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void agNextHunterCurrentTime() throws MalformedObjectNameException {
        if (taskRun) {
            String key = "pullNextAGHunterAGINXml" + DateUtils.format(new Date(), DateUtils.longFormat);
            RReadWriteLock lock = redissonClient.getReadWriteLock(key + "lock");
            try {
                boolean bool = lock.writeLock().tryLock(50, 10, TimeUnit.SECONDS);
                if (bool) {
                    if (RedisBaseUtil.get(key) == null) {
                        RedisBaseUtil.set(key, "1", 60L);
                        logger.info("=============== pullNextAGHunterAGINXml order pull is begin " + dateFormat.format(new Date()) + " ===============");
                        agService.pullAGNextHunterXml();
                        logger.info("=============== pullNextAGHunterAGINXml order pull is end " + dateFormat.format(new Date()) + " =============");
                    }
                }
            } catch (Exception e) {
                logger.error("AGScheduledTasks pullNextAGHunterAGINXml is error {} ", e);
            }
        }
    }


}
