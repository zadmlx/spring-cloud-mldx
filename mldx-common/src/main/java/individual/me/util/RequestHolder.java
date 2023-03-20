package individual.me.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestHolder {

    public static HttpServletRequest currentRequest(){
        //多线程异步操作子线程无法获取主线程request信息,需要设置子线程共享
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(),true);
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
