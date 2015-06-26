package com.ets.mgl804.cli

/**
 * Created by karim on 26/06/15.
 */
/**
 * Authors: Khaled Ben Ali et Karen Mou Kui
 * Date: 15-06-2015
 *
 * Class which implement all methods called with the command line
 *
 */


object PlinkMethod {

  def file(name:String) : Unit = {
    println("file with filename: " + name)
  }



  def makeBed(name:List[String]) : Unit = {
    println("make bed with filename: " + name)
  }



  def genome(name:Unit) : Unit = {
    println("genome")
  }

  def readGenome(name:String) : Unit = {
    println("read-genome with filename: " + name)
  }

  def cluster(name:Unit) : Unit = {
    println("cluster")
  }
}