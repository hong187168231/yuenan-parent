package com.indo.admin;

import com.indo.admin.modules.mem.mapper.MemSubordinateMapper;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.user.pojo.entity.MemSubordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: Mr.liu
 * @Date: 2021/9/1 17:26
 * @Version: 1.0.0
 * @Desc:
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemSubordinateMapper memSubordinateMapper;
    @Test
    public void test() {
        MemSubordinate subordinate = new MemSubordinate();
        subordinate.setId(14L);
        subordinate.setIsDel(false);
        memSubordinateMapper.updateById(subordinate);

    }

}
