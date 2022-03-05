package com.indo.game.common.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.util.UUID;

/**
 * 用于页面展示相关的工具类
 */
public class SnowflakeId {
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeId.class);
    private final long twepoch = 1489111610226L;
    private final long workerIdBits = 5L;
    private final long dataCenterIdBits = 5L;
    private final long maxWorkerId = 31L;
    private final long maxDataCenterId = 31L;
    private final long sequenceBits = 6L;
    private final long workerIdShift = 6L;
    private final long dataCenterIdShift = 11L;
    private final long timestampLeftShift = 16L;
    private final long sequenceMask = 63L;
    private Long workerId;
    private Long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private static volatile SnowflakeId idWorker;

    public static void init() {
        if (isInValidIdWorker()) {
            Class var0 = SnowflakeId.class;
            synchronized(SnowflakeId.class) {
                if (isInValidIdWorker()) {
                    idWorker = new SnowflakeId(getWorkId(), getDataCenterId());
                    logger.info("init SnowflakeIdWorker. workerId:{}; dataCenterId:{}.", idWorker.workerId, idWorker.dataCenterId);
                }
            }
        }

    }

    private static boolean isInValidIdWorker() {
        return null == idWorker || null == idWorker.workerId || idWorker.workerId < 0L || null == idWorker.dataCenterId || idWorker.dataCenterId < 0L;
    }

    public SnowflakeId(long workerId, long dataCenterId) {
        if (workerId <= 31L && workerId >= 0L) {
            if (dataCenterId <= 31L && dataCenterId >= 0L) {
                this.workerId = workerId;
                this.dataCenterId = dataCenterId;
            } else {
                throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", 31L));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 63L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            return timestamp - 1489111610226L << 16 | this.dataCenterId << 11 | this.workerId << 6 | this.sequence;
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            logger.info("getWorkId hostAddress:{}.", hostAddress);
            if (StringUtils.isEmpty(hostAddress)) {
                logger.error("getWorkId->getHostAddress is empty.");
                hostAddress = UUID.randomUUID().toString();
            }

            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            int[] var3 = ints;
            int var4 = ints.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                int b = var3[var5];
                sums += b;
            }

            return (long)(sums % 32);
        } catch (Exception var7) {
            Long num = RandomUtils.nextLong(0L, 31L);
            logger.error("getWorkId occur error.to random long number:{}.", num, var7);
            return num;
        }
    }

    private static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName() + UUID.randomUUID().toString();
            logger.info("getDataCenterId hostName:{}.", hostName);
            if (StringUtils.isEmpty(hostName)) {
                hostName = Inet4Address.getLocalHost().getHostName();
                logger.info("Inet4Address hostName:{}.", hostName);
            }

            if (StringUtils.isEmpty(hostName)) {
                logger.error("getWorkId->getDataCenterId is empty.");
                return RandomUtils.nextLong(0L, 31L);
            } else {
                int[] ints = StringUtils.toCodePoints(hostName);
                int sums = 0;
                int[] var3 = ints;
                int var4 = ints.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    int i = var3[var5];
                    sums += i;
                }

                return (long)(sums % 32);
            }
        } catch (Exception var7) {
            Long num = RandomUtils.nextLong(0L, 31L);
            logger.error("getDataCenterId occur error.to random long number:{}.", num, var7);
            return num;
        }
    }

    public static Long generateId() {
        init();
        return idWorker.nextId();
    }
}
