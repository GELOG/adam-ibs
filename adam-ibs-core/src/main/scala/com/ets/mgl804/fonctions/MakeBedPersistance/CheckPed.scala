package com.ets.mgl804.fonctions.MakeBedPersistance

import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory

/**
 * Created by Ivan Kizema on 15-06-05.
 *
 * Class make a check of coherence of input .ped and .map files.
 */


class CheckPed(sc : SparkContext, fileToLoad: String) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val textFileMap = sc.textFile(fileToLoad + ".map")
  private val textFilePed = sc.textFile(fileToLoad + ".ped")

  def checkData() : Boolean = {
    var dataCorrect = true
    // Check .map
    if (getVariantsNum()<0) {
      dataCorrect=false
    } else {
      logger.info("Variants found : "+getVariantsNum.toString)
    }
    // Check .ped
    if (getIndividualsNum()(0) < 0) {
      dataCorrect=false
    } else {
      logger.info("Individuals found : "+getIndividualsNum()(0).toString+". "+getIndividualsNum()(1).toString+" SPNs each.")
    }

    dataCorrect;
  }

  def getVariantsNum(): Int = {
    val numberMapEntries: Int = this.textFileMap.count().toInt
    //writeLog.addLogLine("Lines in .map file : "+numberMapEntries.toString)
    var numberVariants = 0
    // Verify if each line is consistent
    for (lineMapNum <- 0 to numberMapEntries-1) {
      // Each line should have the same ammount of information (CH, varID, genDist, b-pPos)
      val lineMap = textFileMap.collect()(lineMapNum).split("\\s+")
      if (lineMap(0) == "#") {
        // comment line
      } else if (lineMap(0) == "") {
        // not count 1st char
        if (lineMap.length != 5) {
          logger.error(".map file line " + (lineMapNum + 1))
          return -1
        } else {
          numberVariants = numberVariants + 1
        }
      } else {
        if (lineMap.length != 4) {
          logger.error(".map file line " + lineMapNum)
          return -1
        } else {
          numberVariants = numberVariants + 1
        }
      }
    }
    return numberVariants
  }

  def getIndividualsNum () : Array[Int] = {
    val numberPedEntries: Int = this.textFilePed.count().toInt
    var numberIndividuals = 0
    var numberSPNs = -1

    // Verify if each line is consistent
    for (lineNum <- 0 to numberPedEntries-1) {
      var numberSPNsIndividial = 0
      val linePed = textFilePed.collect()(lineNum).split("\\s+")
      if (linePed(0) == "#") {
        // comment line
      } else {
        if (linePed(0) == "") {
          // not count 1st char
          numberSPNsIndividial=(linePed.length-6-1)
        } else {
          numberSPNsIndividial=(linePed.length-6)
        }
        if (numberSPNs == -1) {
          numberSPNs = numberSPNsIndividial
        } else if(numberSPNs != numberSPNsIndividial) {
          logger.error("Not same number of SPNs Individual in .ped file line " + lineNum)
          return Array(-1,-1)
        }
        numberIndividuals = numberIndividuals + 1
      }
    }

    return Array(numberIndividuals,numberSPNs/2)
  }
}