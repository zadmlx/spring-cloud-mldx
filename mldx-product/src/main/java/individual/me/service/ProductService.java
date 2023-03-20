package individual.me.service;

import individual.me.module.product.ProductVo;
import individual.me.module.product.ShopProduct;

import java.util.List;
import java.util.Set;

public interface ProductService {

    /**
     * 根据id查询店铺所有产品
     * @param shopId 店铺id
     * @return 店铺产品
     */
    List<ShopProduct> selectAllProduct(Long shopId);

    /**
     * 根据种类id查询该种类下所有产品
     * @param categoryId 种类id
     * @return 产品
     */
    List<ShopProduct> selectOneType(Long shopId,Long categoryId);

    /**
     * 根据产品id查询某产品详细信息
     * @param proId 产品id
     * @return 产品信息
     */
    ProductVo selectProduct(Long proId);

    /**
     * 更新产品信息
     * @param product
     */
    void updateProduct(ShopProduct product);

    /**
     * 根据id批量删除
     * @param ids 删除id集合
     */
    void deleteProducts(List<Long> ids);

    void insertProduct(ShopProduct product);

    List<ShopProduct> selectShopProducts(Set<Long> productIds);

}
