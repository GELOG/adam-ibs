package com.ets.mgl804.core

import com.ets.mgl804.cli._
import org.rogach.scallop._
import org.apache.log4j.PropertyConfigurator
import org.slf4j.{LoggerFactory, Logger}
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter

/**
 * Created by ikizema on 15-05-30  : ivan.kizema at gmail.com
 */
object Main {


  def main(args: Array[String]) {

    // PropertyConfigurator.configure("src/main/resources/log4j.properties")

    val conf: ListCommandsPlink = new ListCommandsPlink(args)

    def logger = LoggerFactory.getLogger(this.getClass())

    logger.info("Args(0) : " + args(0) )

    conf.args(0) match {
        case "--file" => PlinkMethod.file(conf.file())
        case "--make-bed" => PlinkMethod.makeBed(conf.makeBed())
        case "--genome" => PlinkMethod.genome(conf.genome())
        case "--cluster" => PlinkMethod.cluster(conf.cluster())
        case "--read-genome" => PlinkMethod.readGenome(conf.readGenome())
        case _ => println(conf.help)
      }
  }
}
