package individual.me.service;


import individual.me.domain.User;
import individual.me.module.user.ShoppingCartVo;
import individual.me.module.user.UserVo;

import java.util.List;

public interface UserService {

    UserVo loadCurrentUser();

    public UserVo loadUserByUserId(Long userId);
    void update(User user);


    List<List<ShoppingCartVo>> selectUserShoppingCart(Long userId);

    void add2shoppingCart(Long productId,String itemInfo);

}
