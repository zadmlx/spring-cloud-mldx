package individual.me.controller;


import cn.hutool.core.util.RandomUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import individual.me.constants.CacheConstants;
import individual.me.domain.LoginUser;
import individual.me.domain.R;
import individual.me.domain.SecurityUser;
import individual.me.exception.BadRequestException;
import individual.me.security.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController("/admin")
public class AuthController {

    public static final String ONLINE_USER_PREFIX = "online:user:";
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/auth")
    public R login(@RequestBody @Validated LoginUser user){
        String uuid = user.getUuid();
        String code = (String) redisTemplate.opsForValue().getAndDelete(uuid);
        if (!StringUtils.hasText(code) || !code.equals(user.getCode())){
            throw new BadRequestException("验证码错误或已过期，请重试");
        }
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenService.createToken(authentication);

        // 获取用户id，将固定前缀+id作为key，token作为value 存入redis
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        redisTemplate.opsForValue().set(ONLINE_USER_PREFIX + securityUser.getSeller().getId(),token,2,TimeUnit.HOURS);
        log.info("认证结束");
        return R.ok("登录成功",token);

    }



    @GetMapping("/code")
    public R sendCode(){
        String uuid = RandomUtil.randomString(6);
        Captcha captcha = new SpecCaptcha(130,48,5);
        String text = captcha.text();
        log.info("image:{}",captcha.toBase64());
        redisTemplate.opsForValue().set(CacheConstants.VERIFY_TOKEN_PREFIX + uuid,text,CacheConstants.VC_TTL, TimeUnit.SECONDS);

        Map<String,Object> map = new HashMap<>(4);
        map.put("uuid",uuid);
        map.put("code",text);
        
        return R.ok(map);
    }
}
