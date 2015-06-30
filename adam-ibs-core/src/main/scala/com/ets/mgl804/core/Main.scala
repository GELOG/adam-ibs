package com.ets.mgl804.core

import com.ets.mgl804.fonctions._
import org.apache.spark.{SparkContext, SparkConf}
import com.ets.mgl804.data._

/**
 * Created by ikizema on 15-05-30  : ivan.kizema at gmail.com
 */
object Main {
  val conf = new SparkConf().setAppName("Slink").setMaster("local")
  val sc = new SparkContext(conf)

  def main(args: Array[String]) {
    val fileName = args(0)  // First args = filename

    val writeLog = new WriteLog(this.sc, fileName)
    val importFiles = new MakeBed(this.sc, fileName, writeLog)

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
}
