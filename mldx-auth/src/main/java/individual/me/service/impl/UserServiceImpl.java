package individual.me.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import individual.me.constants.CacheConstants;
import individual.me.domain.LoginUser;
import individual.me.domain.R;
import individual.me.domain.User;
import individual.me.exception.BadRequestException;
import individual.me.exception.NotFoundException;
import individual.me.repository.UserRepository;
import individual.me.service.PasswordService;
import individual.me.service.UserService;
import individual.me.util.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JwtService jwtService;
    @Override
    public R login(LoginUser loginUser) {
        String uuid = loginUser.getUuid();
        String code = (String) redisTemplate.opsForValue().getAndDelete(CacheConstants.VERIFY_TOKEN_PREFIX + uuid);

        if (!StringUtils.hasText(code)){
            throw new BadRequestException("验证码不存在或已过期");
        }

        if (!loginUser.getCode().equalsIgnoreCase(code)){
            throw new BadRequestException("验证码不正确");
        }
        User user = userRepository.loadUserByUsername(loginUser.getUsername());
        if (user == null) throw new NotFoundException("用户名或密码错误");

        if (!passwordService.validate(loginUser.getPassword(), user.getPassword())){
            throw new BadRequestException("账号或密码不正确");
        }

        String token = jwtService.createToken(user);
        redisTemplate.opsForValue().set(CacheConstants.ONLINE_TOKEN_KEY +user.getId(),token,CacheConstants.TOKEN_RENEW_TTL,TimeUnit.SECONDS);

        return R.ok("登录成功",token);

    }



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
