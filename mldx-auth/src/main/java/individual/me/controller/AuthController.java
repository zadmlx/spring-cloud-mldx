package individual.me.controller;


import individual.me.annotation.Gateway;
import individual.me.annotation.Limit;
import individual.me.domain.LoginUser;
import individual.me.domain.R;
import individual.me.domain.RegisterBody;
import individual.me.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Gateway
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody @Validated LoginUser user){
        return userService.login(user);
    }

    @GetMapping("/code")
    public R sendCode(){
        return userService.sendCode();
    }


    @GetMapping("/register/email")
    public R registerViaEmail(@RequestBody @Validated(RegisterBody.Email.class) RegisterBody body){

        return null;
    }

    @Limit(count = 2,ttl = 60)
    @GetMapping("/register/phone")
    public R registerViaPhone(@RequestBody @Validated(RegisterBody.Phone.class) RegisterBody body){

        return null;
    }

    @GetMapping("/get/{e}")
    public R get(@PathVariable("e") String e){
        Object o = redisTemplate.opsForValue().get(e);
        return R.ok(o);
    }

}
