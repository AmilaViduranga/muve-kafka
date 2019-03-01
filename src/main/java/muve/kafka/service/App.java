package muve.kafka.service;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import muve.kafka.service.service.MUVEConsumer;
import muve.kafka.service.store.BookingStore;


/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	@Autowired
	public Producer<String,String> producer;
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
	public Producer<String,String> producer() {
		Properties props = new Properties();
		props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ACKS_CONFIG, "all");
		props.put(RETRIES_CONFIG, 0);
		props.put(BATCH_SIZE_CONFIG, 32000);
		props.put(LINGER_MS_CONFIG, 100);
		props.put(BUFFER_MEMORY_CONFIG, 33554432);
		props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<String,String>(props);
	}
    
    @Bean
	public BookingStore articlesStore() {
		return new BookingStore();
	}
    
    @Bean
    @Primary
	public MUVEConsumer runner() {
		return new MUVEConsumer();
	}
    
    @Bean
	public TaskExecutor executor() {
		return new ThreadPoolTaskExecutor();
	}
}
