package app;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
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

    public void publishGeneratedData(List<LinkedHashMap<String, Object>> messages, String topic, String projectId) {

        ProjectTopicName topicName = ProjectTopicName.of(projectId, topic);
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            for (final Map<String, Object> message : messages) {
                var jsonMessage = UtilController.linkedHashMapToJSON(message);
                publishData(jsonMessage, publisher, topic, projectId);
            }

        } catch (IOException e) {
            log.error("Pub/Sub Publishing Error: " + e.getMessage());

        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                try {
                    publisher.shutdown();
                } catch (Exception e) {
                    log.error("Pub/Sub Shutdown Error: " + e.getMessage());
                }
            }
        }
    }

    private void publishData(String message, Publisher publisher, String topic, String projectId) {

        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        // Once published, returns a server-assigned message id (unique within the topic)
        ApiFuture<String> future = publisher.publish(pubsubMessage);

        // Add an asynchronous callback to handle success / failure
        ApiFutures.addCallback(future, futureCallback(message, topic, projectId));
    }


    private ApiFutureCallback futureCallback(String message, String topic, String projectId) {

        return new ApiFutureCallback<>() {

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {

                    ApiException apiException = ((ApiException) throwable);

                    // details on the API exception
                    var errorCode = String.valueOf(apiException.getStatusCode().getCode());

                    // creates new publisher with the topic that is not found
                    if (errorCode.equalsIgnoreCase("NOT_FOUND")) {
                        Publisher newPublisher = createNewPublisherWithTopic(topic, projectId);

                        // retries the message publishing
                        if (newPublisher != null) {
                            publishData(message, newPublisher, topic, projectId);
                        }
                        log.debug("Topic was not found. One has been created for " + topic +
                                  " and an attempt to retry the message has been performed.");
                    } else {
                        log.error(errorCode);
                        log.error(String.valueOf(apiException.isRetryable()));
                        log.error("Error publishing message : " + message);
                    }
                }

            }

            @Override
            public void onSuccess(Object o) {
                log.debug(o.toString());
            }
        };
    }

    private Publisher createNewPublisherWithTopic(String topic, String projectId) {
        Publisher publisher = null;
        try {
            // creates topic in Pub/Sub for the new publisher
            createTopic(topic, projectId);
            ProjectTopicName topicName = ProjectTopicName.of(projectId, topic);

            // creates publisher with new topic
            publisher = Publisher.newBuilder(topicName).build();
            return publisher;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return publisher;
    }

    private void createTopic(String topicId, String projectId) throws IOException {
        // Create a new topic
        ProjectTopicName topic = ProjectTopicName.of(projectId, topicId);
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            topicAdminClient.createTopic(topic);
        } catch (ApiException e) {
            // example : code = ALREADY_EXISTS(409) implies topic already exists
            log.error("Error thrown while creating new topic. Topic = " + topicId + " code = " + e.getStatusCode().getCode());
            log.error("Is Retryable? = " + e.isRetryable());
        }

        System.out.printf("Topic %s:%s created.\n", topic.getProject(), topic.getTopic());
    }
}
