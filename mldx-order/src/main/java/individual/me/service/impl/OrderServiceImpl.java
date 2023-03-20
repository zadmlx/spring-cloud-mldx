package individual.me.service.impl;

import individual.me.config.SecurityContextHolder;
import individual.me.module.order.OrderVo;
import individual.me.domain.RefundOrderApplier;
import individual.me.exception.BadRequestException;
import individual.me.module.order.Order;
import individual.me.module.order.RefundOrder;
import individual.me.mq.OrderMQSendService;
import individual.me.service.OrderService;
import individual.me.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private OrderMQSendService orderMQSendService;





    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public void payOrder() {

    }

    /**
     * 申请退款,校验订单状态，判断是否可以进行退款，可以的话就创建退款订单，然后通知卖家，卖家同意和不同意均在退款订单中进行操作
     *
     */
    @Override
    public void applyRefund(RefundOrderApplier applier) {
        // 查询该订单的信息
        Order order = selectOrder(applier.getOrderId());

        if (order == null){
            throw new BadRequestException("请求订单为空");
        }


        // 查询订单状态 订单支付状态，1 未支付 2 已支付 3 已取消 4 退款中 5 已退款
        Integer status = order.getStatus();
        switch (status){
            case 1 -> throw new BadRequestException("订单未支付，不能退款");
            case 3 -> throw new BadRequestException("订单已取消，不能执行退款");
            case 4 -> throw new BadRequestException("正在退款中哦！");
            case 5 -> throw new BadRequestException("已退款，不能重复退款哦");
        }

        // 校验原始订单状态完毕，创建退款订单，存入数据库,使用mq去完成，减少等待时间
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setOrderId(order.getId()).
                setStatus(1).
                setBuyer(applier.getUsername()).
                setPrice(order.getActualPay()).
                setBuyerReason(applier.getReason()).
                setContractPhone(applier.getPhone()).
                setType(1).
                setUserId(order.getUserId());

        // 订单创建完毕，通知卖家进行处理
        refundOrderService.insertRefundOrder(refundOrder);
        orderMQSendService.sendRefundOrder2Seller(refundOrder);

    }

    @Override
    public Order selectOrder(Long orderId) {



        /**
         * TODO
         */

        return new Order();
    }





    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public void buildOrder() {
        Long userId = SecurityContextHolder.getUserId();

        // 这里使用异步操作，远程调用优惠券微服务，
    }

    @Override
    public void saveOrder(Order order) {

    }
}
