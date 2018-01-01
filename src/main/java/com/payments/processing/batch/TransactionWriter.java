package com.payments.processing.batch;

import org.springframework.batch.item.ItemWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 11/19/2017.
 */
public class TransactionWriter implements ItemWriter<Transaction> {

    private String outputFile;

    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        List<String> enrichedTxnList = new ArrayList<>();
        items.forEach(item -> {
            String enrichedTxn = String.join(",", item.getTransactionId(), item.getMerchantId(), item.getMerchantName(), item.getTransactionAmt());
            enrichedTxnList.add(enrichedTxn);
        });
        enrichedTxnList.forEach(System.out::println);
        //Files.write(Paths.get("./output/" + outputFile), enrichedTxnList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        //Files.write(Paths.get("c://output/" + outputFile), enrichedTxnList, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
