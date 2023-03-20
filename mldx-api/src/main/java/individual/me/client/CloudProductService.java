package individual.me.client;

import individual.me.domain.R;
import individual.me.fallback.ProductFallback;
import individual.me.module.product.ProductVo;
import individual.me.module.product.ShopProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(value = "mldx-product"/*,fallbackFactory = ProductFallback.class*/)
public interface CloudProductService {

    @GetMapping("/product/select")
    List<ShopProduct> selectShopProducts(Set<Long> productIds);

    @PostMapping("/product/{shopId}/{typeId}")
    List<ShopProduct> selectOneType(@PathVariable(value = "shopId") Long shopId,@PathVariable(value = "typeId") Long typeId);

    @PostMapping("/product/selectVos")
    List<ProductVo> selectProductVo(@RequestBody Set<Long> ids);
}
