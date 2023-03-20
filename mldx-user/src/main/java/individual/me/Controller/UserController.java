package individual.me.Controller;


import individual.me.config.SecurityContextHolder;
import individual.me.domain.R;
import individual.me.module.user.UserVo;
import individual.me.module.user.ShoppingCartVo;
import individual.me.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping()
    public R getCurrentUser(){
        return R.ok(userService.loadCurrentUser());
    }


    @GetMapping("/{id}")
    public UserVo selectUser(@PathVariable("id")Long userId){
        return userService.loadUserByUserId(userId);
    }


    @GetMapping("/shopcart")
    public List<List<ShoppingCartVo>> selectShoppingCart(){
        Long userId = SecurityContextHolder.getUserId();
        return userService.selectUserShoppingCart(userId);
    }

    @GetMapping("/shopcart/{userId}")
    public List<List<ShoppingCartVo>> remoteSelectShoppingCart(@PathVariable("userId")Long userId){
        return userService.selectUserShoppingCart(userId);
    }

}
