package individual.me.aspect;

import individual.me.annotation.Log;
import individual.me.constants.StringConstants;
import individual.me.service.LogService;
import individual.me.util.JwtService;
import individual.me.util.NetUtil;
import individual.me.util.RequestHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    private final LogService logService;
    private final JwtService jwtService;

    @Autowired
    public LogAspect(LogService logService,JwtService jwtService) {
        this.logService = logService;
        this.jwtService = jwtService;
    }

    ThreadLocal<Long> timeMillis = new ThreadLocal<>();
    @Pointcut("@annotation(individual.me.annotation.Log)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result;
        timeMillis.set(System.currentTimeMillis());

        //执行到这里，如果没有出现问题，就执行正常日志记录
        // 抛出异常的话，就由AfterThrowing 来进行处理
        result = joinPoint.proceed();
        individual.me.domain.Log logEntity = new individual.me.domain.Log("INFO",System.currentTimeMillis() - timeMillis.get());
        // 使用完之后需要及时删除
        timeMillis.remove();
        logService.insertLog(buildLog(logEntity,joinPoint));
        //AsyncLog.me().execute(LogFactory.recordLog(buildLog(logEntity, joinPoint)));
        return result;
    }

    @AfterThrowing(pointcut = "pointcut()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e){
        individual.me.domain.Log logEntity = new individual.me.domain.Log("ERROR",System.currentTimeMillis() - timeMillis.get());
        timeMillis.remove();
        individual.me.domain.Log log = buildLog(logEntity, (ProceedingJoinPoint) joinPoint);
        log.setException(e.getMessage());
        //AsyncLog.me().execute(LogFactory.recordLog(log));
        logService.insertLog(log);
    }

    // 创建log实体，
    private individual.me.domain.Log buildLog(individual.me.domain.Log logEntity,ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aspectLog = method.getAnnotation(Log.class);
        // 获取ip地址和浏览器信息
        HttpServletRequest httpServletRequest = RequestHolder.currentRequest();
        String ip = NetUtil.getIp(httpServletRequest);
        String browser = NetUtil.getBrowser(httpServletRequest);
        String username = jwtService.getUsername(httpServletRequest.getHeader(StringConstants.TOKEN_HEADER), StringConstants.USERNAME);
        //获取用户信息
        logEntity.setBrowser(browser);
        logEntity.setMethod(method.getName());
        logEntity.setIp(ip);
        logEntity.setUsername(username);
        logEntity.setDescription(aspectLog.value());
        logEntity.setCreateTime(LocalDateTime.now());
        return logEntity;
    }

}
