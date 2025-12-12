package com.example.bookingservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingPublisher {
    private final RabbitTemplate rabbit;
    public BookingPublisher(RabbitTemplate rabbit) { this.rabbit = rabbit; }

    public void publishBookingCreated(BookingMessage msg) {
        rabbit.convertAndSend("events.exchange", "booking.created", msg);
        System.out.println("[BookingService] Published booking.created for bookingId=" + msg.getId());
    }
}
