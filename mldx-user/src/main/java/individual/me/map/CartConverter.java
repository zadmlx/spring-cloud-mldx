package individual.me.map;

import individual.me.module.product.ShopProduct;
import individual.me.module.user.RedisCart;
import individual.me.module.user.ShoppingCartVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Component
public class CartConverter {

    public List<ShoppingCartVo> toShopCartVos(List<ShopProduct> shopProducts, List<RedisCart> redisCarts) {
        if (CollectionUtils.isEmpty(shopProducts) || CollectionUtils.isEmpty(redisCarts)){
            return null;
        }
        ArrayList<ShoppingCartVo> list = new ArrayList<>();


        shopProducts.forEach(shopProduct->{
            for (RedisCart redisCart : redisCarts) {
                if (redisCart.getId().equals(shopProduct.getId())){
                    ShoppingCartVo v = new ShoppingCartVo();
                    v.setId(shopProduct.getId()).setAmount(redisCart.getAmount()).setPrice(shopProduct.getPrice())
                            .setDescription(shopProduct.getDescription()).setCategoryId(shopProduct.getCategoryId()).setDetail(redisCart.getDetail());
                    list.add(v);
                }
            }
        });


        return list;
    }
}
