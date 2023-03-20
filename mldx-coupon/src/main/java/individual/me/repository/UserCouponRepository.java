package individual.me.repository;

import individual.me.module.coupon.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCouponRepository {

    void insertUsrCoupon(UserCoupon userCoupon);

    void updateUserCoupon(UserCoupon userCoupon);

    /**
     * 根据订单编号回滚优惠券信息，状态设置为未使用，清除订单编号
     * @param orderId 订单编号
     */
    void rollbackCoupon(Long orderId);

}
