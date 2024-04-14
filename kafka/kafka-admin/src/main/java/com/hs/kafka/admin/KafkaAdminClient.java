package com.hs.kafka.admin;

import com.hs.app.config.KafkaConfigData;
import com.hs.app.config.RetryConfigData;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class KafkaAdminClient {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaAdminClient.class);
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, AdminClient adminClient, RetryTemplate retryTemplate, WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }
    
    public void createKafkaTopicsWithRetry(){
        var createTopicResult = retryTemplate.execute(this::createKafkaTopics);
        LOG.info("Create topic result {}", createTopicResult.values().values());
    }

    private CreateTopicsResult createKafkaTopics(RetryContext retryContext) {
        var topicNames = kafkaConfigData.getTopicNamesToCreate();
        LOG.info("Creating {} topics, attempts {}", topicNames.size(), retryContext.getRetryCount());
        var kafkaTopicsToCreate = topicNames.stream()
                .map(topicName -> new NewTopic(
                        topicName.trim(),
                        kafkaConfigData.getNumOfPartitions(),
                        kafkaConfigData.getReplicationFactor()
                ))
                .toList();
        return adminClient.createTopics(kafkaTopicsToCreate);
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            try {
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException e) {
                throw new KafkaAdminClientException("Error while waiting to check new created topics", e);
            }
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return (HttpStatus) webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }
    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry) {
            throw new KafkaAdminClientException("Reached max number of retry for reading kafka topic(s)!");
        }
    }
}
