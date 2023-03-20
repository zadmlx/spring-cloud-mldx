package individual.me.controller;

import individual.me.module.product.ProductVo;
import individual.me.map.ProductConverter;
import individual.me.module.product.ShopProduct;
import individual.me.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter productConverter;

    @PostMapping("/{shopId}/{typeId}")
    public List<ShopProduct> selectOneType(@PathVariable(value = "shopId") Long shopId,@PathVariable(value = "typeId",required = false) Long typeId){
        log.info("接收到查询请求，shopId:{},typeId:{}",shopId,typeId);
        if (null == typeId) typeId = 0L;
        return productService.selectOneType(shopId, typeId);
    }

    @GetMapping("/select")
    public List<ShopProduct> selectShopProducts(@RequestParam Set<Long> ids){
        return productService.selectShopProducts(ids);
    }

    @PostMapping("/selectVos")
    public List<ProductVo> selectProductVo(@RequestBody Set<Long> ids){
        List<ShopProduct> shopProducts = productService.selectShopProducts(ids);
        return productConverter.toVos(shopProducts);
    }
}
