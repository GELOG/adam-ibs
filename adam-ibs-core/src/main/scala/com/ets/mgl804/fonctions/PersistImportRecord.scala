package com.ets.mgl804.fonctions

import com.ets.mgl804.core.AppContext
import com.ets.mgl804.data._
import java.io.File
import org.apache.avro.generic.IndexedRecord
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import com.ets.mgl804.avrotest.{Message, User}
import org.bdgenomics.formats.avro._
import parquet.avro.AvroParquetWriter

/**
 * Created by ikizema on 15-07-03.
 */
class PersistImportRecord(importRecord: ImportRecord, filename: String) {
  //This is the path where Parquet files will be created
  private val originalPlinkRecord = importRecord;
  private val DATA_PATH = "DATA/avro/";
  private val fileName = filename;
  private val conf = AppContext.conf;
  private val sc = AppContext.sc;
  private val sqc = new SQLContext(sc);

  def main(args: Array[String]) {

  }

  def persistData() {
    val parquetFilePath = initialiseParqurFile();
    writeToFile(parquetFilePath);
    readFromFile(parquetFilePath);
  }

  def readFromFile(parquetFilePath:Path) {
    val individualsDataFrame:DataFrame = sqc.read.parquet(parquetFilePath.toString())
    println("******************************************************************");
    individualsDataFrame
      //.select("familyId","individualId","paternalId","maternalId","sex","phenotype","genotype")
      .show()
    System.out.println("******************************************************************");
  }

  def writeToFile(parquetFilePath:Path) {
    System.out.println("PersistImportRecord writeToFile()")
    val parquetWriter = new AvroParquetWriter[IndexedRecord](parquetFilePath, Individual.getClassSchema())

    // Begin with BIM records (Calculated Variants)
    var variants = scala.collection.mutable.Buffer[Variant]()
    // Load to variants
    this.originalPlinkRecord.bimRecords.foreach(bimRecord => variants.append(createVariant(bimRecord)))
    // Write to Avro
    variants.foreach(variant => parquetWriter.write(variant))

    // Begin with FAM records (Individuals)
    var individuals = scala.collection.mutable.Buffer[Individual]()
    // Load to individuals
    this.originalPlinkRecord.famRecords.foreach(famRecord => individuals.append(createIndividual(famRecord, variants)))
    // Write to Avro
    individuals.foreach(individual => parquetWriter.write(individual))

    // Close parquetWriter
    parquetWriter.close()
  }

  def createVariant(bimRecord:BimRecord) : Variant = {
    var variant = new Variant()
    var contig = new Contig()
    contig.setContigName(bimRecord.getChronosome())
    variant.setContig(contig)
    variant.setStart(bimRecord.getBasepairPosition())
    variant.setEnd(bimRecord.getBasepairPosition())
    variant.setReferenceAllele(bimRecord.getMajorAllele())
    variant.setAlternateAllele(bimRecord.getMinorAllele())
    System.out.println(variant.toString())
    return variant
  }

  def createIndividual(famRecord:FamRecord, variants:scala.collection.mutable.Buffer[Variant]) : Individual = {
    var individual = new Individual()
    individual.setFamilyId(famRecord.getIdFamily())
    individual.setIndividualId(famRecord.getIdIndividual())
    individual.setMaternalId(famRecord.getIdMaternal())
    individual.setPaternalId(famRecord.getIdPaternal())
    val sex = famRecord.getSexe();
    if (sex=="1") {
      individual.setSex(Sex.Male)
    } else if (sex=="2") {
      individual.setSex(Sex.Female)
    } else {
      individual.setSex(Sex.Unknown)
    }
    var phenotype = new Phenotype()
    phenotype.setPhenotype(famRecord.getPhenotype())
    individual.setPhenotype(phenotype)
    // Create Genotype
    var genotype = new Genotype()


    System.out.println(individual.toString())
    return individual;
  }

  def initialiseParqurFile() : Path= {
    //Define the Users and Messages parquet file paths
    val parquetFilePath:Path = new Path(DATA_PATH, this.fileName+".parquet")
    //We must make sure that the parquet file(s) are deleted because the following script doesn't replace the file.
    deleteIfExist(parquetFilePath.getName());
    return parquetFilePath;
  }

  /**
   * This function delete the file on the disk if it exist.
   */
  def deleteIfExist(fileName:String)
  {
    var fileTemp = new File(DATA_PATH+fileName);
    if (fileTemp.exists())
    {
      fileTemp.delete();
    }
  }

}
