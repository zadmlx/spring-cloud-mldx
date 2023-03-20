package individual.me.service.impl;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import individual.me.repository.RefundOrderRepository;
import individual.me.service.RefundOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    @Autowired
    private RefundOrderRepository repository;


    private RabbitTemplate rabbitTemplate;



    @Override
    public void updateRefundOrder(RefundOrder refundOrder) {

    }

    @Override
    public void insertRefundOrder(RefundOrder refund) {

    }

    @Override
    public RefundOrder selectRefundOrder(Long orderId) {
        return null;
    }

    /**
     * 查询所有待处理的退款的订单，但是需要过滤部分申请取消退款的订单
     * @return 待处理的订单
     */
    @Override
    public List<RefundOrder> selectUnhandledRefundOrders() {
        List<RefundOrder> refundOrders = repository.selectUnhandledRefundOrders();
        return refundOrders.stream().filter(o -> o.getStatus() == 0).toList();
    }


    @Override
    public List<RefundOrder> selectAllRefundOrders() {
        return null;
    }
}
