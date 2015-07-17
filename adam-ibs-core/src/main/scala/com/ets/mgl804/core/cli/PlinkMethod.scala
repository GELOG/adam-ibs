package com.ets.mgl804.core.cli

/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

import com.ets.mgl804.core.AppContext
import com.ets.mgl804.fonctions.MakeBedPersistance.MakeBed
import com.ets.mgl804.fonctions._

object PlinkMethod {
  val conf = AppContext.conf;
  val sc = AppContext.sc;
  var inputFileName = new String()
  var outputFileName = new String()

  // --file
  def file(name:String) {
    this.inputFileName = name
  }

  // --out
  def out(name:String) {
    this.outputFileName = name
  }

  // --make-bed
  def makeBed() {
    val writeLog    = new WriteLog(sc, this.inputFileName)
    val importFiles = new MakeBed(sc, this.inputFileName, writeLog)

    if (importFiles.loadData()) {
      // Data loaded correctly
      writeLog.addLogLine("Data load finished with success.")
      importFiles.importRecord.ViewContent
      importFiles.persistAvro(this.outputFileName)

    } else {
      // Error in data load
      writeLog.addLogLine("ERROR : Data load failed.")
    }
    sc.stop()
  }

  // --genome
  def genome(name:Unit) : Unit = {
    println("genome")
  }

  // --read-genome
  def readGenome() : Unit = {
    println("read-genome")
  }

  // --cluster
  def cluster(name:Unit) : Unit = {
    println("cluster")
  }
}