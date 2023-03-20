package individual.me.config.web;

import individual.me.config.SecurityContextHolder;
import individual.me.constants.StringConstants;

import individual.me.util.JwtService;
import individual.me.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserDetailsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("当前请求路径:{}", request.getRequestURI());
        String userId = request.getHeader(StringConstants.USERID);
        String username = request.getHeader(StringConstants.USERNAME);
        SecurityContextHolder.setUserId(userId);
        SecurityContextHolder.setUsername(username);
        log.info("拦截到用户请求信息，username:{},userId:{}",username,userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求完毕之后，必须把线程变量清空
        SecurityContextHolder.remove();
    }
}
