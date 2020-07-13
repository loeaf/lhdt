package lhdt.service;

import lhdt.domain.QueueMessage;

public interface AMQPPublishService {

	/**
	 * message 전송
	 * @param queueMessage
	 */
	public void send(QueueMessage queueMessage);
}
