package individual.me.domain;

import lombok.Data;


/**
 * 前端封装添加产品到购物车的信息为该类，需要包括产品主键，产品规格，产品的添加数量
 */
@Data
public class CartRequestVo {

    private Long itemId;
    private String detail;
}
