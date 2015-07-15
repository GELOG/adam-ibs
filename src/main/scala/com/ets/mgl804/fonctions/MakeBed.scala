package com.ets.mgl804.fonctions

import com.ets.mgl804.data.{BimRecord, SPNforFam, FamRecord, ImportRecord}
import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory

/**
 * Created by ikizema on 15-06-05.
 */
class MakeBed(sc : SparkContext, fileToLoad: String) {

  val textFileMap = sc.textFile(fileToLoad + ".map")
  val textFilePed = sc.textFile(fileToLoad + ".ped")
  val importRecord = new ImportRecord()

  def logb = LoggerFactory.getLogger(this.getClass())

  def loadData() : Boolean = {

    var dataCorrect = true
    val variantsFound = this.getVariantsNum()
    val individualsFound = this.getIndividualsNum()(0)

    // Check .map
    if (variantsFound<0) {
      dataCorrect=false
    } else {
      logb.info("Variants found : "+variantsFound.toString+".")
     // writeLog.addLogLine("Variants found : "+variantsFound.toString+".")
    }
    // Check .ped
    if (individualsFound < 0) {
      dataCorrect=false
    } else {
     // writeLog.addLogLine("Individuals found : "+individualsFound.toString+".")
      logb.info("Individuals found : "+individualsFound.toString+".")
    }

    this.importRecord.computeBimAlleles()
    this.importRecord.computeBedData()
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
          // writeLog.addLogLine
          logb.error("ERROR : .map file line " + (lineMapNum + 1))
          return -1
        } else {
          numberVariants = numberVariants + 1
          processMapLine(lineMap.drop(1))
        }
      } else {
        if (lineMap.length != 4) {
          // writeLog.addLogLine
          logb.error("ERROR : .map file line " + lineMapNum)
          return -1
        } else {
          numberVariants = numberVariants + 1
          processMapLine(lineMap)
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
          processPedLine(linePed.drop(1))
          numberIndividuals = numberIndividuals + 1
        } else {
          numberSPNsIndividial=(linePed.length-6)
          processPedLine(linePed)
          numberIndividuals = numberIndividuals + 1
        }
        if (numberSPNs == -1) {
          numberSPNs = numberSPNsIndividial
        } else if(numberSPNs != numberSPNsIndividial) {
          // writeLog.addLogLine
          logb.error("ERROR : Not same number of SPNs Individual in .ped file line " + lineNum)
          return Array(-1,-1)
        }
        //numberIndividuals = numberIndividuals + 1
      }
    }

    return Array(numberIndividuals,numberSPNs/2)
  }

  // Processing one line of .ped
  def processPedLine(linePed : Array[String]) {
    this.importRecord.famRecords.append(new FamRecord(linePed.take(6)))
    val lineSPNs = linePed.drop(6)
    this.importRecord.spnForFams.append(new SPNforFam())
    for (spn <- 0 to lineSPNs.length/2-1) {
      this.importRecord.spnForFams.last.addSPN(Array(lineSPNs(spn*2),lineSPNs(spn*2+1)))
    }
  }

  def processMapLine(lineMap : Array[String]) {
    this.importRecord.bimRecords.append(new BimRecord(lineMap))
  }
}