package individual.me.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import java.util.Map;
import java.util.function.Consumer;


@FunctionalInterface
public interface RetryCallBack {

    Logger log = LoggerFactory.getLogger(RetryCallBack.class);
    Integer maxRetry = 3;
    String retryKey = "retry-count";

    void callback();

    /**
     * @param message 消息
     * @param channel 信道
     */
    static void handleMQException(Message message, Channel channel, Runnable runnable){
        Map<String, Object> messageHeaders = message.getMessageProperties().getHeaders();
        int retry = messageHeaders.containsKey(retryKey) ? ((Integer) messageHeaders.get(retryKey)) : 0;
        if (retry ++ < maxRetry){
            log.info("消息队列处理订单异常，正在尝试重试，这是第{}次重试",retry);

        }
        else {
            runnable.run();
        }

    }

}
