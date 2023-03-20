package individual.me.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 定义消息路由失败时的策略。true，则调用ReturnCallback；false：则直接丢弃消息
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback((rm)->{
            log.info("消息投递到交换机，但没有路由到队列");
            String replyText = rm.getReplyText();
            String exchange = rm.getExchange();
            int replyCode = rm.getReplyCode();
            String routingKey = rm.getRoutingKey();
            log.error("消息详细信息   交换机：{}，路由key：{}，响应码：{}，失败原因：{}",exchange,routingKey,replyCode,replyText);
        });
    }
}
