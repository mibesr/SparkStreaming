package com.appleeye.spark.utils
import org.uaparser.scala.{Client, Parser}

/**
  * Created by xiaoliu on 20/2/2017.
  */
object UserAgentParser {

  def parse(userAgentStr:String):(String, String, String)={
    Parser.get.parse(userAgentStr) match {
      case c:Client =>(c.userAgent.family, c.os.family, c.device.family)
    }
  }
}
