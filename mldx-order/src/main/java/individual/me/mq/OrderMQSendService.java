package individual.me.mq;

import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static individual.me.constants.OrderMQConstants.*;


@Slf4j
@Service
public class OrderMQSendService {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendRefundOrder2Seller(RefundOrder refundOrder){
        CorrelationData correlationData = new CorrelationData(String.valueOf(refundOrder.getOrderId()));

        // 消息发送到交换机之后，继续发送，如果成功发送到队列，返回ack，失败就返回nack
        correlationData.getFuture().addCallback(r->{
            if (r.isAck()) {
                log.info("退款通知成功发送到商家!消息id:{}", correlationData.getId());
            } else {
                log.error("退款消息未成功发送到商家!消息id:{}", correlationData.getId());
            }
        },ex-> log.info("发送消息失败:{}",ex.getMessage()));
        rabbitTemplate.convertAndSend(REFUND_EXCHANGE,REFUND_KEY,refundOrder,new CorrelationData(String.valueOf(refundOrder.getOrderId())));
    }


    /**
     * 将订单保存30分钟，不支付就取消
     * @param orderId 订单id
     */
    public void keepOrder30minutes(Long orderId){
        rabbitTemplate.convertAndSend(UNPAYED_ORDER_EXCHANGE,UNPAYED_ORDER_KEY,orderId,(m) ->{
            // 设置订单30分钟超时，超时后检查订单状态，如果未支付就取消
            m.getMessageProperties().setDelay(30 * 1000);
            return m;
        },new CorrelationData(String.valueOf(orderId)));
    }
}
