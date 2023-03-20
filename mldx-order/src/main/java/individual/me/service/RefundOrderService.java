package individual.me.service;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;

public interface RefundOrderService {

    void updateRefundOrder(Order order);

    void insertRefundOrder(RefundOrder refund);

    RefundOrder selectRefundOrder(Long orderId);


}
