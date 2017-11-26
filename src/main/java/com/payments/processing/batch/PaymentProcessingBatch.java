package com.payments.processing.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
public class PaymentProcessingBatch {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PaymentProcessingBatch.class, args);
    }
}
