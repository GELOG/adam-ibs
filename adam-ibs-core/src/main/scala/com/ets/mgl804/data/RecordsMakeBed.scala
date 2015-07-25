package com.ets.mgl804.data

import com.ets.mgl804.data.operations.{Encoder, Converter, Bin}
import org.bdgenomics.formats.avro.Base
import org.slf4j.LoggerFactory

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
  def getIdFamily() : CharSequence = {
    return this.idFamily
  }
  def getIdIndividual() : CharSequence = {
    return this.idIndividual
  }
  def getIdPaternal() : CharSequence = {
    return this.idPaternal
  }
  def getIdMaternal() : CharSequence = {
    return this.idMaternal
  }
  def getSexe() : String = {
    return this.sexe
  }
  def getPhenotype() : CharSequence = {
    return this.phenotype
  }
}

// New SPNforFam using Enum base
class SpnFamBase() {
  var spnRecords = scala.collection.mutable.Buffer[Array[Base]]()

  def addSPN(spnRecord: Array[String]) {
    // Code : 1,2,3,4 = A,C,G,T and 0 -> Missing
    var spnUnified = new Array[Base](2)
    for (i <- 0 to 1) {
      if (spnRecord(i) == "0" ){
        spnUnified(i)=Base.Miss
      } else if (spnRecord(i) == "1" ){
        spnUnified(i)=Base.A
      } else if (spnRecord(i) == "2" ){
        spnUnified(i)=Base.C
      } else if (spnRecord(i) == "3" ){
        spnUnified(i)=Base.G
      } else if (spnRecord(i) == "4" ){
        spnUnified(i)=Base.T
      } else if (spnRecord(i) == "A" ){
        spnUnified(i)=Base.A
      } else if (spnRecord(i) == "C" ){
        spnUnified(i)=Base.C
      } else if (spnRecord(i) == "G" ){
        spnUnified(i)=Base.G
      } else if (spnRecord(i) == "T" ){
        spnUnified(i)=Base.T
      } else {
        spnUnified(i)=Base.Error
      }
    }
    //this.spnRecords.append(Array(spnRecord(0), spnRecord(1)))     // Original Data
    this.spnRecords.append(spnUnified)                              // Normalised Data
  }
  def getSpnNum(position:Int): Array[Base] = {
    return this.spnRecords(position)
  }
  def getLengh : Int = {
    return this.spnRecords.length
  }

  def getSpnNumList(position:Int): List[Base] = {
    return List(this.spnRecords(position)(0), this.spnRecords(position)(1))
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
  def getChronosome() : CharSequence = {
    return this.chronosome
  }
  def getVariantIdentifier() : CharSequence = {
    return this.variantIdentifier
  }
  def getGeneticDisatance() : CharSequence = {
    return this.geneticDisatance
  }
  def getBasepairPosition() : Long = {
    return this.basepairPosition
  }
  def getMinorAllele() : CharSequence = {
    return this.minorAllele
  }
  def getMajorAllele() : CharSequence = {
    return this.majorAllele
  }
}

class ImportRecord() {
  private val logger = LoggerFactory.getLogger(this.getClass)
  var bedRecord = new BedRecordBibary()
  var bimRecords = scala.collection.mutable.Buffer[BimRecord]()
  var famRecords = scala.collection.mutable.Buffer[FamRecord]()
  var spnForFams = scala.collection.mutable.Buffer[SpnFamBase]()

  def getSnpIndividual(individualNum : Int) : SpnFamBase = {
    return this.spnForFams(individualNum)
  }

  def ViewContent() = {
    this.bedRecord.viewContent()
    logger.debug("Fam Records : " + this.famRecords.length)
    this.famRecords.foreach(record => logger.debug(record.GenerateString))
    logger.debug("SPNs : " + this.spnForFams.length)
    this.spnForFams.foreach(record => logger.debug(record.GenerateString))
    logger.debug("Bim Records : " + this.bimRecords.length)
    this.bimRecords.foreach(record => logger.debug(record.GenerateString))
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
          this.bedRecord.addByte(Converter.intArrayToByte(oneByteArray))
          // initialise Byte
          oneByteArray=Array(0,0,0,0,0,0,0,0)
          // initialise counterBite et counterByte
          counterBite=4
          counterByte=counterByte+1     // increment when 1 byte fully writed
        }
        if (j==samplesNb-1) {
          this.bedRecord.addByte(Converter.intArrayToByte(oneByteArray))
          counterByte=counterByte+1     // increment when 1 byte completed
          counterBite=4
        }
      }
    }
  }
}

class BedRecordBibary() {
  private val logger = LoggerFactory.getLogger(this.getClass)
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
    logger.debug("Bed Writed Bytes : " + (this.bedRecordBinary.length-3))
    logger.debug(Bin.valueOf(bedRecordBinary).toString)
  }
  def addBytesForVariant(bytesForVariant : Array[Byte]) {
    bytesForVariant.foreach(byte => this.bedRecordBinary.append(byte))
  }
  def addByte(byte : Byte) {
    this.bedRecordBinary.append(byte)
  }
}





