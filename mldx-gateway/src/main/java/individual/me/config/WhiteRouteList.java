package individual.me.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security.white")
public class WhiteRouteList {

    private List<String> white = new ArrayList<>();

    public List<String> getWhite() {
        return white;
    }

    public void setWhite(List<String> white) {
        this.white = white;
    }


    /**
     * 判断请求路径是否需要携带token访问
     * @param path 请求路径
     * @return 是否需要携带token
     */
    public boolean isIgnored(String path){
        if (!StringUtils.hasText(path)){
            return false;
        }
        return white.stream().anyMatch((w) -> {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            return (antPathMatcher.match(w, path));
        });

    }


}
