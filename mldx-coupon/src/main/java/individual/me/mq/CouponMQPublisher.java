package individual.me.mq;

import individual.me.constants.CouponConstants;
import individual.me.module.coupon.UserCoupon;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponMQPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 监听用户抢券信息，userCoupon对象中携带了批次id和用户id，根据批次id查询到完整的优惠券信息
    public void send2rabbit(UserCoupon userCoupon){
        rabbitTemplate.convertAndSend(CouponConstants.SAVE_COUPON_EXCHANGE,CouponConstants.SAVE_COUPON_KEY,userCoupon);
    }



}
