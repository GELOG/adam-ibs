package com.ets.mgl804.cli

/**
 * Created by karim on 26/06/15.
 */
/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

import org.apache.spark.{SparkContext, SparkConf}
import org.slf4j.{LoggerFactory, Logger}
import ch.qos.logback.classic.LoggerContext
import com.ets.mgl804.fonctions._
import com.ets.mgl804.data._

object PlinkMethod {

  def logb = LoggerFactory.getLogger(this.getClass())

  def file(name:String) : Unit = {

    logb.info("file with filename: " + name)
  }

  def makeBed(name:List[String]) : Unit = {

    val filename = name(0)

    logb.info("Filename : " + filename)

    val conf = new SparkConf().setAppName("Slink").setMaster("local")
    val sc   = new SparkContext(conf)

    val importFiles = new MakeBed(sc, filename)

    if (importFiles.loadData()) {

      logb.info("Data load finished with success.")
      importFiles.importRecord.ViewContent

    } else {
      logb.error("Data load failed")
    }
    sc.stop()
  }

  def genome(name:Unit) : Unit = {
    logb.info("genome")
  }

  def readGenome(name:String) : Unit = {
    logb.info("read-genome with filename: " + name)
  }

  def cluster(name:Unit) : Unit = {
    logb.info("cluster")
  }
}