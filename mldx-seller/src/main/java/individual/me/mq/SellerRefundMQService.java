package individual.me.mq;

import com.rabbitmq.client.Channel;
import individual.me.client.CloudCouponService;
import individual.me.client.CloudOrderService;
import individual.me.module.coupon.CouponRollbackDto;
import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import individual.me.service.RefundOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static individual.me.constants.OrderMQConstants.*;

@Slf4j
@Service
public class SellerRefundMQService {


    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private CloudOrderService cloudOrderService;

    @Autowired
    private CloudCouponService cloudCouponService;


    /**
     *收到退款通知，提醒卖家
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(REFUND_QUEUE),
            exchange = @Exchange(value = REFUND_EXCHANGE),key = REFUND_KEY),ackMode = "AUTO")
    public void noticeSeller(@Payload RefundOrder refundOrder, Channel channel, Message message){
        log.info("收到一条退款消息，请及时处理，退款订单编号:{}",refundOrder.getOrderId());
    }


    // 在前端控制器中接收买家反馈，是否同意退款
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(REFUND_QUEUE),
            exchange = @Exchange(value = REFUND_EXCHANGE,type = ExchangeTypes.FANOUT),
            key = REFUND_KEY),ackMode = "MANUAL")
    public void agreeRefundOrder(@Payload RefundOrder refundOrder, Channel channel, Message message){
        SellerRefundMQService proxy = (SellerRefundMQService) AopContext.currentProxy();
        try {
            proxy.handleRefundOrder(refundOrder,true);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            // 抛出异常之后，重试3次，还是失败时候交由人工处理
            log.error("卖家拒绝退款，出现异常，请手动处理");
        }

    }

    // 拒绝退款
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(REFUND_QUEUE),
            exchange = @Exchange(value = REFUND_EXCHANGE,type = ExchangeTypes.FANOUT),
            key = REFUND_KEY),ackMode = "MANUAL")
    public void denyRefundOrder(@Payload RefundOrder refundOrder, Channel channel, Message message){

        // 必须使用代理对象进行方法调用，否则事务不会生效
        SellerRefundMQService proxy = (SellerRefundMQService) AopContext.currentProxy();
        try {
            proxy.handleRefundOrder(refundOrder,false);
        }catch (Exception e){
            // 出现错误重试三次之后重试次数耗尽之后，会调用MessageRecovery接口进行处理，我们可以实现该接口，
            // 添加自定义处理逻辑，然后发送到指定队列进行手动处理
            log.error("卖家同意退款，退款出现异常，请及时手动处理");
            throw e;
        }
    }

    /**
     * 处理退款订单默认同意，保存退款订单记录，回滚优惠券，将原来订单状态设置为取消，
     * 这三个步骤不在同一个服务中心，所以需要使用seata管理分布式事务。
     * @param refundOrder 退款订单
     * @param agree 是否同意退款
     */
    @GlobalTransactional
    void handleRefundOrder(RefundOrder refundOrder, boolean agree){
        if (!agree){
            log.info("卖家拒绝退款");
            return;
        }

        log.info("卖家同意退款，开始执行退款流程");

        // 此处没有检查退款订单状态，是在用户取消申请退款订单的时候进行检查：判断订单的状态是不是为申请中，
        refundOrderService.updateRefundOrder(refundOrder);
        log.info("退款订单状态修改为已退款执行成功");

        // 远程调用订单服务，修改原始订单状态 订单支付状态，1 未支付 2 已支付 3 已取消 4 退款中 5 已退款
        Order order = new Order();
        order.setStatus(5).setId(refundOrder.getOrderId());
        cloudOrderService.updateOrder(order);
        log.info("修改原始订单状态为已退款执行成功,开始回滚优惠券");

        // 查询原始订单信息。
        cloudCouponService.couponRollback(refundOrder.getOrderId());
        log.info("优惠券回滚成功，退款结束");
        // TODO 支付宝退款什么的，发个短信啥的，有空在写吧。

    }
}
