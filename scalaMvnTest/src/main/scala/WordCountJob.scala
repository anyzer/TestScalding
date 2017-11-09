/**
  * Created by guoch on 2/11/17.
  */
import com.twitter.scalding._
import com.twitter.scalding.typed.TypedSink

import scala.io.Source

class WordCountJob(args : Args) extends Job(args) {

  val lines : TypedPipe[String] = TypedPipe.from(TextLine(args("input")))

  lines.flatMap(_.split("\\s+"))
    .groupBy { identity }
    .size
    .toTypedPipe
    .write(TypedTsv[(String, Long)]("output"))


//  TextLine(args("input"))//can also create sources that read directly from compressed files on HDFS
//      .flatMap('line -> 'word) {line : String => line.replaceAll("[,.]", "").toLowerCase.split("\\s+")}
//      .groupBy('word) {_.size}
//      .write(Tsv(args("output")))




//https://gist.github.com/johnynek/a47699caa62f4f38a3e2
  //Word count for alice, and rank top words


}
