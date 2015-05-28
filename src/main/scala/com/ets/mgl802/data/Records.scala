package com.ets.mgl802.data

/**
 * Created by ikizema on 15-05-24 : ivan.kizema at gmail.com
 */
class FamRecord(famRecord: Array[String]) {
  var idFamily = famRecord(0)
  var idIndividual = famRecord(1)
  var idPaternal = famRecord(2)
  var idMaternal = famRecord(3)
  var sexe = famRecord(4)                //(1=male; 2=female; other=unknown)
  var phenotype = famRecord(5)
  def GenerateString : String = {
    this.idFamily + " " + this.idIndividual + " " + this.idPaternal + " " +
      this.idMaternal + " " + this.sexe + " " + this.phenotype
  }
}

class BedRecord(bedRecord: Array[String]) {
  var idIndividual = bedRecord(0)
  var variantIdentifier:String = bedRecord(1)
  var genotypeAllele1 = bedRecord(2)
  var genotypeAllele2 = bedRecord(3)
  def GenerateString : String = {
    this.idIndividual + " " + this.variantIdentifier + " " + this.genotypeAllele1 + " " +
      this.genotypeAllele2
  }
}

class BimRecord(mapRecord: Array[String]) {
  var chronosome = mapRecord(0)
  var variantIdentifier:String = mapRecord(1)
  var geneticDisatance = mapRecord(2)
  var basepairPosition = mapRecord(3)
  //var referenceAllele:Int = mapRecord(4).toInt
  //var alternateAllele:Int = mapRecord(5).toInt
  def GenerateString : String = {
    this.chronosome + " " + this.variantIdentifier + " " + this.geneticDisatance + " " +
    this.basepairPosition
  }
}

class ImportRecord {
  var bedRecords = scala.collection.mutable.Buffer[BedRecord]()
  var bimRecords = scala.collection.mutable.Buffer[BimRecord]()
  var famRecords = scala.collection.mutable.Buffer[FamRecord]()
  def ViewContent = {
    println("Bed Records : " + this.bedRecords.length)
    this.bedRecords.foreach(record => println(record.GenerateString))
    println("Fam Records : " + this.famRecords.length)
    this.famRecords.foreach(record => println(record.GenerateString))
    println("Bim Records : " + this.bimRecords.length)
    this.bimRecords.foreach(record => println(record.GenerateString))
  }
}