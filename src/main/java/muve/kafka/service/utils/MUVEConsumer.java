package muve.kafka.service.utils;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import muve.kafka.service.model.BookingModel;
import muve.kafka.service.store.BookingStore;
import muve.kafka.service.store.OffsetBeginningRebalanceListener;

@Service
public class MUVEConsumer implements Runnable {
	public static String TOPIC = "muve_bookings_test";
	
	@Autowired
	private BookingStore store;
	
	private Gson gson = new Gson();
	
	public Map<String, Object> config() {
		
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(GROUP_ID_CONFIG, "kasse");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }
	
	public void getAllBookings() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config());
        consumer.subscribe(Arrays.asList(TOPIC), new OffsetBeginningRebalanceListener(consumer, TOPIC));
        JsonParser parser = new JsonParser();
        try {
            System.out.println("Starting Listener!");
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000000);
                if (records.isEmpty())
                    continue;

                for (ConsumerRecord<String, String> cr : records) {
                	if(cr != null) {
                		JsonObject json = parser.parse(cr.value()).getAsJsonObject();
                        String action = json.getAsJsonPrimitive("action").getAsString();
                        JsonObject object = json.getAsJsonObject("object");
                        BookingModel booking = gson.fromJson(object, BookingModel.class);
                    	booking.setOffsetId(cr.offset());
                    	booking.setAction(action);
                    	System.out.println("*******************");
                    	System.out.println(cr.offset());
                    	System.out.println("*******************");
                        if(action.equalsIgnoreCase("create")) {
                        	booking.setEventId(Long.toString(cr.timestamp()));
                            store.save(booking);
                            continue;
                        }
                        if(action.equalsIgnoreCase("delete")) {
                        	store.delete(Long.toString(cr.offset()));
                        	continue;
                        }
                        store.save(booking);
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
	}

	public void run() {
		// TODO Auto-generated method stub
		this.getAllBookings();
	}
}
