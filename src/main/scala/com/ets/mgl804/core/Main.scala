package com.ets.mgl804.core

import com.ets.mgl804.fonctions._
import com.ets.mgl804.data._
import com.ets.mgl804.cli._
import org.apache.spark.{SparkContext, SparkConf}
/**
 * Created by ikizema on 15-05-30  : ivan.kizema at gmail.com
 */
object Main {


  def main(args: Array[String]) {

      if(args(0) == "test_spark" ) {

        val fileName = args(1) // First args = filename

        val conf = new SparkConf().setAppName("Slink").setMaster("local")
        val sc   = new SparkContext(conf)

        val writeLog    = new WriteLog(sc, fileName)
        val importFiles = new MakeBed(sc, fileName, writeLog)

        if (importFiles.loadData()) {
          // Data loaded correctly
          writeLog.addLogLine("Data load finished with success.")
          importFiles.importRecord.ViewContent
        } else {
          // Error in data load
          writeLog.addLogLine("ERROR : Data load failed.")
        }

        sc.stop()
      }
      else if(args(0) == "test_cli" ) {

        val conf: ListCommandsPlink = new ListCommandsPlink(args)

        conf.args(1) match {
          case "--file" => PlinkMethod.file(conf.file())
          case "--make-bed" => PlinkMethod.makeBed(conf.makeBed())
          case "--genome" => PlinkMethod.genome(conf.genome())
          case "--cluster" => PlinkMethod.cluster(conf.cluster())
          case "--read-genome" => PlinkMethod.readGenome(conf.readGenome())
          case _ => println(conf.help)
        }
      }
      else {

        System.out.println( "1er param inconnu" )
      }
    }
}
