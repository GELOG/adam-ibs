package com.ets.mgl804.core

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}
import org.slf4j.LoggerFactory

/**
 * Created by ikizema on 15-07-03.
 */
object AppContext {
  val conf = new SparkConf().setAppName("Adam-IBS").setMaster("local")
//    .set("akka.version","2.3.4")
//    .set("akka.actor.guardian-supervisor-strategy","/")
//    .set("akka.actor.creation-timeout","10")
//    .set("akka.actor.unstarted-push-timeout", "20")
  val sc = new SparkContext(conf)
  val sqc = new SQLContext(sc)
  sqc.sql("SET spark.sql.parquet.binaryAsString=true")
  var logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Load AppContext")
}
