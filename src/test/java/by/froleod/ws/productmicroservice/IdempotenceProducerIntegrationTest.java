package by.froleod.ws.productmicroservice;


import by.froleod.ws.core.ProductCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IdempotenceProducerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @MockBean
    KafkaAdmin kafkaAdmin;

    @Test
    void testProducerConfig_whenIdempotenceEnabled_assertsIdempotentProperties() {
         //Arrange
        var producerFactory = kafkaTemplate.getProducerFactory();

        //Act
        var config = producerFactory.getConfigurationProperties();


        //Assert
        assertEquals("true", config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG));
        assertTrue("all".equalsIgnoreCase((String) config.get(ProducerConfig.ACKS_CONFIG)));
        if (config.containsKey(ProducerConfig.RETRIES_CONFIG)) {
            Object retries = config.get(ProducerConfig.RETRIES_CONFIG);
            if (retries instanceof Integer) {
                assertTrue((Integer) retries > 0);
            } else if (retries instanceof String) {
                assertTrue(Integer.parseInt((String) retries) > 0);
            }
        }
    }
}
