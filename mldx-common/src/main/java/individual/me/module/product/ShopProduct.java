package individual.me.module.product;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品实体类
 */
@Data
public class ShopProduct {
    private Long id;
    private Long shopId;
    private Long categoryId;
    private String productName;
    private String description;
    private Double price;
    private Integer sort;//越大越靠前
    private Integer status;// 0 上架  1 未上架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;
    private Integer isDeleted; // 0 yes 1 no

}
