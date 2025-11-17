# spring-batch-remote-partitioning

In this post we’ll look at how to scale a spring batch application on AWS using remote partitioning technique.

Spring batch applications can be scaled by running multiple process in parallel on remote machines that can work independently on the partitioned data. There is a master step that knows how to partition the data and then send the request to slave steps running on remote machines.

The code discussed in this post is available on GitHub.

There are certain prerequisites in order to run the spring batch application with remote partitioning.

MySQL – We need a database to store the spring batch metadata (Job Repository). This is required for the master step to keep track of the slave tasks. I have provisioned a MySQL RDS instance containing a ‘batchdb’ schema.
batchp1

RabbitMQ – We need a messaging middleware to allow master step to send messages to slave tasks running on remote machines. The messages would let the slave tasks know which partitioned data set records they would each process. I have installed RabbitMQ service on an EC2 instance and created the corresponding exchanges and queues.
batchp2

EC2 instances –  We need 3 EC2 instances with 1 instance running the master step (batchMaster) and the remaining 2 nodes running the slave tasks (batchSlave1 and batchSlave2)
batchp3.JPG

TransactionProcessingResourcePartitioner class reads the input files and creates a map of execution context for the slave steps to know which files they need to process.

```
try {
 
    resources = resourcePatternResolver.getResources("classpath*:/*.txt");
 
    System.out.println("List of input resources ---&amp;amp;gt; " + resources.length);
    Arrays.stream(resources).forEach(file -&amp;amp;gt; {
 
        ExecutionContext context = new ExecutionContext();
 
        context.putString("fileName", file.getFilename());
        context.putString("outputFile", "output_" + file.getFilename());
 
        map.put("partition" + partitionNumber.getAndIncrement(), context);
 
    });
    System.out.println("Partitions Created");
} catch (IOException e) {
    e.printStackTrace();
}

```
The master step would then send the messages to RabbitMQ using Spring Integration.

RemotePartitioningAMQPConfiguration class has a AMQPOutbountEndpoint configured as a service activator whenever message arrives in the input channel.

```
@Bean
@ServiceActivator(inputChannel = "outboundRequests")
public AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate template) {
AmqpOutboundEndpoint endpoint = new AmqpOutboundEndpoint(template);
 
endpoint.setExpectReply(true);
endpoint.setOutputChannel(inboundRequests());
 
endpoint.setExchangeName("batch");
endpoint.setRoutingKey("partition.requests");
 
System.out.println("Created AMQP Outbound endpoint---> " + endpoint.toString());
 
return endpoint;
}
```
Slave nodes are configured to receive the messages from RabbitMQ via the AmqpInboundChannelAdapter. The adapter then outputs the message to an output channel to activate the StepExecutionRequestHandler service.

```
@Bean
@Profile("slave")
public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer) {
AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
 
adapter.setOutputChannel(inboundRequests());
 
adapter.afterPropertiesSet();
 
return adapter;
}

```
BatchConfiguration class has the StepExecutionRequestHandler annotated with service activator to handle the incoming messages from the master step.

```
@Bean
@Profile("slave")
@ServiceActivator(inputChannel = "inboundRequests", outputChannel = "outboundStaging")
public StepExecutionRequestHandler stepExecutionRequestHandler() {
StepExecutionRequestHandler stepExecutionRequestHandler =
new StepExecutionRequestHandler();
 
BeanFactoryStepLocator stepLocator = new BeanFactoryStepLocator();
stepLocator.setBeanFactory(this.applicationContext);
stepExecutionRequestHandler.setStepLocator(stepLocator);
stepExecutionRequestHandler.setJobExplorer(this.jobExplorer);
 
return stepExecutionRequestHandler;
}
```
Follow the following steps to run the batch application with remote partitioning.

Copy the batch executable jar to all the three EC2 instances
Run the slave tasks by executing the following command
java -jar -Dspring.profiles.active=slave batch-example-0.0.1-SNAPSHOT.jar

Run the master task by executing the following command
java -jar -Dspring.profiles.active=master batch-example-0.0.1-SNAPSHOT.jar

After the master task is started, we can see that the slave tasks start processing the files in parallel. Each slave is an independent step running remotely and process the files in parallel



