import com.twitter.scalding._
import java.io._
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by guoch on 19/10/17.
  */

class WordCountJob(args : Args) extends Job(args) {

  // Count words
  TextLine(args("input"))
    .flatMap('line -> 'word) {line : String => line.toLowerCase.split("\\s+")}
    .groupBy('word) {_.size}
    .write(Tsv(args("output")))


  // Upload File
//  val testfileName = "input.txt"
//  val testfilePath = "src/main/resources/"
//
//  val destinationPath = "/"
//
//  HDFSFileService.write(testfilePath + testfileName, destinationPath)
//
//  HDFSFileService.deleteFile("/input.txt")

  //Spark
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Word Count")
  val sc = new SparkContext(conf)

  val textFile = sc.textFile("hdfs:///tmp/shakespeare.txt")

  val counts = textFile.flatMap(line => line.split(" "))
    .map(word => (word, 1))
    .reduceByKey(_ + _)

  counts.foreach(println)
  println("Total Words: " + counts.count())
  counts.saveAsTextFile("hdfs:///tmp/shakespeareWordCount")

}
