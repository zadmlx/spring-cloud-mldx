package individual.me.module.order;


import lombok.Data;

// 前端传入的创建订单所需的信息
@Data
public class CreateOrderVo {

    private Long productId;
    // 份数
    private Long counts;

}
