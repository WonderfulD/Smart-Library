package com.ruoyi;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

import static com.ruoyi.RedisConstants.CACHE_BOOKRATING_KEY;

@SpringBootTest
public class TestLogicalExpiration {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("测试图书评分逻辑过期前置插入")
    public void insertData() {
        RedisObject redisObject = new RedisObject();
        redisObject.setData(String.valueOf(100.0));
        redisObject.setLocalDateTime(LocalDateTime.now().plusSeconds(10));
        stringRedisTemplate.opsForValue().set(CACHE_BOOKRATING_KEY + 10, JSONUtil.toJsonStr(redisObject));
    }

}
