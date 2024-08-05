package org.example;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.ApiFutureCallback;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String... args) throws Exception {
        String projectId = "mydemoproject2-427115";
        String topicId = "foodsales2_topic";
        String csvFile = "/Users/scarletthuang/hpccdata/dropzone/sampledatafoodsales.csv";
        List<String> jsonMessages = convertCsvToJson(csvFile);
        publishWithErrorHandlerExample(projectId, topicId, jsonMessages);
    }

    public static List<String> convertCsvToJson(String csvFile) {
        List<String> jsonMessages = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] headers = csvReader.readNext(); // Read the first line which contains headers

            if (headers == null) {
                throw new IOException("No headers found in CSV file.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String[] line;

            while ((line = csvReader.readNext()) != null) {
                Map<String, String> jsonMap = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    // Replace invalid characters and ensure field names are valid
                    String header = headers[i].replaceAll("[^A-Za-z0-9_]", "_");
                    jsonMap.put(header, line[i]);
                }

                String jsonMessage = objectMapper.writeValueAsString(jsonMap);
                jsonMessages.add(jsonMessage);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return jsonMessages;
    }

    public static void publishWithErrorHandlerExample(String projectId, String topicId, List<String> jsonMessages)
            throws IOException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();

            for (final String message : jsonMessages) {
                // Print message for debugging
                System.out.println("Publishing message: " + message);

                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                ApiFuture<String> future = publisher.publish(pubsubMessage);

                ApiFutures.addCallback(
                        future,
                        new ApiFutureCallback<String>() {

                            @Override
                            public void onFailure(Throwable throwable) {
                                if (throwable instanceof ApiException) {
                                    ApiException apiException = ((ApiException) throwable);
                                    System.out.println(apiException.getStatusCode().getCode());
                                    System.out.println(apiException.isRetryable());
                                    System.out.println(apiException.getMessage());
                                }
                                System.out.println("Error publishing message: " + message);
                            }

                            @Override
                            public void onSuccess(String messageId) {
                                System.out.println("Published message ID: " + messageId);
                            }
                        },
                        MoreExecutors.directExecutor());
            }
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}