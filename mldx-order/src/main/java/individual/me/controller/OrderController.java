package individual.me.controller;

import cn.hutool.core.util.IdUtil;
import individual.me.client.CloudCouponService;
import individual.me.client.CloudProductService;
import individual.me.config.SecurityContextHolder;
import individual.me.domain.R;
import individual.me.domain.RefundOrderApplier;
import individual.me.module.coupon.UserCoupon;
import individual.me.module.order.CreateOrderVo;
import individual.me.module.order.Order;
import individual.me.module.order.RenderedOrder;
import individual.me.module.product.ProductVo;
import individual.me.mq.OrderMQSendService;
import individual.me.service.OrderService;
import individual.me.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderService orderService;


    @Autowired
    private CloudProductService cloudProductService;

    @Autowired
    private CloudCouponService cloudCouponService;

    @Autowired
    private OrderMQSendService orderMQSendService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);

    // 创建订单需要什么？考虑一下，产品信息，优惠券，地址簿
    //

    @PostMapping("/build")
    public R createOrder(@RequestBody RenderedOrder renderedOrder){


        Order order = new Order();

        // 使用雪花id，分布式系统中，有一些需要使用全局唯一ID的场景，
        // 有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成，便于数据库插入时减少页分裂和合并
        long flakeId = IdUtil.getSnowflakeNextId();

        Double originPrice = renderedOrder.getTotalPrice();
        List<Long> couponIds = renderedOrder.getCoupons().stream().map(UserCoupon::getId).toList();
        order.setId(flakeId).setStatus(1).setDiscount(0.0).setActualPay(originPrice).setCouponIds(couponIds).setGoodsAmount(originPrice);
        orderService.saveOrder(order);
        orderMQSendService.keepOrder30minutes(order.getId());
        return null;
    }




    // 结算，获取加车的产品，获取优惠券，获取地址

    @PostMapping("/render")
    public RenderedOrder buildOrder(@RequestBody List<CreateOrderVo> productInfos){
        // 根据产品id查询产品vo
        Set<Long> ids = productInfos.stream().map(CreateOrderVo::getProductId).collect(Collectors.toSet());
        List<ProductVo> productVos = cloudProductService.selectProductVo(ids);

        // 创建前端所需的对象
        RenderedOrder renderedOrder = new RenderedOrder();
        // 根据id，获取用户的优惠券和地址
        Long userId = SecurityContextHolder.getUserId();

        CompletableFuture.runAsync(()->{
            List<UserCoupon> userCoupons = cloudCouponService.RemoteSelectCoupon(userId);
            renderedOrder.setCoupons(userCoupons);
        },threadPoolTaskExecutor);

        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);

        // 遍历获取所选产品价值
        productVos.forEach((i)->{
            for (CreateOrderVo infos : productInfos) {
                if (i.getId().equals(infos.getProductId())){
                    totalPrice.updateAndGet(v -> v + infos.getCounts() * i.getPrice());
                }
            }
        });

        renderedOrder.setTotalPrice(totalPrice.get());

        renderedOrder.setProductVos(productVos);

        return renderedOrder;

    }

    @GetMapping("/{id}")
    public Order selectOrder(@PathVariable("id")Long orderId){
        return null;
    }


    /**
     * 申请订单退款，1 买家发起申请，mq发送申请消息，2，卖家监听申请退款队列，同意或者不同意
     * @param
     * @return
     */
    @GetMapping("/refund")
    public R applyRefund(@RequestBody RefundOrderApplier applier){
        orderService.applyRefund(applier);
        return R.ok("退款申请已发送，请等待卖家处理");
    }

    @GetMapping("/update")
    public void updateOrder(@RequestBody Order order){
        orderService.updateOrder(order);
    }

}
