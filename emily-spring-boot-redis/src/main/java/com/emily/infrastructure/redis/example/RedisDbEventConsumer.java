package com.emily.infrastructure.redis.example;

import io.lettuce.core.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redis数据库事件消费，参考：https://my.oschina.net/go4it/blog/2049651
 *
 * @author Emily
 * @since 2021/11/06
 */
public class RedisDbEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RedisDbEventConsumer.class);

    private EventBus eventBus;

    public RedisDbEventConsumer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init() {
        eventBus.get().subscribe(event -> {
            logger.info("event:{}", event);
        });
    }
}
