package com.ets.mgl804.core

import com.ets.mgl804.core.cli._

/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */

object Main {

  def main(args: Array[String]) {
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
            case "--read-genome" => PlinkMethod.readGenome()
            case _ => println(conf.help)
          }
        }
      }
    )
  }

}
