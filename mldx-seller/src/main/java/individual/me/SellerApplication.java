package individual.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients("individual.me")
@SpringBootApplication
public class SellerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellerApplication.class,args);
    }
}
