package cn.cutepikachu.datawisemaster.manager;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Component
public class RateLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 1, 10, RateIntervalUnit.SECONDS);
        boolean canOperate = rateLimiter.tryAcquire(1);
        if (!canOperate) {
            throw new BusinessException(ResponseCode.REQUEST_TOO_FREQUENT);
        }
    }
}
