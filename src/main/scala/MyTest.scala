import com.twitter.scalding._
import java.io._

import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by guoch on 19/10/17.
  */
//
//object MyTest extends App {
//
//  println("Hello, World")
//
//
//}

class WordCountJob(args : Args) extends Job(args) {

  // Count words
  TextLine(args("input"))
    .flatMap('line -> 'word) {line : String => line.toLowerCase.split("\\s+")}
    .groupBy('word) {_.size}
    .write(Tsv(args("output")))

//  // Upload File to HDFS
  val testfileName = "src/main/resources/input.txt"
  val testText = "Example text"

  val testfile = new File(testfileName)

  HDFSFileService.removeFile(testfileName)
  HDFSFileService.saveFile(testfileName)

  // Download File from HDFS
//  val downloadFile = "download.txt"
//  val outputStream = new FileOutputStream(new File(downloadFile))
//  val in = HDFSFileService.getFile("shakespeare.txt")
//  var b = new Array[Byte](1024)
//  var numBytes = in.read(b)
//
//  while (numBytes > 0) {
//    outputStream.write(b, 0, numBytes)
//    numBytes = in.read(b)
//  }
//  outputStream.close()
//  in.close()

}
