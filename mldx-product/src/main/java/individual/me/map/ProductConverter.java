package individual.me.map;

import individual.me.module.product.ProductVo;
import individual.me.module.product.ShopProduct;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductConverter {

    ProductVo toVo(ShopProduct product);
    List<ProductVo> toVos(List<ShopProduct> products);

}
