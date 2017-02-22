package com.appleeye.spark.utils

import org.apache.spark.streaming.StreamingContext
import kafka.serializer.StringDecoder
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka._
/**
  * Created by xiaoliu on 17/2/2017.
  */
object StreamUtils {

  def getDStreamFromSockt(ip:String, port:String, ssc:StreamingContext) = {
    ssc.socketTextStream(ip, port.toInt, StorageLevel.MEMORY_AND_DISK_SER)
  }

  def getDStreamFromKafka(brokers:String, topics:String, ssc:StreamingContext) = {
    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
      "auto.offset.reset" -> "smallest") // set to the smallest for debug
    KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet).map(_._2)
  }

}
