package com.indo.game.game;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisBaseUtil
 * Description: 描述
 *
 * @author hai
 * @since JDK 1.8
 * date: 2020/8/24 23:21
 */
public class RedisBaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisBaseUtil.class);
    public volatile static RedisTemplate redisTemplate;

    public static void init(RedisTemplate redisTemplate) {
        if (null == RedisBaseUtil.redisTemplate && null != redisTemplate) {
            RedisBaseUtil.redisTemplate = redisTemplate;
            logger.info("RedisBaseUtil init:{}", redisTemplate);
        }
    }

    /////////////////////////////////////////通用基础方法/////////////////////////////////////////

    public static <T> T get(Object key) {
        if (null == key || StringUtils.isEmpty(key.toString())) {
            return null;
        }
        try {
            Object result = redisTemplate.opsForValue().get(key);
            return null == result ? null : (T) result;
        } catch (Exception e) {
            logger.error("redis get occur error. key:{}", key, e);
            if (e instanceof ClassCastException) {
                //数据转换异常时删除旧缓存
                delete(key.toString());
                logger.error("deleted redis ClassCastException cache by key:{}", key);
            }
            return null;
        }
    }

    public static void set(Object key, Object value) {
        set(key, value, 0, null);
    }

    public static void set(Object key, Object value, long expireTime) {
        set(key, value, expireTime, TimeUnit.SECONDS);
    }

    public static void set(Object key, Object value, long expireTime, TimeUnit unit) {
        if (null == key || StringUtils.isEmpty(key.toString()) || null == value) {
            return;
        }
        try {
            if (expireTime > 0) {
                unit = null == unit ? TimeUnit.SECONDS : unit;
                redisTemplate.opsForValue().set(key, value, expireTime, unit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        } catch (Exception e) {
            logger.error("redis set occur error. key:{}, value:{}", key, value, e);
        }
    }

    public static boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public static void delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        List<String> keys = new ArrayList<>();
        keys.add(key);
        delete(keys);
    }

    public static void delete(String... keys) {
        if (null == keys || keys.length == 0) {
            return;
        }
        Set set = new HashSet();
        for (String k : keys) {
            if (null == k || "".equals(k.trim())) {
                continue;
            }
            set.add(k);
        }
        delete(set);
    }

    public static Long delete(Collection collection) {
        if (null != collection && collection.size() > 0) {
            try {
                Long effects = redisTemplate.delete(collection);
                logger.info("delete:{} success. effects:{}", JSONObject.toJSONString(collection), effects);
                return effects;
            } catch (Exception e) {
                logger.error("redis delete occur error.", e);
            }
        }
        return 0L;
    }

    /**
     * 删除指定key的缓存，多个key间以逗号分隔
     *
     * @param keys
     */
    public static void deleteByKeys(String keys) {
        if (StringUtils.isEmpty(keys)) {
            return;
        }
//        List<String> idList = StringUtils.splitStringList(keys);
        String[] arr = keys.split(",");
        List<String> idList = new ArrayList<>();
        for (String item : arr) {
            idList.add(item);
        }
        delete(idList);
    }

    /**
     * 模糊匹配清除缓存
     *
     * @param key
     */
    public static void deleteFuzzyMatchCache(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        try {
            delete(keys(key + "*"));
        } catch (Exception e) {
            logger.error("redis keys occur error. key:{}", key, e);
        }
    }

    public static Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            logger.error("redis keys occur error. pattern:{}", pattern, e);
            return new HashSet<>();
        }
    }

    public static Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("redis getExpire occur error. key:{}", key, e);
            return 0L;
        }
    }

    public static void setExpire(String key, long second) {
        setExpire(key, second, TimeUnit.SECONDS);
    }

    public static void setExpire(String key, long second, TimeUnit unit) {
        try {
            if (StringUtils.isEmpty(key) || second <= 0) {
                return;
            }
            unit = null == unit ? TimeUnit.SECONDS : unit;
            redisTemplate.expire(key, second, unit);
        } catch (Exception e) {
            logger.error("redis setExpireTime:{} second:{} occur error.", key, second, e);
        }
    }

    public static boolean hasKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("redis hasKey occur error. key:{}", key, e);
            return false;
        }
    }

    /////////////////////////////////////////redis bitmap 相关方法 start/////////////////////////////////////////

    /**
     * 将指定offset偏移量的值设置为0；
     *
     * @param key    bitmap结构的key
     * @param offset 指定的偏移量。
     * @return 若偏移位上的值为1，那么返回true。
     */
    public static Boolean getBit(String key, Long offset) {
        try {
            return redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            logger.error("redis getBit occur error. key:{}, offset:{}", key, offset, e);
            return null;
        }
    }

    /**
     * 将指定offset偏移量的值设置为1；
     *
     * @param key    bitmap结构的key
     * @param offset 指定的偏移量。
     * @param value  true：即该位设置为1，否则设置为0
     * @return 返回设置该value之前的值。
     */
    public static Boolean setBit(String key, Long offset, boolean value) {
        try {
            return redisTemplate.opsForValue().setBit(key, offset, value);
        } catch (Exception e) {
            logger.error("redis setBit occur error. key:{}", key, e);
            return null;
        }
    }

    /**
     * 统计字符串指定位上被设置为1的bit数
     *
     * @param key 键
     * @return long
     */
    public static Long bitCount(String key) {
        Long result = null;
        try {
            result = (Long) redisTemplate.execute(new RedisCallback<Long>() {
                @Nullable
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.bitCount(key.getBytes());
                }
            });
        } catch (Exception e) {
            logger.error("redis bitCount occur error. key:{}", key, e);
        }

        return Optional.ofNullable(result).orElse(0L);
    }

    /**
     * 统计字符串指定位上被设置为1的bit数
     *
     * @param key   键
     * @param start 开始位置  注意对应byte的位置,是bit位置*8
     * @param end   结束位置
     * @return long
     */
    public static Long bitCount(String key, long start, long end) {
        Long result = null;
        try {
            result = (Long) redisTemplate.execute(
                    new RedisCallback<Long>() {
                        @Nullable
                        @Override
                        public Long doInRedis(RedisConnection connection) throws DataAccessException {
                            return connection.bitCount(key.getBytes(), start, end);
                        }
                    }
            );
        } catch (Exception e) {
            logger.error("redis bitCount occur error. key:{}", key, e);
        }
        return Optional.ofNullable(result).orElse(0L);
    }

    /**
     * 对符合指定格式的key值进行未操作
     *
     * @param op      操作类型：与、或、异或、否
     * @param destKey 存放结果的键
     * @param pattern key格式
     * @return Long
     */
    public static Long bitOp(RedisStringCommands.BitOperation op, String destKey, String pattern) {
        return bitOp(op, destKey, keys(pattern));
    }

    /**
     * 对符合指定格式的key值进行未操作
     *
     * @param op      操作类型：与、或、异或、否
     * @param destKey 存放结果的键
     * @param keys    key 集合
     * @return Long
     */
    public static Long bitOp(RedisStringCommands.BitOperation op, String destKey, Collection<String> keys) {
        keys = null == keys ? Collections.EMPTY_LIST : keys;
        int size = keys.size();
        if (size == 0) {
            return 0L;
        }
        int index = 0;
        byte[][] bytes = new byte[size][];
        for (String key : keys) {
            bytes[index++] = key.getBytes();
        }
        try {
            return (Long) redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, destKey.getBytes(), bytes));
        } catch (Exception e) {
            logger.error("redis bitOp occur error.", e);
            return null;
        }
    }


    /////////////////////////////////////////redis bitmap 相关方法 end/////////////////////////////////////////

    /**
     * 递增 此时value值必须为int类型 否则报错
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static Long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            logger.error("redis increment occur error. key:{}", key, e);
            return 0L;
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static Long decrement(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().decrement(key, delta);
        } catch (Exception e) {
            logger.error("redis decrement occur error. key:{}", key, e);
            return 0L;
        }
    }

    /////////////////////////////////////////redis map 相关方法 start/////////////////////////////////////////

    /**
     * HashGet
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return 值
     */
    public static <V> V hGet(String key, Object hashKey) {
        try {
            return (V) redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            logger.error("redis hGet occur error. key:{}, item:{}", key, hashKey, e);
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static <K, V> Map<K, V> hEntries(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.error("redis hMGet occur error. key:{}", key, e);
            return new HashMap<>();
        }
    }

    public static Long hSize(String key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            logger.error("redis hSize occur error. key:{}", key, e);
            return 0L;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static <K, V> boolean hMSet(String key, Map<K, V> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("redis hSet for key:{},map:{} occur error:{}", key, JSONObject.toJSONString(map), e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static <K, V> boolean hMSet(String key, Map<K, V> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis hMSet for key:{},map:{},time:{} occur error:{}", key, JSONObject.toJSONString(map), time, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return true 成功 false失败
     */
    public static boolean hSet(String key, Object hashKey, Object value) {
        return hSet(key, hashKey, value, 0);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param time    时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hSet(String key, Object hashKey, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis hSet for key:{},item:{},value:{},time:{} occur error:{}", key, hashKey, value, time, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 可以使多个 不能为null
     */
    public static Long hDelete(String key, Object... hashKey) {
        try {
            return redisTemplate.opsForHash().delete(key, hashKey);
        } catch (Exception e) {
            logger.error("redis hDelete occur error. key:{}", key, e);
            return 0L;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            logger.error("redis hHasKey occur error. key:{}, item:{}", key, hashKey, e);
            return false;
        }
    }

    /////////////////////////////////////////redis map 相关方法 end/////////////////////////////////////////

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public static double hIncrement(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            logger.error("redis hIncrement occur error", e);
            return 0D;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public static double hDecrement(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            logger.error("redis hDecrement occur error", e);
            return 0D;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public static double hIncrement(String key, String item, long by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            logger.error("redis hIncrement occur error", e);
            return 0D;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public static double hDecrement(String key, String item, long by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            logger.error("redis hDecrement occur error", e);
            return 0D;
        }
    }

    /////////////////////////////////////////redis set 相关方法 start/////////////////////////////////////////

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("redis sGet for key:{} occur error:{}", key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("redis sHasKey for key:{},value:{} occur error:{}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("redis sSet for key:{},values:{} occur error:{}", key, JSONObject.toJSONString(values), e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            setExpire(key, time);
            return count;
        } catch (Exception e) {
            logger.error("redis sSetAndTime for key:{},time:{},values:{} occur error:{}", key, time, JSONObject.toJSONString(values), e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("redis sGetSetSize for key:{}, occur error:{}", key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            logger.error("redis setRemove for key:{},values:{} occur error:{}", key, JSONObject.toJSONString(values), e);
            return 0;
        }
    }


    /////////////////////////////////////////redis list 相关方法 start/////////////////////////////////////////

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public static <V> List<V> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("redis lRange for key:{},start:{},end:{} occur error:{}", key, start, end, e);
            return new ArrayList<>();
        }
    }

    public static <V> V lRightPop(String key) {
        try {
            return (V) redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            logger.error("redis lRightPop for key:{}", key, e);
            return null;
        }
    }

    public static <V> V lLeftPop(String key) {
        try {
            return (V) redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.error("redis lLeftPop for key:{}", key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("redis lGetListSize for key:{} occur error:{}", key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static <T> T lGetIndex(String key, long index) {
        try {
            Object result = redisTemplate.opsForList().index(key, index);
            return null == result ? null : (T) result;
        } catch (Exception e) {
            logger.error("redis lGetIndex for key:{},index:{} occur error:{}", key, index, e);
            return null;
        }
    }

    public static boolean lLeftPush(String key, Object value) {
        return lLeftPush(key, value, 0);
    }

    public static boolean lLeftPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis lLeftPush for key:{},value:{} occur error:{}", key, value, e);
            return false;
        }
    }

    public static boolean lLeftPushs(String key, Object pivot, Object value) {
        return lLeftPushs(key, pivot, value, 0);
    }

    public static boolean lLeftPushs(String key, Object pivot, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, pivot, value);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis lLeftPushs for key:{},value:{} occur error:{}", key, pivot, value, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @param time   时间(秒)
     * @return
     */
    public static <V> boolean lLeftPushAll(String key, Collection<V> values, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis lLeftPushAll for key:{},time:{},values:{} occur error:{}", key, time, JSONObject.toJSONString(values), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lRightPush(String key, Object value) {
        return lRightPush(key, value, 0);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lRightPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis lRightPush for key:{},time:{},value:{} occur error:{}", key, time, value, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public static <V> boolean lRightPushAll(String key, Collection<V> values) {
        return lRightPushAll(key, values, 0);
    }

    /**
     * 将list放入缓存
     *
     * @param key    键
     * @param values 值
     * @param time   时间(秒)
     * @return
     */
    public static <V> boolean lRightPushAll(String key, Collection<V> values, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            setExpire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("redis lRightPushAll for key:{},time:{},values:{} occur error:{}", key, time, JSONObject.toJSONString(values), e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("redis lUpdateIndex for key:{},index:{},value:{} occur error:{}", key, index, value, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            logger.error("redis lRemove for key:{},count:{},value:{} occur error:{}", key, count, value, e);
            return 0;
        }
    }

    /////////////////////////////////////////redis list 相关方法 end/////////////////////////////////////////

}
