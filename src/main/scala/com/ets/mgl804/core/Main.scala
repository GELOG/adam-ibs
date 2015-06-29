package com.ets.mgl804.core

import com.ets.mgl804.cli._
import org.rogach.scallop._

/**
 * Created by ikizema on 15-05-30  : ivan.kizema at gmail.com
 */
object Main {


  def main(args: Array[String]) {

    val conf: ListCommandsPlink = new ListCommandsPlink(args)

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
