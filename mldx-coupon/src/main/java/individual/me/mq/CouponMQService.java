package individual.me.mq;

import com.rabbitmq.client.Channel;
import individual.me.constants.CouponConstants;
import individual.me.module.coupon.Coupon;
import individual.me.module.coupon.CouponRollbackDto;
import individual.me.module.coupon.UserCoupon;
import individual.me.service.CouponService;
import individual.me.service.UserCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


/**
 * 优惠券回滚服务
 */
@Slf4j
@Service
public class CouponMQService {


    @Autowired
    private CouponService couponService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    /**
     * 优惠券回滚，需要两个信息，优惠券id和订单id
     * 1 查询该优惠券是否存在，
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = CouponConstants.COUPON_ROLLBACK_QUEUE),
            exchange = @Exchange(name = CouponConstants.COUPON_ROLLBACK_EXCHANGE),
            key = (CouponConstants.COUPON_ROLLBACK_KEY)),ackMode = "MANUAL")
    public void rollbackCoupon(@Payload CouponRollbackDto rollbackDto, Channel channel, Message message){
        Coupon coupon = couponService.selectCouponInfo(rollbackDto.getBatchId());
        if (ObjectUtils.isEmpty(coupon)){
            log.info("优惠券信息为空，无法执行回退");
        }
        // 开始回滚优惠券
        log.info("开始回滚优惠券");
        userCouponService.rollbackCoupon(rollbackDto.getOrderId());
    }




    /**
     * 监听优惠券抢购情况，一旦有用户成功抢购，添加到数据库，
     * 此处监听到的用户优惠券信息只有批次id和用户id，需要根据批次id查询优惠券信息，然后填充到用户优惠券信息中，然后存入db
     * @param userCoupon 携带批次id和用户id的优惠券对象
     */
    @RabbitListener
    public void addCoupon2count(UserCoupon userCoupon){
        log.info("监听到用户抢券成功，添加到用户账户");

        // 获取coupon完整信息，填充到用户优惠券信息，存入数据库
        Coupon coupon = couponService.selectCouponInfo(userCoupon.getBatchId());
        userCoupon.setExpireTime(coupon.getExpireTime()).setBeginTime(coupon.getBeginTime());
        userCouponService.insertCoupon(userCoupon);
    }

}
