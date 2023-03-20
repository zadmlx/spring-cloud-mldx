package individual.me.mq;

import individual.me.constants.SellerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * date: 2022年10月14日20:02:41
 * 本来想模仿瑞吉外卖写，但是跟踪源码发现可以实现MessageRecover接口，rabbit监听器失败重试达到最大后会进行回调
 * 在handleRetryExhausted()中会调用该实现类的recover方法，但是调用这个方法的的并不是我们在rabbitTemplate中自定义注入的
 * recoverCallBack对象,而是RetryOperationsInterceptor，这个对象最终会调用MessageRecoverer::recover
 *
 *
 * 调用链路，创建MessageRecoverer对象简称mr，注入RabbitAnnotationDrivenConfiguration，
 * 调用configure方法，创建RetryInterceptorBuilder简称rib对象，将mr填充到rib中，调用build方法构建
 * 两种RetryOperationsInterceptor对象，有状态或者无状态的roi对象，创建之后添加到AbstractRabbitListenerContainerFactory中，
 */

@Slf4j
@Component
public class MessageRecoverImpl implements MessageRecoverer, InitializingBean {

    // 由于需要发送异常消息到指定队列，需要创建RepublishMessageRecoverer，构造函数里面需要amqpTemplate，因此注入该对象
    @Autowired
    private AmqpTemplate amqpTemplate;


    // 需求，完成个人业务逻辑最后，将异常消息转发到指定队列进行处理   ？    但是是不是可以直接继承RepublishMessageRecoverer呢？
    private RepublishMessageRecoverer republishMessageRecoverer;

    @Override
    public void recover(Message message, Throwable cause) {
        log.info("重试次数耗尽，通知管理员小哥哥");
        log.info("message:{}",message.getBody());

        // TODO 个人业务逻辑。。。比如记录日志，但是记录日志是不是可以在监听到异常队列之后处理呢？或者直接不需要投递到处理异常的交换机

        // 个人业务逻辑处理完毕之后在添加到指定处理异常的队列
        //      headers.put(X_EXCEPTION_STACKTRACE, stackTraceAsString);
        //		headers.put(X_EXCEPTION_MESSAGE, exceptionMessage);
        //		headers.put(X_ORIGINAL_EXCHANGE, messageProperties.getReceivedExchange());
        //		headers.put(X_ORIGINAL_ROUTING_KEY, messageProperties.getReceivedRoutingKey());
        republishMessageRecoverer.recover(message,cause);
    }

    @Override
    public void afterPropertiesSet() {
        republishMessageRecoverer = new RepublishMessageRecoverer(
                amqpTemplate, SellerConstants.SELLER_UNHANDLED_REFUND_ORDER_EXCHANGE, SellerConstants.SELLER_UNHANDLED_REFUND_ORDER_KEY);
    }

}
