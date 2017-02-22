package com.appleeye.spark.utils


import java.sql.Timestamp

import org.joda.time.{DateTime, DateTimeZone, Days}

/**
  *
 */

object Date {
  def calculateDatePartition(timestamp: Timestamp): (Int, Int, Int, Int) = {
    val dateTime = new DateTime(timestamp)
    (dateTime.getYear,dateTime.getMonthOfYear, dateTime.getDayOfMonth, dateTime.getHourOfDay)
  }
}
