package com.emily.infrastructure.rabbitmq.factory;

import com.emily.infrastructure.common.utils.StrUtils;
import com.emily.infrastructure.core.context.ioc.IOCContext;
import com.emily.infrastructure.rabbitmq.RabbitMqProperties;
import com.emily.infrastructure.rabbitmq.common.RabbitMqInfo;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @Description :  RabbitMq消息中间件工厂类
 * @Author :  Emily
 * @CreateDate :  Created in 2022/6/6 11:31 上午
 */
public class RabbitMqFactory {
    /**
     * 获取RabbitTemplate对象
     */
    public static RabbitTemplate getRabbitTemplate() {
        return getRabbitTemplate(null);
    }

    /**
     * 获取RabbitTemplate对象
     *
     * @param key 消息中间件配置标识
     * @return
     */
    public static RabbitTemplate getRabbitTemplate(String key) {
        String beanName;
        if (Objects.isNull(key) || StringUtils.equals(key, IOCContext.getBean(RabbitMqProperties.class).getDefaultConfig())) {
            beanName = StrUtils.toLowerFirstCase(RabbitMqInfo.RABBIT_TEMPLATE);
        } else {
            beanName = MessageFormat.format("{0}{1}", key, RabbitMqInfo.RABBIT_TEMPLATE);
        }
        if (!IOCContext.containsBean(beanName)) {
            throw new IllegalArgumentException(MessageFormat.format("RabbitMQ消息中间件标识{0}不存在", key));
        }
        return IOCContext.getBean(beanName, RabbitTemplate.class);
    }

    /**
     * 获取AmqpAdmin对象，用于动态的初始化队里、交换机、路由绑定Key
     * AmqpAdmin amqpAdmin = IOCContext.getBean("testAmqpAdmin", AmqpAdmin.class);
     * TopicExchange exchange = ExchangeBuilder.topicExchange("exchange").build();
     * Queue queue = QueueBuilder.durable("topic.emily.queue").build();
     * Binding binding = BindingBuilder.bind(queue).to(exchange).with("topic.#");
     * amqpAdmin.declareExchange(exchange);
     * amqpAdmin.declareQueue(queue);
     * amqpAdmin.declareBinding(binding);
     */
    public static AmqpAdmin getAmqpAdmin() {
        return getAmqpAdmin(null);
    }

    /**
     * 获取AmqpAdmin对象，用于动态的初始化队里、交换机、路由绑定Key
     * AmqpAdmin amqpAdmin = IOCContext.getBean("testAmqpAdmin", AmqpAdmin.class);
     * TopicExchange exchange = ExchangeBuilder.topicExchange("exchange").build();
     * Queue queue = QueueBuilder.durable("topic.emily.queue").build();
     * Binding binding = BindingBuilder.bind(queue).to(exchange).with("topic.#");
     * amqpAdmin.declareExchange(exchange);
     * amqpAdmin.declareQueue(queue);
     * amqpAdmin.declareBinding(binding);
     *
     * @param key 消息中间件配置标识
     * @return
     */
    public static AmqpAdmin getAmqpAdmin(String key) {
        if (Objects.isNull(key)) {
            key = IOCContext.getBean(RabbitMqProperties.class).getDefaultConfig();
        }
        //AmqpAdmin实例bean名称
        String beanName = MessageFormat.format("{0}{1}", key, RabbitMqInfo.AMQP_ADMIN);
        if (!IOCContext.containsBean(beanName)) {
            throw new IllegalArgumentException(MessageFormat.format("RabbitMQ消息中间件标识{0}不存在", key));
        }
        return IOCContext.getBean(beanName, AmqpAdmin.class);
    }

    /**
     * 获取RabbitMessagingTemplate实例对象
     */
    public static RabbitMessagingTemplate getRabbitMessagingTemplate() {
        return getRabbitMessagingTemplate(null);
    }

    /**
     * 获取RabbitMessagingTemplate实例对象
     *
     * @param key 消息中间件标识
     * @return
     */
    public static RabbitMessagingTemplate getRabbitMessagingTemplate(String key) {
        if (Objects.isNull(key)) {
            key = IOCContext.getBean(RabbitMqProperties.class).getDefaultConfig();
        }
        //AmqpAdmin实例bean名称
        String beanName = MessageFormat.format("{0}{1}", key, RabbitMqInfo.RABBIT_MESSAGING_TEMPLATE);
        if (!IOCContext.containsBean(beanName)) {
            throw new IllegalArgumentException(MessageFormat.format("RabbitMQ消息中间件标识{0}不存在", key));
        }
        return IOCContext.getBean(beanName, RabbitMessagingTemplate.class);
    }

    /**
     * 声明RabbitMQ消息中间件队列、交换器、绑定，并初始化
     *
     * @param queue    队列
     * @param exchange 交换器
     * @param binding  绑定
     */
    public static void declare(Queue queue, Exchange exchange, Binding binding) {
        declare(null, queue, exchange, binding);
    }

    /**
     * 声明RabbitMQ消息中间件队列、交换器、绑定，并初始化
     *
     * @param key      中间件配置标识
     * @param queue    队列
     * @param exchange 交换器
     * @param binding  绑定
     */
    public static void declare(String key, Queue queue, Exchange exchange, Binding binding) {
        if (Objects.isNull(key)) {
            key = IOCContext.getBean(RabbitMqProperties.class).getDefaultConfig();
        }
        AmqpAdmin amqpAdmin = getAmqpAdmin(key);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }

    /**
     * 获取RabbitMQ消息中间件通道
     *
     * @param transactional true-支持事务，false-不支持事务
     * @return
     */
    public static Channel getChannel(boolean transactional) {
        return getChannel(null, transactional);
    }

    /**
     * 获取RabbitMQ消息中间件通道
     *
     * @param key           中间件配置标识
     * @param transactional true-支持事务，false-不支持事务
     * @return
     */
    public static Channel getChannel(String key, boolean transactional) {
        ConnectionFactory connectionFactory = getRabbitTemplate(key).getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        return connection.createChannel(transactional);
    }
}
