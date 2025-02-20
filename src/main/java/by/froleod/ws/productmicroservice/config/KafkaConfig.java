package by.froleod.ws.productmicroservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String PRODUCT_CREATED_TOPIC = "product-created-events-topic";

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name(PRODUCT_CREATED_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.in.sync.replicas", "2")) // мин кол-во серверов-реплик в синхронизации с сервером-лидером
                .build();
    }
}
