package individual.me.mq;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import individual.me.service.OrderService;
import individual.me.service.RefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static individual.me.constants.OrderMQConstants.*;
import static individual.me.constants.SellerConstants.*;


@Slf4j
@Service
public class OrderMQListenerService {
    public static final String X_EXCEPTION_MESSAGE = "x-exception-message";

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private OrderService orderService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(REFUND_QUEUE),
            exchange = @Exchange(REFUND_EXCHANGE),key = REFUND_KEY),ackMode = "AUTO")
    public void notice2handleRefundOrder(@Payload RefundOrder refundOrder) {
        refundOrderService.insertRefundOrder(refundOrder);
        log.info("系统检测到退款申请，已保存退款订单，请及时处理，订单编号:{}",refundOrder.getOrderId());


    }


    // 手动处理抛出异常的订单

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(SELLER_UNHANDLED_REFUND_ORDER_QUEUE),
            exchange = @Exchange(value = SELLER_UNHANDLED_REFUND_ORDER_EXCHANGE),
            key = SELLER_UNHANDLED_REFUND_ORDER_KEY),ackMode = "AUTO")
    public void notice2handleUnresolvedRefundOrder(Message message){
        log.info("系统原因无法处理订单，请手动处理，异常:{}",message.getMessageProperties().getHeader(X_EXCEPTION_MESSAGE).toString());
    }


    /**
     * 处理到时间的订单
     * @param orderId 订单id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(UNPAYED_ORDER_QUEUE),
            exchange = @Exchange(value = UNPAYED_ORDER_EXCHANGE,delayed = "true"),
            key = UNPAYED_ORDER_KEY
    ),ackMode = "AUTO")
    public void handleTimedOrder(@Payload Long orderId){
        Order order = orderService.selectOrder(orderId);
        // 如果订单超时未支付，修改订单状态为3 已取消

        log.info("订单到期，开始检查订单状态，订单id:{}",orderId);
        if (order.getStatus() == 1){
            log.info("订单超时未支付，将取消订单");
            order.setStatus(3);
            orderService.updateOrder(order);
        }
        log.info("订单已支付，spring将自动确认已处理该消息");
    }
}
