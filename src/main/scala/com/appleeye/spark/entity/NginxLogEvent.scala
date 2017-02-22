package com.appleeye.spark.entity

/**
  * Created by xiaoliu on 15/2/2017.

1487233467.995
0.005
111.161.59.195
-/200
591
GET
http://changyan.sohu.com/api/gold/user/get_coins?callback=jQuery1706211163070984185_1487233464670&client_id=cysfFizOu&_=1487233467763
-
DIRECT/10.16.39.68:80(0.001)
application/x-javascript; charset=UTF-8
"http://m.biquge.cc/html/9/9378/"
"Mozilla/5.0(Linux; U; Android 6.0.1; zh-cn; OPPO A57 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.2 Mobile Safari/537.36"
"debug_uuid=C766D1F3A3F00001133BA550DF646770; sohucookie=thirdparty; debug_test=sohu_third_cookie"
@
  ***/
case class NginxLogEvent (var eventTs:String, var responseTime:String, var srcIP:String, var status:String,
                          var bodySize:String, var method:String,  var url:String,
                          var dstIP:String, var contentType:String, var referUrl:String,
                          var userAgent:String, var cookie:String, var identity: Identity,
                          var location:Location, var uagent:UAgent, var datePartition:DatePartition)

case class Identity(var uvID:String, var ipLOC:String)
case class Location(var country:String, var state:String, var city:String)
case class UAgent(var agent:String, var os:String, var device:String)
case class DatePartition(var year:Int, var month:Int, var day:Int, var hour:Int)

object NginxLogEvent{
  val DEFAULT_STR = ""
  def apply(eventTs: String,
            responseTime: String,
            srcIP: String,
            status: String,
            bodySize: String,
            method: String,
            url: String,
            dstIP: String,
            contentType: String,
            referUrl: String,
            userAgent: String,
            cookie: String
  ): NginxLogEvent = new NginxLogEvent(
     eventTs, responseTime, srcIP, status,bodySize, method, url,
    dstIP, contentType, referUrl, userAgent, cookie,
    Identity(DEFAULT_STR, DEFAULT_STR),
    Location(DEFAULT_STR, DEFAULT_STR,DEFAULT_STR),
    UAgent(DEFAULT_STR, DEFAULT_STR, DEFAULT_STR),
    DatePartition(0, 0, 0, 0))
}
