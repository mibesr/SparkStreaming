package com.appleeye.spark.extract

import java.sql.Timestamp

import com.appleeye.spark.utils.{Date, UserAgentParser}
import org.scalatest.FunSuite
import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers._

/**
  * Created by xiaoliu on 20/2/2017.
  */

class ETLProcessTest extends FunSuite with PropertyChecks {
"""
  |1487233391 => 2017/2/16 16:23:11
""".stripMargin

  val testDateData = Map(
    "1487233391.805"
  ->(2017, 2, 16, 16))



  test("Test Datetime check") {
    for((ts, expected) <- testDateData){
       Date.calculateDatePartition(
         new Timestamp(ts.toDouble.toLong * 1000)) shouldBe expected
    }
  }

  val testUAData = Map(
    "Mozilla/5.0 " +
      "(iPhone; CPU iPhone OS 5_1_1 like Mac OS X) AppleWebKit/534.46 " +
      "(KHTML, like Gecko) Version/5.1 Mobile/9B206 Safari/7534.48.3"
      -> ("Mobile Safari", "iOS", "iPhone"),
    "" -> ("Other", "Other", "Other")
  )

  test("Test UserAgent") {
    for((userAgentStr, expected) <- testUAData){
      UserAgentParser.parse(userAgentStr) shouldBe expected
    }
  }

  val testCookieData = Map(
    "a=1;b=2" -> Map("a"->"1",
                     "b"->"2"),
    "-" -> Map("-"->"-")
  )

  test("Test UserCookie") {
    for((userCookie, expected) <- testCookieData){
      ETLProcess.parseCookie(userCookie) shouldBe expected
    }
  }
}
