package individual.me.module.coupon;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserCoupon {

    // 用户id
    private Long id;
    // 需要一个字段记录是优惠券的批次
    private Long userId;
    private Long batchId;

    // 0 未使用，1 已使用
    private Long status;

    private LocalDateTime beginTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    // 优惠券被哪个订单使用
    private Long outOrderNo;
}
