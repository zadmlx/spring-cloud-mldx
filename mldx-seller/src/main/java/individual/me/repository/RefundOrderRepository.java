package individual.me.repository;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RefundOrderRepository {

    void updateRefundOrder(Order order);

    void insertRefundOrder(RefundOrder refund);

    RefundOrder selectRefundOrder(Long orderId);

    /**
     * 查询未处理的订单
     * @return 未处理的订单信息
     * TODO
     * 此处还是加个行锁吧，lock in sharemode
     */
    List<RefundOrder> selectUnhandledRefundOrders();

    /**
     * 查询所有退款订单
     * @return 退款订单
     */
    List<RefundOrder> selectAllRefundOrders();
}
