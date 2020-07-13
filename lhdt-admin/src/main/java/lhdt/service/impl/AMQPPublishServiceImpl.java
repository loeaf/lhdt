package lhdt.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import lhdt.config.PropertiesConfig;
import lhdt.domain.QueueMessage;
import lhdt.service.AMQPPublishService;

/**
 * @author Cheon JeongDae
 *
 */
@Slf4j
@Service
public class AMQPPublishServiceImpl implements AMQPPublishService {

	@Autowired
	private PropertiesConfig propertiesConfig;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Transactional
	public void send(QueueMessage queueMessage) {
		log.info("@@ Publish send message >>> {}", queueMessage);
		rabbitTemplate.convertAndSend(propertiesConfig.getQueueName(), queueMessage);
	}
}
