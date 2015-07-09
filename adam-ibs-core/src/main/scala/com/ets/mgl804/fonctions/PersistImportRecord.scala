package com.ets.mgl804.fonctions

import com.ets.mgl804.core.AppContext
import com.ets.mgl804.data.ImportRecord
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
  private val DATA_PATH = "DATA/avro/"
  private val fileName = filename
  private val conf = AppContext.conf;
  private val sc = AppContext.sc;
  private val sqc = new SQLContext(sc)

  def main(args: Array[String]) {


  }

  def initialiseParqurFile () {
    //Define the Users and Messages parquet file paths
    val parquetFilePath:Path = new Path(DATA_PATH, this.fileName+".parquet")
    //We must make sure that the parquet file(s) are deleted because the following script doesn't replace the file.
    deleteIfExist(parquetFilePath.getName());
  }

  /**
   * This function delete the file on the disk if it exist.
   */
  def deleteIfExist(fileName:String)
  {
    val fileTemp = new File(fileName);
    if (fileTemp.exists())
    {
      fileTemp.delete();
    }
  }

}
