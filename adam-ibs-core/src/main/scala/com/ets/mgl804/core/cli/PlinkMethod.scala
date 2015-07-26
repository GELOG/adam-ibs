package com.ets.mgl804.core.cli

/**
 * Authors: Khaled Ben Ali, Karen Mou Kui, Ivan Kizema
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

import com.ets.mgl804.core.AppContext
import com.ets.mgl804.fonctions.MakeBedPersistance.MakeBed
import com.ets.mgl804.fonctions.Genome.Genome
import org.slf4j.LoggerFactory

object PlinkMethod {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val conf = AppContext.conf;
  private val sc = AppContext.sc;
  private var inputFileName = new String()
  private var outputFileName = new String()

  // --file
  def file(name:String) {
    this.inputFileName = name
    logger.info("--file : "+this.inputFileName)
  }

  // --out
  def out(name:String) {
    this.outputFileName = name
    logger.info("--out : "+this.inputFileName)
  }

  // --make-bed
  def makeBed() {
    logger.info("--make-bed")
    val importFiles = new MakeBed(sc, this.inputFileName)
    if (this.outputFileName.isEmpty) {
      this.outputFileName=this.inputFileName
    }
    this.outputFileName=this.outputFileName+".makeBed"

    if (importFiles.loadData()) {
      // Data loaded correctly
      logger.info("--make-bed :Data load finished with success.")
      //importFiles.importRecord.ViewContent
      importFiles.persistAvro(this.outputFileName)

    } else {
      // Error in data load
      logger.error("--make-bed : Data load failed !")
    }
    sc.stop()
  }

  // --genome
  def genome(name:Unit) {
    logger.info("--genome")
    if (this.outputFileName.isEmpty) {
      this.outputFileName=this.inputFileName
    }
    this.outputFileName=this.outputFileName+".genome"
    val genomeColculations = new Genome(this.inputFileName)
    genomeColculations.computeData()
    genomeColculations.viewContent()
  }

  // --read-genome
  def readGenome() {
    logger.info("--read-genome")
  }

  // --cluster
  def cluster(name:Unit) {
    logger.info("--read-genome")
  }
}