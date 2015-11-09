package com.ets.mgl804.core

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}
import org.slf4j.LoggerFactory

/**
 * Created by ikizema on 15-07-03.
 */
object AppContext {
  val conf = new SparkConf().setAppName("Adam-IBS").setMaster("local")
  val sc = new SparkContext(conf)
  val sqc = new SQLContext(sc)
  sqc.sql("SET spark.sql.parquet.binaryAsString=true")
  var logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Load AppContext")
}
