package individual.me.domain;

import lombok.Data;

/**
 * 退款订单申请的实体类，前端页面获取用户手机号，id，退款原因，退款金额
 */
@Data
public class RefundOrderApplier {

    private Long orderId;
    private String username;
    private String phone;
    private String reason;
}
