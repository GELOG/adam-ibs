package com.ets.mgl804.persistance

import com.ets.mgl804.core.AppContext
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificDatumReader
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.bdgenomics.formats.avro._
import parquet.avro.AvroParquetReader


/**
 * Created by ikizema on 17/07/15.
 */
class ParquetLoader(filename:String) {
  private val DATA_PATH = "DATA/avro/"
  private val fileName = filename
  private val conf = AppContext.conf
  private val sc = AppContext.sc
  private val sqc = new SQLContext(sc)
  private val parquetFilePath:Path = new Path(this.fileName)
  private val individualsDataFrame:DataFrame = sqc.read.parquet(parquetFilePath.toString())


  def showContent(): Unit = {
    System.out.println("******************************************************************");
    val individualsDataFrame:DataFrame = sqc.read.parquet(parquetFilePath.toString())
    individualsDataFrame.printSchema()
    individualsDataFrame.show()
    System.out.println(individualsDataFrame.collect().apply(0).asInstanceOf[Individual].toString)
    System.out.println("******************************************************************");

//    val reader: AvroParquetReader[GenericRecord] = new AvroParquetReader[GenericRecord](parquetFilePath)
////    val individual = reader.asInstanceOf[Individual]
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
