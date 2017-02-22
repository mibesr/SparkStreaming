#!/usr/bin/env bash
spark-submit --class com.appleeye.spark.driver.KafkaStreamingProcess \
--master yarn --deploy-mode client --driver-memory 5G --num-executors 6 \
--executor-cores 4 executor-memory 6G \
/data_b/xiaoliu/bitbucket/spark-streaming/target/scala-2.10/spark-kafka-streaming_2.10-1.0.0-SNAPSHOT.jar \
"$brokers" "$topics"  "$geoFilePath"  "$outputidir"

# need to add JVM settings later