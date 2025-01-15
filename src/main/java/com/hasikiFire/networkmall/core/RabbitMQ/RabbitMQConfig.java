package com.hasikiFire.networkmall.core.RabbitMQ;

import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hasikiFire.networkmall.core.common.constant.RabbitMQConstants;

@Configuration
public class RabbitMQConfig {

  // 延迟队列（用户套餐队列）
  @Bean
  public Queue userPackageQueue() {
    return QueueBuilder.durable(RabbitMQConstants.USAGERECORD_QUEUE)
        .withArgument("x-dead-letter-exchange", "") // 使用默认交换机
        .withArgument("x-dead-letter-routing-key", RabbitMQConstants.USER_PACKAGE_DEAD_LETTER_QUEUE) // 死信路由键
        .build();
  }

  // 死信队列（用户套餐死信队列）
  @Bean
  public Queue userPackageDeadLetterQueue() {
    return QueueBuilder.durable(RabbitMQConstants.USER_PACKAGE_DEAD_LETTER_QUEUE) // 持久化队列
        .build();
  }
}