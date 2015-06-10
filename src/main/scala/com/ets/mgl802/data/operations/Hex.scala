package com.ets.mgl802.data.operations

/**
 * Created by ikizema on 15-06-09.
 */
// View data in Haxadecomal
object Hex {
  def valueOf (buf: scala.collection.mutable.Buffer[Byte]) = {
    if (null == buf) {
      None
    } else {
      val sb = new StringBuilder(buf.length * 2)
      for (b <- buf) {
        sb.append("%02X".format(b & 0xff)+" ")
      }
      sb.toString()
    }
  }
}
