package individual.me.client;

import individual.me.domain.R;
import individual.me.module.order.CreateOrderVo;
import individual.me.module.order.Order;
import individual.me.module.order.RenderedOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@FeignClient("mldx-order")
public interface CloudOrderService {

    @GetMapping("/order/{id}")
    public Order selectOrder(@PathVariable("id")Long orderId);

    @GetMapping("/order/update")
    public void updateOrder(@RequestBody Order order);

    @PostMapping("/order/render")
    RenderedOrder renderOrder(@RequestBody List<CreateOrderVo> productInfos);

    @PostMapping("/order/build")
    R createOrder(RenderedOrder renderedOrder);
}
