package com.ets.mgl802.load

import com.ets.mgl802.data._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * Created by ikizema on 15-05-24 : ivan.kizema at gmail.com
 */
object LoadFile {
  val conf = new SparkConf().setAppName("Slink").setMaster("local")
  val sc = new SparkContext(conf)
  var importRecord: ImportRecord = new ImportRecord

  def main(args: Array[String]): Unit = {
    val fileName = args(0)  // First args = filename
    loadFile(fileName)

    testLoad()
    sc.stop()
  }

  def loadFile(fileToLoad: String) {
    val textFileMap = sc.textFile(fileToLoad+".map")       // loading .map
    val textFilePed = sc.textFile(fileToLoad+".ped")       // loading .ped
    // Check file
    if (textFileMap.count()==0 || textFilePed.count()==0) {
      println("Error load data : any data in files.")
    } else {
      // At least 1 line in files
      var numberMapEntries: Int =textFileMap.count().toInt
      var numberPedEntries: Int =textFilePed.count().toInt

      // Generation FAM Record
      for (linePed <- 0 to numberPedEntries-1) {
        val linePedOrganised=textFilePed.collect()(linePed).split("\\s+")
        this.importRecord.famRecords.append(new FamRecord(linePedOrganised.take(6)))
      }

      // Generation BIM Record
      for (lineMap <- 0 to numberMapEntries-1) {
        val lineMapOrganised=textFileMap.collect()(lineMap).split("\\s+")
        this.importRecord.bimRecords.append(new BimRecord(lineMapOrganised))
      }

      // Generation BED Record
      for (linePed <- 0 to numberPedEntries-1) {
        for (lineMap <- 0 to numberMapEntries-1) {
          //println(textFileMap.collect()(line))      //get line by line
          loadOneLine(textFileMap.collect()(lineMap), textFilePed.collect()(linePed)) //process line by line
        }
      }
    }
  }

  def loadOneLine(lineMap: String, linePed: String){
    val lineMapOrganised=lineMap.split("\\s+")
    //this.importRecord.bimRecords.append(new BimRecord(lineMapOrganised))
    val linePedOrganised=linePed.split("\\s+")
    //this.importRecord.famRecords.append(new FamRecord(linePedOrganised.take(6)))    // 6 first values from linePed
    var dataPedGenotype = linePedOrganised.drop(6)                                  // On enleve les 6 premieres valeures
    if (dataPedGenotype.length>0) {
      for (numSPN <- 0 to (dataPedGenotype.length/2)-1) {
        var bedRecordArgs:Array[String] = new Array[String](4)
        bedRecordArgs(0)=linePedOrganised(1)
        bedRecordArgs(1)=lineMapOrganised(1)
        bedRecordArgs(2)=dataPedGenotype(numSPN*2)
        bedRecordArgs(3)=dataPedGenotype(numSPN*2+1)
        this.importRecord.bedRecords.append(new BedRecord(bedRecordArgs))
      }
    }
  }

  def testLoad() {
    this.importRecord.ViewContent
  }
}

// Syntaxe interessante :
//string.split("\\s+").foreach(mot => println(mot))     -> Separe toues les mots separes par un ou plusieurs " "
