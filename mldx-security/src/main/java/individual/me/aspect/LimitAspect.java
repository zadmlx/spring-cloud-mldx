package individual.me.aspect;

import individual.me.annotation.Limit;
import individual.me.exception.BadRequestException;
import individual.me.util.NetUtil;
import individual.me.util.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Order(2)
@Component
@Aspect
public class LimitAspect implements InitializingBean {


    private DefaultRedisScript<Long> redisScript;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Pointcut("@annotation(individual.me.annotation.Limit)")
    public void pointcut(){}


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("限制开始");
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        Limit limit = signature.getMethod().getAnnotation(Limit.class);
        String ip = NetUtil.getIp(RequestHolder.currentRequest());
        List<String> key = Collections.singletonList(ip);
        Long count = redisTemplate.execute(redisScript, key, limit.count(), limit.ttl());
        if (count != null && count.intValue() < limit.count()){
            log.info("这是第{}次访问",count);
            return joinPoint.proceed();
        }

        throw new BadRequestException("请求次数超过限制，请稍后重试");


    }

    @Override
    public void afterPropertiesSet() {
        ClassPathResource resource = new ClassPathResource("limit.lua");
        redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(resource);
    }

    /*
     * 思路
     * 1，获取注解的参数：时间和次数，然后获取请求的ip地址，
     * 2. lua脚本逻辑：首先根据key：ip，获取请求次数，如果是第一次，添加访问次数设置为1，过期时间60s
     *    如果是后续继续访问，获取key，在获取请求次数，判断次数，小于5就设置访问次数加一，
     *    大于5，就返回一个-1，然后后续根据返回次数进行判断
     */

}
