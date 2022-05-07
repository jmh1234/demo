package com.demo.rabbitmq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * RabbitMQConfig
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Configuration
public class RabbitMqConfig {
    @Bean("workListenerFactory")
    public SimpleRabbitListenerContainerFactory myFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        //自动ack,没有异常的情况下自动发送ack
        //auto  自动确认,默认是auto
        //MANUAL  手动确认
        //none  不确认，发完自动丢弃
        containerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //拒绝策略,true回到队列 false丢弃，默认是true
        containerFactory.setDefaultRequeueRejected(true);
        //默认的PrefetchCount是250，采用Round-robin dispatching，效率低
        //setPrefetchCount 为 1，即可启用fair 转发
        containerFactory.setPrefetchCount(1);
        return containerFactory;
    }
}
