package com.indo.common.redis.utils;

import com.indo.common.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
public class GeneratorIdUtil {

    /**
     * 获取有过期时间的自增长ID
     *
     * @param key
     * @return
     */
    public static long generate(String key) {
        String redisKey = RedisConstants.BUSINESS_NO_PREFIX + key;
        Object value = RedisUtils.get(redisKey);
        long valueNum = 1L;
        if (ObjectUtils.isEmpty(value)) {
            RedisUtils.set(redisKey, valueNum);
            RedisUtils.expire(redisKey, 2);//2秒过期
        } else {
            RedisUtils.incr(redisKey, 1l);
            valueNum = Long.parseLong(RedisUtils.get(redisKey).toString());
        }
        return valueNum;
    }

    public static String generateId() {
        // 生成id为当前日期（yyMMddHHmmss）+5位（从00000开始不足位数补0）
        LocalDateTime now = LocalDateTime.now();
        String idPrefix = getIdPrefix(now);// 生成yyyyMMddHHmmss
        String id = idPrefix.substring(0, 4) + String.format("%05d", generate(idPrefix)) + idPrefix.substring(4);
        return id;
    }

    public static String getIdPrefix(LocalDateTime now) {
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }


}
