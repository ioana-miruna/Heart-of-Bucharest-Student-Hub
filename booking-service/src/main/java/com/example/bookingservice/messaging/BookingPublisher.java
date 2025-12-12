package com.example.bookingservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingPublisher {

    private final RabbitTemplate rabbitTemplate;

    public BookingPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookingCreated(BookingMessage msg) {
        rabbitTemplate.convertAndSend(
                "events.exchange",
                "booking.created",
                msg
        );
        System.out.println("[BookingService] Published booking.created event");
    }
}

