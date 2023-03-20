package individual.me.module.coupon;

import lombok.Data;

@Data
public class CouponRollbackDto {
    private Long batchId;
    private Long orderId;
}
