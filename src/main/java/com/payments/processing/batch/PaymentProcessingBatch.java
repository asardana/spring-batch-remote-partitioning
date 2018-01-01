package com.payments.processing.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@IntegrationComponentScan
public class PaymentProcessingBatch {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PaymentProcessingBatch.class, args);
    }
}
