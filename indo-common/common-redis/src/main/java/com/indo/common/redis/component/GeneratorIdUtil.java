package com.indo.common.redis.component;

import com.indo.common.constant.RedisConstants;
import com.indo.common.enums.BusinessTypeEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Slf4j
public class GeneratorIdUtil {

    /**
     * 获取有过期时间的自增长ID
     *
     * @param key
     * @return
     */
    public static long generate(String key) {
        String redisKey =  RedisConstants.BUSINESS_NO_PREFIX + key;
        String value = StringUtils.isBlank(RedisUtils.get(redisKey)) ? "0" : RedisUtils.get(redisKey).toString();
        long valueNum = Long.parseLong(value);
        if("0".equals(value)) {
            valueNum = 1;
            RedisUtils.set(redisKey, valueNum);
            RedisUtils.expire(redisKey, 2);//2秒过期
        } else {
            RedisUtils.incr(redisKey, 1l);
            valueNum = Long.parseLong(RedisUtils.get(redisKey));
        }
        return valueNum;
    }

    public static String generateId() {
        // 生成id为当前日期（yyMMddHHmmss）+6位（从000000开始不足位数补0）
        LocalDateTime now = LocalDateTime.now();
        String idPrefix = getIdPrefix(now);// 生成yyyyMMddHHmmss
        String id = idPrefix.substring(0, 4) + String.format("%1$05d", generate(idPrefix)) + idPrefix.substring(4);
        return id;
    }

    public static String getIdPrefix(LocalDateTime now) {
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }



}
