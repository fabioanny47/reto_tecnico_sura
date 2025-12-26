package co.com.sura.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "example-queue")
    public void receive(String message) {
        System.out.println("Received message: " + message);
    }
}