package com.ets.mgl802.data

import com.ets.mgl802.data.operations.{Encoder, Converter, Bin}

import scala.math.pow

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

class SPNforFam() {
  var spnRecords = scala.collection.mutable.Buffer[Array[String]]()
  def addSPN(spnRecord: Array[String]) {
    // Code : 1,2,3,4 = A,C,G,T and 0 -> Missing
    var spnUnified = Array("0","0")
    for (i <- 0 to 1) {
      spnUnified(i) = spnRecord(i) match {
        case "0" => "0"
        case "1" => "A"
        case "2" => "C"
        case "3" => "G"
        case "4" => "T"
        case "A" => "A"
        case "C" => "C"
        case "G" => "G"
        case "T" => "T"
        case _ =>  "-1"       // Error in data
      }
    }
    //this.spnRecords.append(Array(spnRecord(0), spnRecord(1)))     // Original Data
    this.spnRecords.append(spnUnified)                              // Normalised Data
  }
  def GenerateString: String = {
    var output = ""
    this.spnRecords.foreach(rec => output = output + rec(0) + rec(1) + " ")
    return output
  }
}

class BimRecord(mapRecord: Array[String]) {
  var chronosome:String = mapRecord(0)
  var variantIdentifier:String = mapRecord(1)
  var geneticDisatance:String = mapRecord(2)
  var basepairPosition:Int = mapRecord(3).toInt
  var minorAllele:String = "-1"
  var majorAllele:String = "-1"
  def GenerateString : String = {
    this.chronosome + " " + this.variantIdentifier + " " + this.geneticDisatance + " " +
      this.basepairPosition + " " + this.minorAllele + " " + this.majorAllele
  }
  def setAlleles(alleles: Array[String]) {
    // Code : 1,2,3,4 = A,C,G,T and 0 -> Missing
    var allelesNorm = Array("0","0")
    for (i <- 0 to 1) {
      allelesNorm(i) = alleles(i) match {
        case "0" => "0"
        case "1" => "A"
        case "2" => "C"
        case "3" => "G"
        case "4" => "T"
        case "A" => "A"
        case "C" => "C"
        case "G" => "G"
        case "T" => "T"
        case _ =>  "-1"       // Error in data
      }
    }
    this.minorAllele=allelesNorm(0)
    this.majorAllele=allelesNorm(1)
  }
}

class ImportRecord() {
  var bedRecord = new BedRecordBibary()
  var bimRecords = scala.collection.mutable.Buffer[BimRecord]()
  var famRecords = scala.collection.mutable.Buffer[FamRecord]()
  var spnForFams = scala.collection.mutable.Buffer[SPNforFam]()
  def ViewContent = {
    //println("Bed Binary Records : " + this.bedRecord.bedRecordBinary.length)
    this.bedRecord.viewContent()
    println("Fam Records : " + this.famRecords.length)
    this.famRecords.foreach(record => println(record.GenerateString))
    println("SPNs : " + this.spnForFams.length)
    this.spnForFams.foreach(record => println(record.GenerateString))
    println("Bim Records : " + this.bimRecords.length)
    this.bimRecords.foreach(record => println(record.GenerateString))
  }
  // Calculation Alleles in the Bim File (A1 and A2)
  def computeBimAlleles() {
    for (i<-0 to this.bimRecords.length-1) {
      var stringAlleles = ""
      for (j<-0 to this.spnForFams.length-1) {
        if (this.spnForFams(j).spnRecords(bimRecords(i).basepairPosition-1)(0) != "0") {
          stringAlleles=stringAlleles+this.spnForFams(j).spnRecords(bimRecords(i).basepairPosition-1)(0)
        }
        if (this.spnForFams(j).spnRecords(bimRecords(i).basepairPosition-1)(1) != "0") {
          stringAlleles=stringAlleles+this.spnForFams(j).spnRecords(bimRecords(i).basepairPosition-1)(1)
        }
      }
      setBimAlleles(i, stringAlleles)
    }
  }
  // Setting of Alleles in the Bim File (A1 and A2)
  def setBimAlleles(bimRecNum : Int, stringAlleles : String) {
    // Code : 1,2,3,4 = A,C,G,T and 0 -> Missing
    // Possible caracters : A, C, G, T
    val letters = Array ("A","C","G","T")
    val countLetters = Array (0,0,0,0)
    for (i<-0 to stringAlleles.length-1){
      if (stringAlleles(i)=='A') {countLetters(0)=countLetters(0)+1}
      if (stringAlleles(i)=='C') {countLetters(1)=countLetters(1)+1}
      if (stringAlleles(i)=='G') {countLetters(2)=countLetters(2)+1}
      if (stringAlleles(i)=='T') {countLetters(3)=countLetters(3)+1}
    }
    var alleleMaj = ""
    var alleleMin = ""
    var tmpCount = 0
    for (i<-0 to 3) {
      if (countLetters(i) !=0 ) {
        if (countLetters(i)>tmpCount) {
          tmpCount=countLetters(i)
          alleleMaj=letters(i)
        }
      }
    }
    val maxCount = tmpCount
    for (i<-0 to 3) {
      if (countLetters(i) !=0 ) {
        if (countLetters(i)<=tmpCount) {
          tmpCount=countLetters(i)
          alleleMin=letters(i)
        }
      }
    }
    val minCount = tmpCount
    // For the same ordering as in PLINK
    if (minCount != maxCount) {
      this.bimRecords(bimRecNum).setAlleles(Array(alleleMin,alleleMaj))
    } else {
      this.bimRecords(bimRecNum).setAlleles(Array(alleleMaj,alleleMin))
    }
  }

  // Calculation and setting of the Bed Binary Data
  def computeBedData() {
    // The rest of the file is a sequence of V blocks of N/4 (rounded up) bytes each, where V is the number of variants and N is the number of samples.
    val variantsNb = this.bimRecords.length
    val samplesNb = this.spnForFams.length
    val bytesPerVar = samplesNb/4 +1
    val bytesNb = variantsNb * bytesPerVar
    for (i <-0 to variantsNb-1) {
      // Process for each variant : this.bimRecords(i)
      val variantAlleles = Array(this.bimRecords(i).minorAllele, this.bimRecords(i).majorAllele)

      var bytesForVariant = new Array[Byte](bytesPerVar)
      var oneByteArray = new Array[Int](8)
      var counterBite = 4
      var counterByte = 1

      for (j <- 0 to samplesNb-1) {
        // SPN on position i for individual j
        val sampleSPN = this.spnForFams(j).spnRecords(i)
        val encodedBits = Encoder.encodeSPN(variantAlleles,sampleSPN)
        oneByteArray((counterBite*2-1)-1)=encodedBits(0)
        oneByteArray((counterBite*2-1)-0)=encodedBits(1)
        counterBite=counterBite-1

        if (counterBite==0) {
          // write Byte oneByteArray as counterByte number
          // oneByteArray.foreach(bite => print(bite))
          // println()
          this.bedRecord.addByte(Converter.intArrayToByte(oneByteArray))
          // initialise Byte
          oneByteArray=Array(0,0,0,0,0,0,0,0)
          // initialise counterBite et counterByte
          counterBite=4
          counterByte=counterByte+1     // increment when 1 byte fully writed
        }
        if (j==samplesNb-1) {
          // Last value, uncompleted bytes are already nulls
          // write Byte oneByteArray as counterByte number
          // oneByteArray.foreach(bite => print(bite))
          // println()
          this.bedRecord.addByte(Converter.intArrayToByte(oneByteArray))
          counterByte=counterByte+1     // increment when 1 byte completed
          counterBite=4
        }
      }
    }
  }
}

class BedRecordBibary() {
  var bedRecordBinary = scala.collection.mutable.Buffer[Byte]()
  // The first three bytes are the magic number.
  bedRecordBinary.append(0x6c)
  bedRecordBinary.append(0x1b)
  bedRecordBinary.append(0x01)
//  Code :
//  00	Homozygous for first allele in .bim file
//  01	Missing genotype
//  10	Heterozygous
//  11	Homozygous for second allele in .bim file
  def GenerateString : String = {
    this.bedRecordBinary.toArray.toString
  }
  def viewContent() {
    println("Bed Writed Bytes : " + (this.bedRecordBinary.length-3))
    println(Bin.valueOf(bedRecordBinary))
  }
  def addBytesForVariant(bytesForVariant : Array[Byte]) {
    bytesForVariant.foreach(byte => this.bedRecordBinary.append(byte))
  }
  def addByte(byte : Byte) {
    this.bedRecordBinary.append(byte)
  }
}





