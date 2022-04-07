



bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic loyalty-topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic hadoop-sink-topic


bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic loyalty-topic --from-beginning --property print.key=true --property key.separator=":"
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hadoop-sink-topic --from-beginning --property print.key=true --property key.separator=":"