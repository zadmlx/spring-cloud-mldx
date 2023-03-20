package individual.me.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVo {
    private Long id;
    private Long shopId;
    private String title;
    private String subTitle;
    private String rules;
    // 订单使用优惠券金额门栏
    private Double beginMount;
    //满beginMount之后优惠的金额
    private Double discountValue;
    /**
     * 固定折扣优惠券折扣使用上限
     */
    private Double maxDiscount;
    //private Integer stock;
    private LocalDateTime beginTime;
    private LocalDateTime expireTime;
}
