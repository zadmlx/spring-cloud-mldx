package individual.me.util;

import cn.hutool.core.util.IdUtil;
import individual.me.constants.CacheConstants;
import individual.me.constants.StringConstants;
import individual.me.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class JwtService implements InitializingBean {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String TOKEN = "shanchunyonghuai1qu23liyancun45jiatingtai67zuo8910zhihuashanchunyonghuai1qu23liyancun45jiatingtai67zuo8910zhihua";
    private static final String TOKEN_HEADER = "Authorization";

    public  JwtParser jwtParser;
    public  JwtBuilder jwtBuilder;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(TOKEN);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtBuilder = Jwts.builder().signWith(key,SignatureAlgorithm.HS512);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public  String getToken(ServerHttpRequest request){
        String token = request.getHeaders().getFirst(TOKEN_HEADER);
        return StringUtils.hasText(token) ? token : null;

    }

    public  String createToken(User user){
        Map<String,Object> map = new HashMap<>(4);
        map.put(StringConstants.USERID,user.getId().toString());
        map.put(StringConstants.USERNAME,user.getUsername());
        return jwtBuilder.setId(IdUtil.randomUUID()).setClaims(map).compact();
    }

    public  String getValue(String token,String key){
        Claims body = jwtParser.parseClaimsJws(token).getBody();
        Object o = body.get(key);
        return ((String) o);
    }

    public  String getUserId(String token,String key){
        return getValue(token,key);
    }
    public  String getUsername(String token,String key){
        return getValue(token,key);
    }

    public  String getLevel(String token,String key){
        return getValue(token,key);
    }

    /**
     * 如果token有效期小于30分钟，就续期30分钟
     * @param token 请求token
     */
    public  void refreshToken(String token){
        String id = getValue(token, StringConstants.USERID);
        String key = CacheConstants.ONLINE_TOKEN_KEY + id;
        Long ttl = redisTemplate.getExpire(key);
        if (ttl < CacheConstants.TOKEN_RENEW_TTL){
            redisTemplate.expire(key,ttl + CacheConstants.TOKEN_RENEW_TIME, TimeUnit.SECONDS);
        }
    }

}
