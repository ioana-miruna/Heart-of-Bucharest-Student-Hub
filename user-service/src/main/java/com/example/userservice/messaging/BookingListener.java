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

    // consumes messages published by booking-service
    @RabbitListener(queues = "booking.created.queue")
    public void handleBookingCreated(BookingMessage msg) {
        System.out.println("[UserService] Received booking.created for customerId=" + msg.getCustomerId()
                + " eventId=" + msg.getEventId());
        if (msg.getCustomerId() != null) {
            loyaltyService.awardPointsForBooking(msg.getCustomerId());
        }
    }
}
