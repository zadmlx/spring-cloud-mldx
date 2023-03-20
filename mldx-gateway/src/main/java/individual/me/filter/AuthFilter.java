package individual.me.filter;

import cn.hutool.json.JSONUtil;
import individual.me.config.WhiteRouteList;
import individual.me.constants.CacheConstants;
import individual.me.constants.StringConstants;
import individual.me.domain.R;
import individual.me.util.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.yaml.snakeyaml.events.Event;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Order(1)
@Slf4j
@Component
public class AuthFilter implements GlobalFilter {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private WhiteRouteList whiteRouteList;

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String path = request.getURI().getPath();
        if (whiteRouteList.isIgnored(path)){
            return chain.filter(exchange);
        }

        log.info("当前请求：{},非白名单不可访问必须携带token", request.getURI());
        String token = jwtService.getToken(request);
        log.info("当前请求token:{}",token);
        if (!StringUtils.hasText(token)){
            return unauthorized(exchange, "请求token为空");
        }
        String userId = jwtService.getUserId(token, StringConstants.USERID);
        Object o = redisTemplate.opsForValue().get(CacheConstants.ONLINE_TOKEN_KEY + userId);
        if (ObjectUtils.isEmpty(o) || !o.equals(token)){
            return unauthorized(exchange,"token已过期或不存在");
        }
        String username = jwtService.getUsername(token, StringConstants.USERNAME);
        log.info("根据token获取到用户id:{}，用户名:{}",userId,username);

        if (!StringUtils.hasText(username)){
            return unauthorized(exchange,"请求用户信息不完整,请尝试重新登录");
        }
        mutate.header(StringConstants.USERID,userId);
        mutate.header(StringConstants.USERNAME,username);
        mutate.header("origin","gateway");

        jwtService.refreshToken(token);
        log.info("验证完毕，开始放行");
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        R r = R.fail(HttpStatus.UNAUTHORIZED.value(), message);
        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(r).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
