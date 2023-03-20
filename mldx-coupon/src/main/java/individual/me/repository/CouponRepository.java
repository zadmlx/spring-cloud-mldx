package individual.me.repository;

import individual.me.module.coupon.Coupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponRepository {

    /**
     * 店主添加店铺优惠券
     * @param coupon 店铺优惠券信息
     */
    void addShopCoupon(Coupon coupon);

    /**
     * 添加优惠券到自己账户下，由redis保存
     * @param couponId 优惠券Id
     */
    void add2Count(Long couponId);

    /**
     * 退款时，优惠券回滚
     * @param couponId 优惠券Id
     */
    void refund(Long couponId);


    // 根据优惠券批次查询优惠券信息
    Coupon selectCouponInfo(Long batchId);

}
