package com.ets.mgl802.fonctions

import org.apache.spark.SparkContext

/**
 * Created by ikizema on 15-06-05.
 */

// ToDo : Write code to make file logger

class WriteLog (sc : SparkContext, filenameToLog: String) {
  def addLogLine (logLine : String) {
    System.out.println(logLine)
  }
}
