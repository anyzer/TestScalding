import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by guoch on 31/10/17.
  */
object Main {

  def main(args: Array[String]) {

    //Create a SparkContext to initialize Spark
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

}
