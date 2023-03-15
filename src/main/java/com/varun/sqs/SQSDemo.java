package com.varun.sqs;

import com.amazonaws.services.sqs.model.Message;

import java.util.Optional;
import java.util.function.Consumer;

public class SQSDemo {
    public static void main(String[] args) {
        SQSUtil sqsUtil = new SQSUtil("test-queue");
        sqsUtil.createQueue();

        sqsUtil.sendMessage("SQS Test");

        Consumer<Message> messageConsumer = (message) -> System.out.println(message.getBody());
        Optional<Message> receivedMessage = sqsUtil.receiveMessage();

        if (receivedMessage.isPresent()) {
            Message message = receivedMessage.get();
            sqsUtil.consumeAndDeleteMessage(message, messageConsumer);
        }
    }
}
