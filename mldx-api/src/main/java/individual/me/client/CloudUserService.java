package individual.me.client;

import individual.me.domain.R;
import individual.me.domain.User;
import individual.me.module.user.ShoppingCartVo;
import individual.me.module.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("mldx-user")
public interface CloudUserService {

    @GetMapping("/user")
    R getCurrentUser();

    @GetMapping("/user/{id}")
    UserVo selectUser(@PathVariable("id")Long userId);

    @GetMapping("/shopcart/{userId}")
    List<List<ShoppingCartVo>> remoteSelectShoppingCart(@PathVariable("userId")Long userId);
}
