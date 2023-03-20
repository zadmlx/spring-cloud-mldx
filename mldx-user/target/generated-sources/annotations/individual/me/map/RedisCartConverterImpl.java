package individual.me.map;

import individual.me.module.user.RedisCart;
import individual.me.module.user.ShoppingCartVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-18T00:11:58+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class RedisCartConverterImpl implements RedisCartConverter {

    @Override
    public List<ShoppingCartVo> toVos(List<RedisCart> redisCarts) {
        if ( redisCarts == null ) {
            return null;
        }

        List<ShoppingCartVo> list = new ArrayList<ShoppingCartVo>( redisCarts.size() );
        for ( RedisCart redisCart : redisCarts ) {
            list.add( redisCartToShoppingCartVo( redisCart ) );
        }

        return list;
    }

    protected ShoppingCartVo redisCartToShoppingCartVo(RedisCart redisCart) {
        if ( redisCart == null ) {
            return null;
        }

        ShoppingCartVo shoppingCartVo = new ShoppingCartVo();

        shoppingCartVo.setId( redisCart.getId() );
        shoppingCartVo.setDetail( redisCart.getDetail() );
        shoppingCartVo.setAmount( redisCart.getAmount() );

        return shoppingCartVo;
    }
}
