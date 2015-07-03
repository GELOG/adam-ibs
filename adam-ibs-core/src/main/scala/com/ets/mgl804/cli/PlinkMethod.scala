package com.ets.mgl804.cli

/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

import com.ets.mgl804.fonctions._
import org.apache.spark.{SparkConf, SparkContext}

object PlinkMethod {
  val conf = new SparkConf().setAppName("Adam-IBS").setMaster("local")
  val sc = new SparkContext(conf)

  var fileName = new String()

  def file(name:String) {
    this.fileName = name
//    println("File with file: " + name)
  }

  def makeBed() {
    val writeLog    = new WriteLog(sc, this.fileName)
    val importFiles = new MakeBed(sc, this.fileName, writeLog)

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

  def readGenome() : Unit = {
    println("read-genome")
  }

  def cluster(name:Unit) : Unit = {
    println("cluster")
  }
}