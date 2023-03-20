package individual.me.module.coupon;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
public class Coupon implements Serializable {

    private Long id;
    // 需要一个字段记录是优惠券的批次
    private Long batch;
    private Long shopId;
    private String title;
    private String subTitle;
    private String rules;
    // 订单使用优惠券金额门栏
    private Double beginMount;
    //满beginMount之后优惠的金额
    private Double discountValue;
    /**
     * 优惠券使用规则，0 满减 ，1 固定折扣
     */
    private Integer discountType;
    /**
     * 固定折扣优惠券折扣使用上限
     */
    private Double maxDiscount;
    private Integer stock;
    private LocalDateTime beginTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;





}
