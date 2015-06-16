package com.ets.mgl804.data.operations

import scala.math._

/**
 * Created by ikizema on 15-06-09.
 */
object Converter {
  def intArrayToByte(data: Array[Int]): Byte = {
  var valueInt = 0
  for (i <- 0 to 7) {
    //println (pow(data(7-i)*2,i).toInt)
    valueInt = valueInt + pow(data(7-i)*2,i).toInt
  }
  return valueInt.toByte
  }
}
