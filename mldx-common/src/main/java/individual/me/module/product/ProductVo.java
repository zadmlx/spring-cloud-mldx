package individual.me.module.product;

import lombok.Data;

/**
 * 返回前端的产品信息类
 */

@Data
public class ProductVo {

    private Long id;
    private Long shopId;
    private Long categoryId;
    private String productName;
    private String description;
    private Double price;
    private Integer status;

}
