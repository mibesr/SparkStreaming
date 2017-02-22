package com.appleeye.spark.driver

/**
  * Created by xiaoliu on 13/2/2017.
  */
import com.appleeye.spark.entity.NginxLogEvent
import com.appleeye.spark.extract.ETLProcess
import com.appleeye.spark.utils.GeoIP.MaxMindIpGeo
import com.appleeye.spark.utils.{HdfsUtils, StreamUtils}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext, SparkFiles}
import org.apache.spark.streaming._

object KafkaStreamingProcess {

  val schemaString = "eventTs responseTime srcIP status " +
    "bodySize method url dstIP contentType referUrl userAgent cookie uvID ipLOC" +
    " agent os device country state city"

  val schemaInt="year month day hour"
  val schema =
    StructType(
      schemaString.split(" ").map(fieldName =>
        StructField(fieldName, StringType, true))
        ++ schemaInt.split(" ").map(fieldName =>
        StructField(fieldName, IntegerType, true)))

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println(s"""
                            |Usage: KafkaStreamingProcess <brokers> <topics>
                            |  <brokers> is a list of one or more Kafka brokers
                            |  <topics> is a list of one or more kafka topics to consume from
                            |
        """.stripMargin)
      System.exit(1)
    }
    val Array(param1, param2, geoFileDir, outputDir) = args

//    val geoFilePath = "/Users/xiaoliu/develop/local/ipdata/"
    val geoFileName = "GeoLite2-City.mmdb"
//    val outputDir = "/Users/xiaoliu/develop/local/data3"

    val sparkConf = new SparkConf().
      setAppName("SparkKafkaStreaming").
      set("spark.streaming.blockInterval","2000ms")
    //    .setMaster("local[4]")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)
    val ssc = new StreamingContext(sc, Seconds(12))

    sc.addFile(HdfsUtils.getFullPath(geoFileDir, sc) + "/" + geoFileName)

    // val lines =StreamUtils.getDStreamFromSockt(args, ssc)
    val lines = StreamUtils.getDStreamFromKafka(param1, param2, ssc)

    lines.map(e=>ETLProcess.formatline(e))
      .map(_.split(ETLProcess.splitPattern))
      .map(x => {NginxLogEvent(x(0), x(1), x(2), x(3),
                          x(4), x(5), x(6), x(8),
                          x(9), x(10), x(11), x(12))
      }).filter(e=>ETLProcess.isFilter(e)).mapPartitions(events =>{
      val geoIp = MaxMindIpGeo(SparkFiles.get(geoFileName), 10000, synchronized = true)
      events.map(e=>ETLProcess.process(e, geoIp))}
    ).map(e=>Row(e.eventTs, e.responseTime, e.srcIP, e.status,
      e.bodySize, e.method, e.url,e.dstIP, e.contentType,
      e.referUrl, e.userAgent, e.cookie,
      e.identity.uvID, e.identity.ipLOC,
      e.uagent.agent, e.uagent.os, e.uagent.device,
      e.location.country, e.location.state, e.location.city,
      e.datePartition.year, e.datePartition.month, e.datePartition.day, e.datePartition.hour)).
      foreachRDD(rdd => {
        val eventDF = sqlContext.createDataFrame(rdd, schema)
        eventDF.write.mode(SaveMode.Append)
          .partitionBy("year", "month", "day", "hour")
          .parquet(outputDir)
        })
    //spark generally writes one file per partition
    ssc.start()
    ssc.awaitTermination()
  }
}
