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
import com.ets.mgl804.fonctions._
import com.ets.mgl804.data._

object PlinkMethod {

  def file(name:String) : Unit = {
    println("file with filename: " + name)
  }

  def makeBed(name:List[String]) : Unit = {

    val filename = name(0)

    val conf = new SparkConf().setAppName("Slink").setMaster("local")
    val sc   = new SparkContext(conf)

    val writeLog    = new WriteLog(sc, filename)
    val importFiles = new MakeBed(sc, filename, writeLog)

    if (importFiles.loadData()) {
      // Data loaded correctly
      writeLog.addLogLine("Data load finished with success.")
      importFiles.importRecord.ViewContent
    } else {
      // Error in data load
      writeLog.addLogLine("ERROR : Data load failed.")
    }

    sc.stop()
  }

  def genome(name:Unit) : Unit = {
    println("genome")
  }

  def readGenome(name:String) : Unit = {
    println("read-genome with filename: " + name)
  }

  def cluster(name:Unit) : Unit = {
    println("cluster")
  }
}