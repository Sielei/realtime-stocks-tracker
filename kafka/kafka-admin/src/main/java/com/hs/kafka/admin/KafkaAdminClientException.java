package com.hs.kafka.admin;

public class KafkaAdminClientException extends RuntimeException{
    public KafkaAdminClientException(String message) {
        super(message);
    }

    public KafkaAdminClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
