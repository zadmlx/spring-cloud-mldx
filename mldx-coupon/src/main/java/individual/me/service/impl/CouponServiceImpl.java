package individual.me.service.impl;

import individual.me.config.SecurityContextHolder;
import individual.me.constants.SellerConstants;
import individual.me.domain.R;
import individual.me.exception.BadRequestException;
import individual.me.module.coupon.Coupon;
import individual.me.module.coupon.UserCoupon;
import individual.me.mq.CouponMQPublisher;
import individual.me.repository.CouponRepository;
import individual.me.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService, InitializingBean {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    private DefaultRedisScript<Long> defaultRedisScript;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CouponMQPublisher couponMQPublisher;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void addShopCoupon(Coupon coupon) {

    }

    @Override
    public R add2Count(Long batchId) {

//        Coupon coupon = selectCouponInfo(batchId);
//
//        String userId = SecurityContextHolder.getUserId();
//        String key = coupon.getShopId() + CouponConstants.SHOP_COUPON + userId;
//
//        /*
//         * 1,抢购优惠券，需要判断库存，需要库存的key，库存不足返回-1
//         * 2,查看自己的优惠券集合，是否有该优惠券id，如果有，返回1，
//         * 3如果没有，库存减一，将优惠券id添加到自己的优惠券集合中，返回0
//         */
//        String stockKey =  CouponConstants.COUPON_STOCK + batchId;
//
//        // 用户优惠券列表使用set结构
//        String userKey = userId + ":coupon:";
//
//        return redisTemplate.execute(defaultRedisScript, Collections.emptyList(), stockKey, userKey, batchId);

        // 抢券：需要注意，只能抢一张，使用lua脚本
        // 用户的优惠券每一个批次只能有一张，所以使用set结构，抢券先需要判断是否已抢过，是的话直接返回提示，
        // 不是的话需要获取Redisson分布式锁，判断库存是否大于0，是的话将优惠券信息发送到mq，由mq添加到数据库

        Long userId = SecurityContextHolder.getUserId();
        String key = "user:" + userId;
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, batchId))){
            return R.fail("一个人只能抢一张券哦!");
        }

        RLock lock = redissonClient.getLock("coupon" + batchId);

        try {
            lock.lock(200, TimeUnit.MILLISECONDS);
            ValueOperations<String, Object> redisOperation = redisTemplate.opsForValue();

            if (Integer.parseInt(Objects.requireNonNull(redisOperation.get(SellerConstants.COUPON_KEY + batchId)).toString()) > 0){
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setBatchId(batchId).setUserId(userId);

                couponMQPublisher.send2rabbit(userCoupon);

                return R.ok("抢券成功！");
            }

            return R.fail("一个人只能抢一张券哦!");

        }catch (Exception e){
            log.error("抢券出现异常:{}",e.getMessage());
            throw new BadRequestException("抢券出现异常，请重试");
        }

    }

    /**
     * 优惠券回滚，
     * @param orderId 优惠券Id
     */
    @Override
    public void couponRollback(Long orderId) {
        Coupon coupon = selectCouponInfo(orderId);


    }


    @Override
    public Coupon selectCouponInfo(Long batchId) {
        if (batchId < 0){
            return null;
        }
        return couponRepository.selectCouponInfo(batchId);
    }

    @Override
    public void insert2db(Coupon coupon) {

    }


    /**
     * 属性装填完毕之后会自动执行
     */
    @Override
    public void afterPropertiesSet() {
        ClassPathResource resource = new ClassPathResource("coupon.lua");
        defaultRedisScript.setLocation(resource);
        defaultRedisScript.setResultType(Long.class);
    }
}
