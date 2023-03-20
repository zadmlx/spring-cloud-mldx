package individual.me.Controller;

import cn.hutool.core.util.IdUtil;
import individual.me.client.CloudCouponService;
import individual.me.client.CloudOrderService;
import individual.me.client.CloudProductService;
import individual.me.config.SecurityContextHolder;
import individual.me.domain.R;
import individual.me.module.coupon.UserCoupon;
import individual.me.module.order.CreateOrderVo;
import individual.me.module.order.Order;
import individual.me.module.order.RenderedOrder;
import individual.me.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userOrder")
public class OrderController {


    @Autowired
    private CloudOrderService cloudOrderService;

    @Autowired
    private CloudProductService cloudProductService;

    @Autowired
    private CloudCouponService cloudCouponService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);

    /**
     * 结算订单
     * 1 传入需要结算的产品id集合，和数量
     * 2 根据id查询产品信息，
     * 3 查询用户地址信息
     * 4 查询用户优惠券信息
     * 5 封装到一个对象，返回前端
     * @param productInfos 产品信息
     * @return
     */
    @PostMapping("/render")
    public RenderedOrder buildOrder(@RequestBody List<CreateOrderVo> productInfos){

        Long userId = SecurityContextHolder.getUserId();
        RenderedOrder renderedOrder = cloudOrderService.renderOrder(productInfos);
        renderedOrder.setUserId(userId);
        return renderedOrder;
    }


    /**
     * 创建订单
     * @param renderedOrder 渲染完成的订单
     * @return
     */
    @PostMapping("/create")
    public R buildOrder(@RequestBody RenderedOrder renderedOrder){

        cloudOrderService.createOrder(renderedOrder);

    }
}
