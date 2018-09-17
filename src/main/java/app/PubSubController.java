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
import util.UtilController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PubSubController {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public void publishGeneratedData(List<LinkedHashMap<String, Object>> messages) {

        ProjectTopicName topicName = ProjectTopicName.of(System.getenv("GCP_PROJECT_ID"), "Generated");
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            for (final Map<String, Object> message : messages) {
                var jsonMap = UtilController.linkedHashMapToJSON(message);
                publishData(jsonMap, publisher);
            }

        } catch (IOException e) {
            log.debug("Pub/Sub Publishing Error: " + e.getMessage());

        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                try {
                    publisher.shutdown();
                } catch (Exception e) {
                    log.debug("Pub/Sub Shutdown Error: " + e.getMessage());
                }
            }
        }
    }

    private void publishData(String message, Publisher publisher) {

        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        // Once published, returns a server-assigned message id (unique within the topic)
        ApiFuture<String> future = publisher.publish(pubsubMessage);

        // Add an asynchronous callback to handle success / failure
        ApiFutures.addCallback(future, futureCallback(message));
    }


    private ApiFutureCallback futureCallback(String message) {

        return new ApiFutureCallback<>() {

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {

                    ApiException apiException = ((ApiException) throwable);

                    // details on the API exception
                    log.debug(String.valueOf(apiException.getStatusCode().getCode()));
                    log.debug(String.valueOf(apiException.isRetryable()));
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
