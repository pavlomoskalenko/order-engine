/*
package click.pavlomoskalenko.auth.messaging.publisher;

import click.pavlomoskalenko.auth.messaging.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "user.events.created";

    public void publishUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
}
*/
