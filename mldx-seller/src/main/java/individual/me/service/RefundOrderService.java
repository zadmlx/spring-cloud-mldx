package individual.me.service;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;

import java.util.List;

public interface RefundOrderService {

    void updateRefundOrder(RefundOrder order);

    void insertRefundOrder(RefundOrder refund);

    RefundOrder selectRefundOrder(Long orderId);

    /**
     * 查询未处理的订单
     * @return 未处理的订单信息
     */
    List<RefundOrder> selectUnhandledRefundOrders();

    /**
     * 查询所有退款订单
     * @return 退款订单
     */
    List<RefundOrder> selectAllRefundOrders();

}
