package app;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PubSubController {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public void publishMessage(List<LinkedHashMap<String, Object>> messages) {
        ProjectTopicName topicName = ProjectTopicName.of("test-mock-service", "Generated");
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();


            for (final Map<String, Object> message : messages) {
                ByteString data = ByteString.copyFromUtf8(linkedHashMapToJSON(message));
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> future = publisher.publish(pubsubMessage);

                // Add an asynchronous callback to handle success / failure
                ApiFutures.addCallback(future, futureCallback(message));
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                try {
                    publisher.shutdown();
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }
        }
    }

    private String linkedHashMapToJSON(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> property: map.entrySet()) {
            sb.append("\"" + property.getKey() + "\": \"" + property.getValue() + "\",");
        }
        sb.append("}");
        return sb.toString().replace(",}", "}");

    }

    private ApiFutureCallback futureCallback(Map<String, Object> message) {
        return new ApiFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {
                    ApiException apiException = ((ApiException) throwable);
                    // details on the API exception
                    System.out.println(apiException.getStatusCode().getCode());
                    System.out.println(apiException.isRetryable());
                }
                System.out.println("Error publishing message : " + message);
            }

            @Override
            public void onSuccess(Object o) {
                System.out.println(o.toString());
            }
        };
    }
}
