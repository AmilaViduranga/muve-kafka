package muve.kafka.service.utils;

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
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import muve.kafka.service.model.BookingModel;

@Service
public class MUVEProducer {
	static Gson gson = new Gson();
	static String TOPICNAME = "muve_bookings_test";
	
	private Producer<String, String> createPRoducer() {
		Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 0);
        props.put(BATCH_SIZE_CONFIG, 16000);
        props.put(LINGER_MS_CONFIG, 100);
        props.put(BUFFER_MEMORY_CONFIG, 33554432);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer(props);
        return producer;
	}
	
	public void sendMessages(BookingModel book) {
		Producer<String, String> producer = this.createPRoducer();
        producer.send(new ProducerRecord<String, String>(TOPICNAME, book.getId(), createWrapper(book)));
        producer.close();
	}
	
	public void deleteMessage(String id) {
		Producer<String, String> producer = this.createPRoducer();
		producer.send(new ProducerRecord<String, String>(TOPICNAME, id, deleteWrapper(id)));
	}
	
	public void updateMessage(String id, BookingModel book) {
		Producer<String, String> producer = this.createPRoducer();
		producer.send(new ProducerRecord<String, String>(TOPICNAME, id, putWrapper(book)));
	}
	
	private static String createWrapper(BookingModel book) {
	        JsonObject cmd = new JsonObject();
	        cmd.addProperty("action", "create");
	        cmd.add("object", gson.toJsonTree(book));
	        return cmd.toString();
	}
	
	private String deleteWrapper( String id) {
        JsonObject cmd = new JsonObject();
        cmd.addProperty("action", "delete");
        return cmd.toString();
    }

    private String putWrapper(BookingModel book) {
        JsonObject cmd = new JsonObject();
        cmd.addProperty("action", "update");
        cmd.add("object", gson.toJsonTree(book));
        return cmd.toString();
    }	
}
