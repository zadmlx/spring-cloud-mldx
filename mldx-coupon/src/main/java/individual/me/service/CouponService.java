package individual.me.service;

import individual.me.domain.R;
import individual.me.module.coupon.Coupon;

public interface CouponService {

    /**
     * 店主添加店铺优惠券
     * @param coupon 店铺优惠券信息
     */
    void addShopCoupon(Coupon coupon);

    /**
     * 添加优惠券到自己账户下，由redis保存
     * @param couponId 优惠券Id
     */
    R add2Count(Long couponId);

    /**
     * 退款时，优惠券回滚
     * @param OrderId 优惠券Id
     */
    void couponRollback(Long OrderId);

    Coupon selectCouponInfo(Long batchId);

    void insert2db(Coupon coupon);

}
