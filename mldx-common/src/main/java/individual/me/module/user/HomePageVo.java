package individual.me.module.user;

import individual.me.domain.User;
import individual.me.module.product.ShopProduct;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class HomePageVo implements Serializable {

    private List<ShopProduct> shopProducts;
    private List<ShoppingCartVo> shopCartVos;
    private UserVo userInfo;

}