package individual.me.map;

import individual.me.module.user.RedisCart;
import individual.me.module.user.ShoppingCartVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RedisCartConverter {

    List<ShoppingCartVo> toVos(List<RedisCart> redisCarts);
}
