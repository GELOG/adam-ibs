package com.ets.mgl804.cli

/**
 * Created by karim on 26/06/15.
 */
/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 *
 * Class declaring all implemented commands
 *
 * See https://github.com/scallop/scallop for more explication about the CLI parser
 */

import org.rogach.scallop._

// List of commands used
class ListCommandsPlink(arguments: Seq[String]) extends ScallopConf(arguments) {

  val file        = opt[String](  "file",
                                  argName = "name",
                                  noshort = true,
                                  descr = "Specify .ped + .map filename prefix (default 'plink')"
                            )

  val makeBed     = opt[List[String]](  "make-bed",
                                        argName = "prefix name",
                                        noshort = true,
                                        descr = "Create a new binary fileset. Specify .ped and .map files"
                                      )

  val genome      = opt[Boolean]( "genome",
                                  noshort = true,
                                  descr = "Calculate IBS distances between all individuals"
                                )

  val readGenome  = opt[String](  "read-genome",
                                  argName = "file name",
                                  noshort = true,
                                  descr = "Command can directly read compressed file"
                                )

  val cluster     = opt[Boolean]( "cluster",
                                  noshort = true,
                                  descr = "Cluster samples using a pairwise similarity statistic (normally IBS)"
                                )

  val help        = opt[Int]( "help",
                              short = 'h',
                              descr = "Show help message"
                            )
}