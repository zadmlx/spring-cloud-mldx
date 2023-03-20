package individual.me.security;

import individual.me.domain.SecurityUser;
import individual.me.domain.Seller;
import individual.me.util.RequestHolder;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
@Component
public class JwtTokenService implements InitializingBean {

    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    private static final String username = "username";
    private static final String id = "id";
    private static final String shopId = "shopId";
    private static final String SECRET = "yejingyuqinghuangyuxixingchengyusihuiyusuiyejingyuqinghuangyuxixingchengyusihuiyusuiyejingyuqinghuangyuxixingchengyusihuiyusui";

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS256);
    }

    public String createToken(Authentication authentication){
        if (authentication.getPrincipal() instanceof SecurityUser securityUser){
            log.info("开始创建jwt token");
            Seller seller = securityUser.getSeller();
            HashMap<String,Object> identity = new HashMap<>(2);
            identity.put(username,authentication.getName());
            identity.put(id,seller.getId());
            return jwtBuilder.setClaims(identity).compact();
        }
        return null;
    }


    public Long getUserId(){
        String token = getToken();
        Claims body = getClaims(token);
        return body.get(id,Long.class);
    }

    public Authentication createAuthentication(String token){
        Claims body = getClaims(token);
        String name = body.get(username,String.class);
        return new UsernamePasswordAuthenticationToken(name,"*** PROTECTED ***", Collections.singleton(new SimpleGrantedAuthority("seller")));
    }


    private Claims getClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

    private String getToken(){
        HttpServletRequest request = RequestHolder.currentRequest();
        //String remoteAddr = request.getRemoteAddr();
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)){
            return token;
        }
        return null;
    }
}
