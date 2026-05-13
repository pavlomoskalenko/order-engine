/*
package click.pavlomoskalenko.order.messaging.listener;

import click.pavlomoskalenko.order.messaging.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventListener {

    @KafkaListener(topics = "user.events.created", groupId = "order-service")
    public void onUserCreated(@Payload UserCreatedEvent userCreatedEvent, Acknowledgment ack) {
        log.info("Received UserCreatedEvent for userId={}", userCreatedEvent.getId());
        ack.acknowledge();
    }
}
*/
