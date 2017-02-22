package com.appleeye.spark.extract

import java.sql.Timestamp

import com.appleeye.spark.entity.NginxLogEvent
import com.appleeye.spark.utils.{Date, UserAgentParser}
import com.appleeye.spark.utils.GeoIP.MaxMindIpGeo

/**
  * Created by xiaoliu on 17/2/2017.
  */

object ETLProcess extends Serializable {
  def isFilter(e: NginxLogEvent) = e != null
  """
    This will split on one or more spaces only if those spaces are followed by zero,
    or an even number of quotes (all the way to the end of the string!).
  """
  val splitPattern ="[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"

  """
    This function is to extract and fill the fields of each record
  """.stripMargin

  def process(e: NginxLogEvent, ipGeo: MaxMindIpGeo):NginxLogEvent = {

    e.status = e.status.replaceFirst("-/", "")
    e.url = e.url.replace("https://", "http://").
      replace("http://","").split("\\?")(0)
    e.referUrl = e.referUrl.replace("https://", "http://").
      replace("http://","")
    e.contentType = e.contentType.split(";")(0)

    val cookie_map = parseCookie(e.cookie)
    e.identity.ipLOC = cookie_map.getOrElse("IPLOC", "-")
    e.identity.uvID  = cookie_map.getOrElse("SUV", "-").replace("\"", "");

    val location = ip2location(e.srcIP, ipGeo)
    e.location.country = location._1
    e.location.state = location._2
    e.location.city = location._3

    val datePartition = Date.calculateDatePartition(new Timestamp(
      e.eventTs.toDouble.toLong * 1000))
    e.datePartition.year  = datePartition._1
    e.datePartition.month = datePartition._2
    e.datePartition.day   = datePartition._3
    e.datePartition.hour  = datePartition._4
    // CPU-consuming, need more optimazation
//    val userAgent = UserAgentParser.parse(e.userAgent)
//    e.uagent.agent = userAgent._1
//    e.uagent.os = userAgent._2
//    e.uagent.device = userAgent._3
    e
  }

  def formatline(e: String):String ={
    e.replace("; ", ";")
  }

  def parseCookie(cookie_str: String):Map[String, String]= {
    cookie_str.split(";").map(e => e.split("=") match {
      case Array(f1, f2) => (f1, f2)
      case _ => ("-", "-") // add empty string handle
    }).toMap
  }

  //return country, state, city
  def ip2location(ip: String, geoIp: MaxMindIpGeo): (String, String, String) = {
    geoIp.getLocation(ip) match {
      case Some(ipLocation) => (ipLocation.countryCode.getOrElse("-"),
        ipLocation.region.getOrElse("-"),
        ipLocation.city.getOrElse("-"))
      case None => ("-", "-", "-")
    }
  }
}
