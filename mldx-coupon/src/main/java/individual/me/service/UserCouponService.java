package individual.me.service;

import individual.me.module.coupon.UserCoupon;
import individual.me.module.coupon.CouponRollbackDto;

import java.util.List;

public interface UserCouponService {

    void insertCoupon(UserCoupon userCoupon);

    void updateCoupon(UserCoupon userCoupon);

    void rollbackCoupon(Long orderId);

    List<UserCoupon> selectUserCoupon(Long userId);


    UserCoupon selectUserCouponByOrderId(Long orderId);
}
