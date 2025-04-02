package by.froleod.ws.productmicroservice.service;

import by.froleod.ws.core.ProductCreatedEvent;
import by.froleod.ws.productmicroservice.service.dto.CreateProductDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static by.froleod.ws.productmicroservice.config.KafkaConfig.PRODUCT_CREATED_TOPIC;

@Service
public class ProductServiceImpl implements ProductService {

    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDto product) throws ExecutionException, InterruptedException {
        //TODO: save to db
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                product.getTitle(),
                product.getPrice(),
                product.getQuantity());

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(PRODUCT_CREATED_TOPIC,
                productId,
                productCreatedEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String,ProductCreatedEvent> result = kafkaTemplate.send(record).get();

        LOGGER.info("topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("offset: {}", result.getRecordMetadata().offset());



//        CompletableFuture<SendResult<String,ProductCreatedEvent>> future =
//                kafkaTemplate.send(PRODUCT_CREATED_TOPIC, productId, productCreatedEvent); // асинхронно
//
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                LOGGER.error("Failed to send message: " + exception.getMessage());
//            } else {
//                LOGGER.info("Message sent to the topic: " + PRODUCT_CREATED_TOPIC + ": " + result.getRecordMetadata());
//            }
//        });

        LOGGER.info("Return: {}", productId);


        return productId;
    }
}
