package com.varun.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * A utility class to play with AWS SQS. It is instantiated by providing a {@link queueName}.
 */
public class SQSUtil {

    private final String queueName;
    private final AmazonSQS sqs;
    private final Logger logger;
    private String queueUrl;

    public SQSUtil(String queueName) {
        this.queueName = queueName;
        this.sqs = AmazonSQSClientBuilder.defaultClient();
        this.logger = Logger.getLogger(SQSUtil.class.getName());
    }

    /**
     * Creates a SQS queue with queue name as {@link queueName}. It first checks if queue already exists and goes on to
     * create the queue if it doesn't exist.
     */
    public void createQueue() {
        try {
            getQueueUrl();
            this.logger.info(String.format("Queue with name %s already exists", this.queueName));
            return;
        } catch (QueueDoesNotExistException e) {
            this.logger.info(String.format("Queue with name %s doesn't exists. Creating new queue", this.queueName));
        }
        try {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(this.queueName)
                    .addAttributesEntry("MessageRetentionPeriod", "86400");
            sqs.createQueue(createQueueRequest);
            this.logger.info(String.format("Queue with name %s created successfully", this.queueName));
        } catch (QueueDeletedRecentlyException e) {
            this.logger.warning(String.format("Queue with name %s was recently deleted", this.queueName));
        }
    }

    /**
     * Lists down the queue urls of all queues associated with the AWS account
     */
    public void listQueues() {
        sqs.listQueues()
                .getQueueUrls()
                .forEach(System.out::println);
    }

    /**
     * Sends a message with a message body to the queue {@link queueName}
     *
     * @param messageBody content of the message
     */
    public void sendMessage(String messageBody) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(getQueueUrl())
                .withMessageBody(messageBody);
        sqs.sendMessage(sendMessageRequest);
    }

    /**
     * Receives 1 message from the queue {@link queueName}
     *
     * @return Message if present or else an Optional type
     */
    public Optional<Message> receiveMessage() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(getQueueUrl())
                .withMaxNumberOfMessages(1);
        ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
        return receiveMessageResult.getMessages()
                .stream()
                .limit(1)
                .findFirst();
    }

    /**
     * Performs the consumption of message and then deletes the message from the queue {@link queueName}
     *
     * @param message         SQS Message
     * @param messageConsumer Consumer to consume the message
     */
    public void consumeAndDeleteMessage(Message message, Consumer<Message> messageConsumer) {
        messageConsumer.accept(message);
        sqs.deleteMessage(getQueueUrl(), message.getReceiptHandle());
    }

    private String getQueueUrl() throws QueueDoesNotExistException {
        if (this.queueUrl != null) {
            return this.queueUrl;
        }
        this.queueUrl = sqs.getQueueUrl(this.queueName).getQueueUrl();
        return this.queueUrl;
    }
}
