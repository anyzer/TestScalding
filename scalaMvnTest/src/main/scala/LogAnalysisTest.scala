import com.twitter.scalding._

/**
  * Created by guoch on 10/11/17.
  * java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool LogAnalysisTest --local --input src/main/resources/graph.tsv --output output.txt -Xmx1024m
  */
class LogAnalysisTest(args : Args) extends Job(args) {

  val logSchema = List ('datetime, 'user, 'activity, 'data, 'session, 'position, 'response, 'device, 'error, 'location)

//  val myProduct = Csv("src/main/resources/23.log").read
//    .write(Tsv("log.tsv"))

  val path = "src/main/resources/log-files/2017/11/"
//  val logs = MultipleTsvFiles(List(path + "22.log", path + "23.log"), logSchema )
//  val logs = Tsv(path, logSchema ) //above two cannot work on local model
  val logs = Tsv(path + "22.log", logSchema )

  logs.read
    .filter('activity) { x:String => x=="login" }
    .write(Tsv("log-files-just-logins"))

  val sliced_logs = logs.project('location, 'device)
    .write(Tsv("logins-important-info"))

  //In this specific example, if input data of 5 GB is stored in a single file in a Hadoop cluster
  // that uses a block size of 128 MB, then in total, 40 blocks of input data exist.
  // For every block of data, one map task will be spawned containing our entire Scalding application logic.

  val longitude = logs.map('location -> 'lat) {x:String => getPosition(x, 0)}
      .map('location -> 'lon) {x:String => getPosition(x, 1)}
      .groupBy('lat, 'lon, 'device){group => group.size('count)}

    longitude.write (Tsv("groupByDeviceAndLocation"))

  def getPosition(p:String, pType:Int):String = {
    val position = p.split(",").toList

    val lat = "%4.2f" format position.get(pType).toString.toFloat

    return lat
  }

  longitude.mapTo(('lat, 'lon, 'device, 'count) -> 'json){
    x:(String, String, String, String) => val (lat, lon, device, count) = x
      s"""{"lat":$lat,"lon":$lon,"device":"$device","count":$count}"""
  }
    .groupAll {group => group.mkString('json, ",")}
    .map('json -> 'json) {x:String => "[" + x + "]"}
    .write(TextLine("locationJson.json"))

// following code, which is from the book, cannot compile, so I write above to split code into two map steps
//    .map('location -> 'lat, 'lon)
//    { x:String => val (lat,lon) = x.split(",")
//      ("%4.2f" format lat.toFloat, "%4.2f" format lon.toFloat)
//    }
//    .groupBy('lat, 'lon, 'device)
//    { group => group.size('count) }

}
































