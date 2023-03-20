package individual.me.controller;

import individual.me.constants.OrderMQConstants;
import individual.me.domain.R;
import individual.me.module.order.RefundOrder;
import individual.me.service.RefundOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refund")
public class RefundController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RefundOrderService refundOrderService;


    @PostMapping("/agree")
    public void agreeRefundOrder(@RequestBody RefundOrder refundOrder){
        rabbitTemplate.convertAndSend(OrderMQConstants.REFUND_EXCHANGE,OrderMQConstants.SELL_AGREE_REFUND_KEY,refundOrder);
    }

    @PostMapping("/deny")
    public void denyRefundOrder(@RequestBody RefundOrder refundOrder){

        rabbitTemplate.convertAndSend(OrderMQConstants.REFUND_EXCHANGE,OrderMQConstants.SELL_DENY_REFUND_KEY,refundOrder);
    }


    @PostMapping("refund")
    public List<RefundOrder> selectAllRefundOrders(){
        return refundOrderService.selectAllRefundOrders();
    }

    @GetMapping("all")
    public List<RefundOrder> selectUnhandledRefundOrders(){
        return refundOrderService.selectUnhandledRefundOrders();
    }
}
