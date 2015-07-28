package com.ets.mgl804.core

import org.slf4j.LoggerFactory
import com.ets.mgl804.core.cli._

/**
 * Authors: Khaled Ben Ali, Karen Mou Kui, Ivan Kizema
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

object Main {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]) {
    var argumentString = "arguments : "
    args.foreach(argument => argumentString = argumentString + argument + " ")
    this.logger.info("Begin with " + argumentString)
    val conf: ListCommandsPlink = new ListCommandsPlink(args)
    conf.args.foreach(
      arg => {
        if (arg.charAt(0)=='-') {
          arg match {
            case "--file" => PlinkMethod.file(conf.file())
            case "--out" => PlinkMethod.out(conf.out())
            case "--make-bed" => PlinkMethod.makeBed()
            case "--genome" => PlinkMethod.genome(conf.genome())
            case "--cluster" => PlinkMethod.cluster(conf.cluster())
            case "--show-parquet" => PlinkMethod.showParquet()
            case _ => println(conf.help)
          }
        }
      }
    )
  }

}
