package com.lms.notificationservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.lms.notificationservice.dto.NotificationMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(NotificationMessage msg) {

        rabbitTemplate.convertAndSend("leave.notifications", msg);
    }
}