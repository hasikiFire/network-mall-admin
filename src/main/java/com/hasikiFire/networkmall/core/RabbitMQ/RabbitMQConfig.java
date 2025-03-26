package com.hasikiFire.networkmall.core.RabbitMQ;

import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hasikiFire.networkmall.core.common.constant.RabbitMQConstants;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMQConfig {

  // 延迟队列（用户套餐队列）
  @Bean
  public Queue userPackageQueue() {
    return QueueBuilder.durable(RabbitMQConstants.USAGERECORD_QUEUE)
        .withArgument("x-dead-letter-exchange", "") // 使用默认交换机
        .withArgument("x-dead-letter-routing-key", RabbitMQConstants.USER_PACKAGE_DEAD_LETTER_QUEUE) // 死信路由键
        .build();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

    // 设置消息转换器（如 JSON）
    // rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

    // 启用发布确认（必须在配置中开启）
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
      if (!ack) {
        log.info("消息未到达MQ: " + cause);
        // 这里可以记录日志或重发逻辑
      } else {
        log.info("消息已确认到达MQ");
      }
    });

    return rabbitTemplate;
  }

  // 死信队列（用户套餐死信队列）
  @Bean
  public Queue userPackageDeadLetterQueue() {
    return QueueBuilder.durable(RabbitMQConstants.USER_PACKAGE_DEAD_LETTER_QUEUE) // 持久化队列
        .build();
  }
}