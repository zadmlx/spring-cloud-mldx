package individual.me.module.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ShoppingCartVo implements Serializable {

    private Long id;
    // 产品简介
    private String description;
    // 分类id
    private Long categoryId;
    // 产品规格
    private String detail;
    //产品价格
    private Double price;
    private Integer amount;
}
