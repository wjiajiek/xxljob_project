package com.xxl.job.executor.aspect;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.executor.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class DistributedLockAspect {

    @Autowired
    private RedisTemplate redisTemplate;
//    distributedLock1
    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock lock) throws Throwable {
        // 获取锁名
        String lockKey = lock.value();
        if (StringUtils.isBlank(lockKey)) {
            lockKey = joinPoint.getSignature().toLongString();
        }
        // 获取锁的过期时间
        long expire = lock.expire();
        // 尝试获取锁
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, expire, TimeUnit.SECONDS);
        if (acquired != null && acquired) {
            try {
                XxlJobHelper.log("【{}】获取分布式锁成功",lockKey);
                log.info("【{}】获取分布式锁成功",lockKey);
                // 执行目标方法
                return joinPoint.proceed();
            } finally {
               // 释放锁
                redisTemplate.delete(lockKey);
            }
        } else {
            XxlJobHelper.log("【{}】获取分布式锁失败",lockKey);
            log.info("【{}】获取分布式锁失败",lockKey);
            XxlJobHelper.handleResult(500, "获取分布式锁失败");
            return null;
        }

    }

}

