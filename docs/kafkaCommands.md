# Helpful Kafka commands:

1. Reseting Kafka Stream application offset:
./kafka-streams-application-reset.sh --input-topics Ratings --application-id streams-pipe --to-earliest

2. Running consumer:
./kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic Ratings --from-beginning

3. Running producer:
✗ ./kafka_2.11-2.3.1/bin/kafka-console-producer.sh --broker-list localhost:9093 --topic Ratings

4. When Docker Image of Kafka Manager won't run because of non deleted PID_FILE:
docker-compose up --build --force-recreate kafka-manager
