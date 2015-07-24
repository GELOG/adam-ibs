package com.ets.mgl804.persistance

import com.ets.mgl804.core.AppContext
import org.apache.avro.generic.{IndexedRecord, GenericRecord}
import org.apache.avro.specific.SpecificDatumReader
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.bdgenomics.formats.avro._
import parquet.avro.{AvroParquetWriter, AvroParquetReader}


/**
 * Created by ikizema on 17/07/15.
 */
class ParquetLoader(filename:String) {
  private val DATA_PATH = "DATA/avro/"
  private val fileName = filename
  private val conf = AppContext.conf
  private val sc = AppContext.sc
  private val sqc = new SQLContext(sc)
  sqc.sql("SET spark.sql.parquet.binaryAsString=true")
  private val parquetFilePath:Path = new Path(this.fileName)
  private val individualsDataFrame:DataFrame = sqc.read.parquet(parquetFilePath.toString())


  def showContent(): Unit = {
    System.out.println("******************************************************************");
//    val individualsDataFrame:DataFrame = sqc.read.parquet(parquetFilePath.toString())
//    individualsDataFrame.printSchema()
//    individualsDataFrame.show()

    System.out.println("******************************************************************");
    val parquetReader = new AvroParquetReader[IndexedRecord](parquetFilePath)
    val individual:Individual = parquetReader.read().asInstanceOf[Individual]
    val individual2:Individual = parquetReader.read().asInstanceOf[Individual]
    System.out.println(individual.getIndividualId)
    System.out.println(individual2.getGenotype)

//    val individual = individualsDataFrame.first().getAs[Individual](1)
//    individuals.foreach(row => System.out.println(row.asInstanceOf[Individual].toString))
//    System.out.println()


//    val reader: AvroParquetReader[GenericRecord] = new AvroParquetReader[GenericRecord](parquetFilePath)
//    val individual = reader.asInstanceOf[Individual]
//    System.out.println(individual)
//
//    val nextRecord: GenericRecord = reader.read()
//    System.out.println(nextRecord.toString)
////    val nextRecord: GenericRecord = reader.read()
////    System.out.println(nextRecord.toString)
//    System.out.println("******************************************************************");
//    val individual = nextRecord.get(0).asInstanceOf[Individual]
//    System.out.println(individual.toString)

    System.out.println("******************************************************************");

//    val reader = new SpecificDatumReader[Individual](scala.reflect.classTag[Individual].runtimeClass.asInstanceOf[Class[Individual]])
//    val reads = ac.loadAlignments(

    System.out.println("******************************************************************");
  }

}
