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


  // Upload/Delete File
  val testfileName = "input.txt"
  val testfilePath = "src/main/resources/"

  val destinationPath = "/"

  HDFSFileService.write(testfilePath + testfileName, destinationPath)

  HDFSFileService.deleteFile("/input.txt")


}
