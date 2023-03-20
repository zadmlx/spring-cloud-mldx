package individual.me.thread;


import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    private static final int corePoolSize = 6;
    private static final int maxPoolSize = 50;
    private static final int queueCapacity = 500;
    private static final int keepAliveSeconds = 300;

    @Bean
    @Primary
    public ThreadPoolTaskExecutor mldxExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);

        // 拒绝策略：谁调用谁负责
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadFactory(new MldxThreadFactory());
        taskExecutor.initialize();
        return taskExecutor;
    }
    @Bean
    public ScheduledExecutorService scheduledExecutorService(){
        return new ScheduledThreadPoolExecutor(corePoolSize,new MldxThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
