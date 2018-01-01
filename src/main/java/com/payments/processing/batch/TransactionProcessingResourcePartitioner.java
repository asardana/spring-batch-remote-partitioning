package com.payments.processing.batch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Aman on 12/2/2017.
 */
public class TransactionProcessingResourcePartitioner implements Partitioner {

    @Autowired
    ResourcePatternResolver resourcePatternResolver;


    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        AtomicInteger partitionNumber = new AtomicInteger(1);
        Resource[] resources;

        try {

            resources = resourcePatternResolver.getResources("classpath*:/*.txt");

            System.out.println("List of input resources ---> " + resources.length);
            Arrays.stream(resources).forEach(file -> {

                ExecutionContext context = new ExecutionContext();

                context.putString("fileName", file.getFilename());
                context.putString("outputFile", "output_" + file.getFilename());

                map.put("partition" + partitionNumber.getAndIncrement(), context);

            });
            System.out.println("Partitions Created");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return map;

    }

}
