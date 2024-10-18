package com.kge.energy.crm.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockUtils {

    private final StringRedisTemplate redisTemplate;

    /**
     * @param lockKey    锁
     * @param value      身份标识（保证锁不会被其他人释放）
     * @param expireTime 锁的过期时间（单位：秒）
     * @return 成功返回true, 失败返回false
     */
    public boolean lock(String lockKey, String value, long expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, TimeUnit.SECONDS);
        if (result != null) {
            log.info("==> redis加锁[{}]:[{}]", lockKey, result);
            return result;
        } else {
            log.error("==> redis加锁失败[{}]", lockKey);
            return false;
        }
    }

    /**
     * redisTemplate解锁
     *
     * @param key
     * @param value
     * @return 成功返回true, 失败返回false
     */
    public boolean unlock(String key, String value) {
        Object currentValue = redisTemplate.opsForValue().get(key);
        boolean result = false;
        if (currentValue != null && currentValue.equals(value)) {
            Boolean delete = redisTemplate.opsForValue().getOperations().delete(key);
            if (delete != null) {
                log.info("==> redis解锁[{}]:[{}]", key, delete);
                result = delete;
            }
        }
        return result;
    }



}