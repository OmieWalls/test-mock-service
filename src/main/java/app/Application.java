package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public PubSubInboundChannelAdapter messageChannelAdapter(
//            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
//            PubSubOperations pubSubTemplate) {
//        PubSubInboundChannelAdapter adapter =
//                new PubSubInboundChannelAdapter(pubSubTemplate, "testSubscription");
//        adapter.setOutputChannel(inputChannel);
//        adapter.setAckMode(AckMode.MANUAL);
//
//        return adapter;
//    }

}