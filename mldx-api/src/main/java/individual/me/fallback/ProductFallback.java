package individual.me.fallback;

import individual.me.client.CloudProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class ProductFallback implements FallbackFactory<CloudProductService> {
    @Override
    public CloudProductService create(Throwable cause) {
        log.info("产品模块出现异常:{}",cause.getMessage());
        return null;
    }
}
