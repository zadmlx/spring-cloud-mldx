package individual.me.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 在java spring cloud项目中，我们常常会在子模块中创建公共类库，作为驱动包。
 * 那么在另外一个子模块中，需要加载配置文件的时候，往往Spring Boot 自动扫描包的时候，只会扫描自己模块下的类。
 * 因此使用spring.factories来进行自动装配
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserDetailsInterceptor()).addPathPatterns("/**").excludePathPatterns("/auth/**");
    }
}
