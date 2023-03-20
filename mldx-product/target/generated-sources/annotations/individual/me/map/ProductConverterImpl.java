package individual.me.map;

import individual.me.module.product.ProductVo;
import individual.me.module.product.ShopProduct;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-17T21:42:49+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class ProductConverterImpl implements ProductConverter {

    @Override
    public ProductVo toVo(ShopProduct product) {
        if ( product == null ) {
            return null;
        }

        ProductVo productVo = new ProductVo();

        productVo.setId( product.getId() );
        productVo.setShopId( product.getShopId() );
        productVo.setCategoryId( product.getCategoryId() );
        productVo.setProductName( product.getProductName() );
        productVo.setDescription( product.getDescription() );
        productVo.setPrice( product.getPrice() );
        productVo.setStatus( product.getStatus() );

        return productVo;
    }

    @Override
    public List<ProductVo> toVos(List<ShopProduct> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductVo> list = new ArrayList<ProductVo>( products.size() );
        for ( ShopProduct shopProduct : products ) {
            list.add( toVo( shopProduct ) );
        }

        return list;
    }
}
