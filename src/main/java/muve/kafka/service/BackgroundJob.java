package muve.kafka.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import muve.kafka.service.utils.MUVEConsumer;

@Component
public class BackgroundJob {
	private static Logger logger = LogManager.getLogger(BackgroundJob.class);
	@Autowired
	TaskExecutor executor;

	@Autowired
	MUVEConsumer runner;

	@PostConstruct
	public void start() {
		logger.info("Starting KafkaListenerRunner in the background!");
		executor.execute(runner);
	}
}
