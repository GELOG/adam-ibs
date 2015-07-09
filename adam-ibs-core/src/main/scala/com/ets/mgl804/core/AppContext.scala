package com.ets.mgl804.core

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by ikizema on 15-07-03.
 */
object AppContext {
  val conf = new SparkConf().setAppName("Adam-IBS").setMaster("local")
  val sc = new SparkContext(conf)
}
