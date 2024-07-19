package org.example;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String... args) throws Exception {
        // TODO(developer): Replace these variables before running the sample.
        String projectId = "mydemoproject2-427115";
        String topicId = "roxie_topic";

        publishWithErrorHandlerExample(projectId, topicId);
    }

    public static void publishWithErrorHandlerExample(String projectId, String topicId)
            throws IOException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            //Change messages as needed. Should be copied in as JSON formatted.
            List<String> messages = Arrays.asList("{\n" +
                    "           \"recid\": 644126,\n" +
                    "           \"id\": \"5082066557802196041\",\n" +
                    "           \"firstname\": \"CELENIA\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                            "           \"middlename\": \" \",\n" +
                    "           \"filedate\": 19920319,\n" +
                    "           \"bureaucode\": 199,\n" +
                    "           \"gender\": \"F\",\n" +
                    "           \"birthdate\": 19221201,\n" +
                    "           \"streetaddress\": \"2355 FOREST HILLS DR\",\n" +
                    "           \"csz_id\": 2437,\n" +
                    "           \"recpos\": \"72142000\",\n" +
                    "           \"city\": \"TRANSFER\",\n" +
                    "           \"state\": \"PA\",\n" +
                    "           \"zipcode\": 16154\n" +
                    "         }", "{\n" +
                    "           \"recid\": 44886,\n" +
                    "           \"id\": \"7830553978810542989\",\n" +
                    "           \"firstname\": \"GANIJA\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"Z\",\n" +
                    "           \"filedate\": 19870909,\n" +
                    "           \"bureaucode\": 13,\n" +
                    "           \"gender\": \"F\",\n" +
                    "           \"birthdate\": 19260301,\n" +
                    "           \"streetaddress\": \"2090 POTTS HILL RD\",\n" +
                    "           \"csz_id\": 7619,\n" +
                    "           \"recpos\": \"5027120\",\n" +
                    "           \"city\": \"POMPTON PLAINS\",\n" +
                    "           \"state\": \"NJ\",\n" +
                    "           \"zipcode\": 7444\n" +
                    "         }",
                    "        {\n" +
                    "           \"recid\": 277642,\n" +
                    "           \"id\": \"4364131847069821064\",\n" +
                    "           \"firstname\": \"MONTAKARN\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"Q\",\n" +
                    "           \"filedate\": 19870309,\n" +
                    "           \"bureaucode\": 13,\n" +
                    "           \"gender\": \"M\",\n" +
                    "           \"birthdate\": 19640929,\n" +
                    "           \"streetaddress\": \"45 MALLARD RD\",\n" +
                    "           \"csz_id\": 43972,\n" +
                    "           \"recpos\": \"31095792\",\n" +
                    "           \"city\": \"DUNCANSVILLE\",\n" +
                    "           \"state\": \"PA\",\n" +
                    "           \"zipcode\": 16635\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 727071,\n" +
                    "           \"id\": \"12221818927523640828\",\n" +
                    "           \"firstname\": \"RUEI\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"S\",\n" +
                    "           \"filedate\": 19950921,\n" +
                    "           \"bureaucode\": 252,\n" +
                    "           \"gender\": \"M\",\n" +
                    "           \"birthdate\": 19751201,\n" +
                    "           \"streetaddress\": \"43 HILLANDO DR\",\n" +
                    "           \"csz_id\": 14260,\n" +
                    "           \"recpos\": \"81431840\",\n" +
                    "           \"city\": \"GLOUCESTER POIN\",\n" +
                    "           \"state\": \"VA\",\n" +
                    "           \"zipcode\": 23062\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 533969,\n" +
                    "           \"id\": \"17844693484560518926\",\n" +
                    "           \"firstname\": \"ANNONAN\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                            "           \"middlename\": \" \",\n" +
                    "           \"filedate\": 19850821,\n" +
                    "           \"bureaucode\": 376,\n" +
                    "           \"gender\": \"F\",\n" +
                    "           \"birthdate\": 19530108,\n" +
                    "           \"streetaddress\": \"134 E 17TH ST APT 63\",\n" +
                    "           \"csz_id\": 24474,\n" +
                    "           \"recpos\": \"59804416\",\n" +
                    "           \"city\": \"JAMESON\",\n" +
                    "           \"state\": \"MO\",\n" +
                    "           \"zipcode\": 64647\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 596872,\n" +
                    "           \"id\": \"1166595353551052552\",\n" +
                    "           \"firstname\": \"DETELIN\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"M\",\n" +
                    "           \"filedate\": 19861027,\n" +
                    "           \"bureaucode\": 171,\n" +
                    "           \"gender\": \"U\",\n" +
                    "           \"birthdate\": 19650301,\n" +
                    "           \"streetaddress\": \"338 W 89TH ST APT 3R\",\n" +
                    "           \"csz_id\": 968,\n" +
                    "           \"recpos\": \"66849552\",\n" +
                    "           \"city\": \"ARLINGTON\",\n" +
                    "           \"state\": \"MN\",\n" +
                    "           \"zipcode\": 55307\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 347861,\n" +
                    "           \"id\": \"675797059124681114\",\n" +
                    "           \"firstname\": \"NAMIT\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                            "           \"middlename\": \" \",\n" +
                    "           \"filedate\": 19860401,\n" +
                    "           \"bureaucode\": 24,\n" +
                    "           \"gender\": \"M\",\n" +
                    "           \"birthdate\": 19640928,\n" +
                    "           \"streetaddress\": \"221 CLERMONT AVE # 1\",\n" +
                    "           \"csz_id\": 32567,\n" +
                    "           \"recpos\": \"38960320\",\n" +
                    "           \"city\": \"GLEN ELLYN\",\n" +
                    "           \"state\": \"IL\",\n" +
                    "           \"zipcode\": 60138\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 623354,\n" +
                    "           \"id\": \"17588373756131785978\",\n" +
                    "           \"firstname\": \"TEH\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"L\",\n" +
                    "           \"namesuffix\": \"SR\",\n" +
                    "           \"filedate\": 19871001,\n" +
                    "           \"bureaucode\": 238,\n" +
                    "           \"gender\": \"M\",\n" +
                    "           \"birthdate\": 19561201,\n" +
                    "           \"streetaddress\": \"18 CROSS RIDGE RD\",\n" +
                    "           \"csz_id\": 3598,\n" +
                    "           \"recpos\": \"69815536\",\n" +
                    "           \"city\": \"EAST ORANGE\",\n" +
                    "           \"state\": \"NJ\",\n" +
                    "           \"zipcode\": 7017\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 609590,\n" +
                    "           \"id\": \"5832249607040579389\",\n" +
                    "           \"firstname\": \"VALDINA\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                    "           \"middlename\": \"T\",\n" +
                    "           \"filedate\": 19940913,\n" +
                    "           \"bureaucode\": 352,\n" +
                    "           \"gender\": \"F\",\n" +
                    "           \"birthdate\": 19730712,\n" +
                    "           \"streetaddress\": \"122 N BROAD ST\",\n" +
                    "           \"csz_id\": 44428,\n" +
                    "           \"recpos\": \"68273968\",\n" +
                    "           \"city\": \"PITTSFIELD\",\n" +
                    "           \"state\": \"NH\",\n" +
                    "           \"zipcode\": 3263\n" +
                    "         }",
                    "         {\n" +
                    "           \"recid\": 341777,\n" +
                    "           \"id\": \"8451032049636405291\",\n" +
                    "           \"firstname\": \"YAMROT\",\n" +
                    "           \"lastname\": \"SMITH\",\n" +
                            "           \"middlename\": \" \",\n" +
                    "           \"filedate\": 20000810,\n" +
                    "           \"bureaucode\": 168,\n" +
                    "           \"gender\": \"F\",\n" +
                    "           \"birthdate\": 19611214,\n" +
                    "           \"streetaddress\": \"280 W 25TH ST # C\",\n" +
                    "           \"csz_id\": 5289,\n" +
                    "           \"recpos\": \"38278912\",\n" +
                    "           \"city\": \"FAYETTEVILLE\",\n" +
                    "           \"state\": \"NC\",\n" +
                    "           \"zipcode\": 28314\n" +
                    "         }");

            for (final String message : messages) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> future = publisher.publish(pubsubMessage);

                // Add an asynchronous callback to handle success / failure
                ApiFutures.addCallback(
                        future,
                        new ApiFutureCallback<String>() {

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
                            public void onSuccess(String messageId) {
                                // Once published, returns server-assigned message ids (unique within the topic)
                                System.out.println("Published message ID: " + messageId);
                            }
                        },
                        MoreExecutors.directExecutor());
            }
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}