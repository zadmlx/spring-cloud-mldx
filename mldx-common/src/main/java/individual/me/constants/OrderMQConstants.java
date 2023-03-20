package individual.me.constants;

public class OrderMQConstants {

    // ******************************* 申请退款 ******************************
    public static final String REFUND_EXCHANGE = "mldx.apply.refund.exchange";
    public static final String REFUND_QUEUE = "mldx.apply.refund.queue";
    public static final String REFUND_KEY = "mldx.apply.refund.key";


    // ****************************** 处理退款 **************************************

    public static final String SELL_AGREE_REFUND_KEY = "mldx.seller.agree.refund.key";
    public static final String SELL_DENY_REFUND_KEY = "mldx.seller.deny.refund.key";



    public static final String UNPAYED_ORDER_EXCHANGE = "mldx.unpayed.order.exchange";
    public static final String UNPAYED_ORDER_QUEUE = "mldx.unpayed.order.queue";
    public static final String UNPAYED_ORDER_KEY = "mldx.unpayed.order.key";
}
