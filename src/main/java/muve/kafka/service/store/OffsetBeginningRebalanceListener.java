package muve.kafka.service.store;

import java.util.Collection;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class OffsetBeginningRebalanceListener implements ConsumerRebalanceListener{
	private final KafkaConsumer<String, String> consumer;
    private boolean resetted;
    private String partition;

    public OffsetBeginningRebalanceListener(KafkaConsumer<String, String> consumer, String partition) {
        this.consumer = consumer;
        this.partition = partition;
    }


	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		// TODO Auto-generated method stub
		System.out.println("Revoked from");

        for (TopicPartition partition : partitions) {
            System.out.println("collection = [" + partition + "]");
        }	
	}

	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		// TODO Auto-generated method stub
		System.out.print("Assigned to: ");

        for (TopicPartition partition : partitions) {
            System.out.print(partition + " ");
        }

        System.out.println();

        if (!resetted) {
            consumer.seekToBeginning(Collections.singletonList(new TopicPartition(partition,0)));
            resetted = true;
        }
	}
	
}
