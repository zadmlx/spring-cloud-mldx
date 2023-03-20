package individual.me.aspect;

import individual.me.constants.StringConstants;
import individual.me.exception.AuthException;
import individual.me.util.RequestHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Order(0)
@Aspect
@Component
public class OriginAspect {


    @Pointcut("@annotation(individual.me.annotation.Gateway)")
    private void pointcut(){};

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.currentRequest();
        String origin = request.getHeader(StringConstants.ORIGIN);
        if (!StringUtils.hasText(origin) && !origin.equals(StringConstants.GATEWAY_ORIGIN)){
            throw new AuthException("请通过网关访问哦!");
        }
        String username = request.getHeader(StringConstants.USERNAME);
        String userid = request.getHeader(StringConstants.USERID);
        String level = request.getHeader(StringConstants.USER_LEVEL);
        if (!StringUtils.hasText(username) || !StringUtils.hasText(userid) || !StringUtils.hasText(level)){
            throw new AuthException("不要非法访问哦!");
        }
        return joinPoint.proceed();
    }

}
