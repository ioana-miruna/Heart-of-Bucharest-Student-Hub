package com.example.userservice.messaging;

import com.example.userservice.loyalty.LoyaltyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookingListener {

    private final LoyaltyService loyaltyService;

    public BookingListener(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @RabbitListener(queues = "booking.created.queue")
    public void receiveBookingCreated(BookingMessage msg) {
        System.out.println("[UserService] Received booking.created event");
        loyaltyService.awardPointsForBooking(msg.getCustomerId());
    }
}

