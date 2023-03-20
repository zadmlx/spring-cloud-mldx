package individual.me.service;

import individual.me.module.order.OrderVo;
import individual.me.domain.RefundOrderApplier;
import individual.me.module.order.Order;

public interface OrderService {


    void deleteOrder(Long orderId);

    void payOrder();

    void applyRefund(RefundOrderApplier applier);

    Order selectOrder(Long orderId);

    void updateOrder(Order order);

    /**
     * 结算，获取购物车产品，地址簿，和优惠券
     */
    void buildOrder();

    /**
     * 真正创建订单，
     */
    void saveOrder(Order order);
}
