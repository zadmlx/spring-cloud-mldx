package individual.me.module.order;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 我们实际上就是在购买优惠券，优惠券分两种 普通的和秒杀限量的
 * 实际创建的优惠券订单类
 */
@Data
@Accessors(chain = true)
public class Order implements Serializable {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 购买的代金券id
     */
    private List<Long> couponIds;

    /**
     * 支付方式，1 微信，2 支付宝，3 云闪付，4 数字人民币
     */
    private Integer payType;

    /**
     * 订单支付状态，1 未支付 2 已支付 3 已取消 4 退款中 5 已退款
     */
    private Integer status;

    /**
     * 商品总价
     */
    private Double goodsAmount;

    /**
     * 折扣金额
     */
    private Double discount;

    /**
     * 实际支付金额
     */
    private Double actualPay;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;

}
