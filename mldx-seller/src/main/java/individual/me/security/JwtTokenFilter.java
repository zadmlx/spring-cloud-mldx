package individual.me.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String ONLINE_USER_PREFIX = "online:user:";
    public static final Long  TOKEN_RENEW_SECONDS = 1800L;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)){
            // 解析出用户id，校验是否已退出，因为我们使用redis维护jwt token的状态，当用户退出登录或者长时间没有操作的时候，token在redis会被删除，
            // 所以需要通过id去查询redis，如果redis不存在该id的token，那么说明token已过期
            Long userId = jwtTokenService.getUserId();
            String redisToken = (String) redisTemplate.opsForValue().get(ONLINE_USER_PREFIX + userId);
            if (StringUtils.hasText(redisToken) && token.equals(redisToken)){
                // 校验了用户状态和token正确性，从token中解析出用户信息，封装到UsernamePasswordAuthenticationToken中
                Authentication authentication = jwtTokenService.createAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                refreshToken(userId);
            }
        }

        filterChain.doFilter(request,response);
    }


    private void refreshToken(Long userId){{
        // 获得当前token过期时间，小于半小时，就续期，续期半小时
        Long leftSeconds = redisTemplate.getExpire(ONLINE_USER_PREFIX + userId, TimeUnit.SECONDS);
        assert leftSeconds != null;
        if (leftSeconds < TOKEN_RENEW_SECONDS){
            redisTemplate.expire(ONLINE_USER_PREFIX + userId,leftSeconds + TOKEN_RENEW_SECONDS,TimeUnit.SECONDS);
        }
    }

    }
}
;