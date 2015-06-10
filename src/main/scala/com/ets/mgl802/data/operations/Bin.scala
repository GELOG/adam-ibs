package com.ets.mgl802.data.operations

/**
 * Created by ikizema on 15-06-09.
 */
// View data in Binary
object Bin {
  def valueOf (buf: scala.collection.mutable.Buffer[Byte]) = {
    if (null == buf) {
      None
    } else {
      val sb = new StringBuilder(buf.length * 2)
      for (b <- buf) {
        //sb.append("%8s".format(b & 0xFF)+" ")
        //sb.append(Integer.toBinaryString((b+256)%256)+"\n")
        sb.append(String.format("%8s",Integer.toBinaryString((b + 256) % 256)).replace(' ', '0')+" ")
      }
      sb.toString()
    }
  }
}
