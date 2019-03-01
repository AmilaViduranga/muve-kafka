package muve.kafka.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import muve.kafka.service.service.MUVEConsumer;

@Component
public class BackgroundJob {
	 @Autowired
	    TaskExecutor executor;

	    @Autowired
	    MUVEConsumer runner;

	    @PostConstruct
	    public void start() {
	        System.out.println("Starting KafkaListenerRunner in the background!");
	        executor.execute(runner);
	    }
}
