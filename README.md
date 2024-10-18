# Starting Kafka
## Terminal 1
```shell
cd ~/tools/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties
```

## Terminal 2
```shell
cd ~/tools/kafka
bin/kafka-server-start.sh config/server.properties
```

# Running `producer`
```shell
mvn clean install
```
```shell
java -jar kafka-producer-wikimedia/target/kafka-producer-wikimedia-0.0.1-SNAPSHOT.jar com.samples.springboot.SpringBootProducerApplication
```

# Running `consumer`
```mysql
create database wikimedia;
```

After running `kafka-producer-wikimedia` data should be in the queue
# Terminal 3
```shell
bin/kafka-console-consumer.sh --topic wikimedia_recentchange --from-beginning --bootstrap-server localhost:9092
```