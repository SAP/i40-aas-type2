package com.sap.i40aas.datamanager.kafkaConnector;

import com.sap.i40aas.datamanager.kafkaConnector.serializers.KafkaSubmodelDeserializer;
import identifiables.Submodel;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Profile("dev")

public class KafkaConsumerConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  public ConsumerFactory<String, String> consumerFactory(String groupId) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> fooKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory("foo"));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> barKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory("bar"));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> headersKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory("headers"));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> partitionsKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory("partitions"));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory("filter"));
    factory.setRecordFilterStrategy(record -> record.value()
      .contains("World"));
    return factory;
  }

  public ConsumerFactory<String, com.sap.i40aas.datamanager.kafkaConnector.Greeting> greetingConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "greeting");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(com.sap.i40aas.datamanager.kafkaConnector.Greeting.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, com.sap.i40aas.datamanager.kafkaConnector.Greeting> greetingKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, com.sap.i40aas.datamanager.kafkaConnector.Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(greetingConsumerFactory());
    return factory;
  }

  public ConsumerFactory<String, Submodel> submodelConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "submodel");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaSubmodelDeserializer());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Submodel> submodelKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Submodel> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(submodelConsumerFactory());
    return factory;
  }

}
