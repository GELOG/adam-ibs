package com.ets.mgl802.core

import com.ets.mgl802.fonctions._
import org.apache.spark.{SparkContext, SparkConf}
import com.ets.mgl802.data._

/**
 * Created by ikizema on 15-05-30  : ivan.kizema at gmail.com
 */
object Main {
  val conf = new SparkConf().setAppName("Slink").setMaster("local[2]")
  val sc = new SparkContext(conf)

  def main(args: Array[String]) {
    val fileName = args(0)  // First args = filename
    val importRecord = new LoadFile(this.sc).loadFile(fileName)      // Get loaded data
    val genomeAnalyses = new Genome(importRecord)                    // Genome function with loaded data

    importRecord.ViewContent                                         // View loaded data
    sc.stop()
  }
}
