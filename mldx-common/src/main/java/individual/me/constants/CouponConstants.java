package individual.me.constants;

public class CouponConstants {

    public static final String SHOP_COUPON = ":coupon:";
    public static final String COUPON_STOCK = "coupon:stock:";


    // ********************   mq  **************************


    public static final String SAVE_COUPON_EXCHANGE = "mldx.seller.coupon.save.exchange";
    public static final String SAVE_COUPON_QUEUE = "mldx.seller.coupon.save.queue";
    public static final String SAVE_COUPON_KEY = "mldx.seller.coupon.save.key";

            // ********************* MQ--->优惠券回滚

    public static final String COUPON_ROLLBACK_EXCHANGE = "mldx.coupon.rollback.exchange";
    public static final String COUPON_ROLLBACK_QUEUE = "mldx.coupon.rollback.queue";
    public static final String COUPON_ROLLBACK_KEY = "mldx.coupon.rollback.key";


}
