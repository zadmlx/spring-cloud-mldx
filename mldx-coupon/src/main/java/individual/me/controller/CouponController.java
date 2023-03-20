package individual.me.controller;

import individual.me.config.SecurityContextHolder;
import individual.me.domain.R;
import individual.me.module.coupon.Coupon;
import individual.me.module.coupon.CouponRollbackDto;
import individual.me.module.coupon.UserCoupon;
import individual.me.service.CouponService;
import individual.me.service.UserCouponService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 优惠券控制器。
 *
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {


    @Autowired
    public CouponService couponService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 抢券
     * @param batchId 优惠券批次id
     * @return
     */
    @GetMapping("/take/{batchId}")
    public R takeCoupon(@PathVariable("batchId")Long batchId){
        return couponService.add2Count(batchId);
    }

    /**
     * 根据优惠券id查询优惠券信息
     * @param couponId 优惠券id
     * @return
     */
    @GetMapping("/get/{couponId}")
    public Coupon selectCoupon(@PathVariable("couponId")Long couponId){
        return couponService.selectCouponInfo(couponId);
    }




    /**
     * 查询用户所有的的优惠券
     * @param userId 用户ID
     * @return
     */
    // 远程调用
    @GetMapping("/get/{id}")
    public List<UserCoupon> RemoteSelectCoupon(@PathVariable("id")Long userId){
        return userCouponService.selectUserCoupon(userId);
    }

    /**
     * 根据订单id查询优惠券
     */
    @GetMapping("/select1")
    public UserCoupon selectUserCouponByOrderId(Long orderId) {
        return userCouponService.selectUserCouponByOrderId(orderId);
    }


    @PostMapping("/rollBack/{orderId}")
    public R couponRollback(@PathVariable("orderId") Long orderId){
        userCouponService.rollbackCoupon(orderId);
        return R.ok();
    }

}
