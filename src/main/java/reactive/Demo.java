package reactive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.SubmissionPublisher;

public class Demo {

    public static void main(String[] args) throws InterruptedException, IOException {
        SimpleSubscriber subscriber = new SimpleSubscriber();
        SubmissionPublisher<String> publisher = new SubmissionPublisher<String>();
        publisher.subscribe(subscriber);

        System.out.println(publisher.getSubscribers());
        publisher.submit("Hello Devoxx");
        publisher.submit("Hello Devoxx");

        publisher.close();

        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
