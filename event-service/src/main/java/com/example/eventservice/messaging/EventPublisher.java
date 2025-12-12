package com.example.eventservice.messaging;

import com.example.eventservice.model.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private final RabbitTemplate rabbit;
    public EventPublisher(RabbitTemplate rabbit) { this.rabbit = rabbit; }

    public void publishEventCreated(Event e) {
        rabbit.convertAndSend("events.exchange", "event.created", new EventMessage(e.getId(), e.getName(), e.getCapacity()));
        System.out.println("[EventService] Published event.created for id=" + e.getId());
    }
}
