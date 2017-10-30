import com.twitter.scalding._

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

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

  private val conf = new Configuration()


  TextLine(args("input"))
    .flatMap('line -> 'word) {line : String => line.toLowerCase.split("\\s+")}
    .groupBy('word) {_.size}
    .write(Tsv(args("output")))

}
