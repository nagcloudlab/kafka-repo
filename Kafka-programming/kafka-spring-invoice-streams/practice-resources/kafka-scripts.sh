


bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic pos-topic
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic pos-topic --from-beginning --property print.key=true --property key.separator=":"

