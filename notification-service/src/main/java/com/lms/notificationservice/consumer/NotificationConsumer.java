
package com.lms.notificationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.lms.notificationservice.dto.NotificationMessage;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = "leave.notifications")
    public void consume(NotificationMessage notification) {

        System.out.println("===================================");

        System.out.println("NOTIFICATION RECEIVED");

        System.out.println("Type : " + notification.getType());

        System.out.println("Message : " + notification.getMessage());

        System.out.println("===================================");
    }
}