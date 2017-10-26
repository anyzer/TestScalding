import com.twitter.scalding._

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
  TextLine(args("input"))
    .flatMap('line -> 'word) {line : String => line.toLowerCase.split("\\s+")}
    .groupBy('word) {_.size}
    .write(Tsv(args("output")))
}
