import com.twitter.scalding._

/**
  * Created by guoch on 10/11/17.
  */
class CalculateDailyAdPoints(args : Args) extends Job(args)  {


  //There are three groups:
  //  Category
  //Sports: Football Rugby Tennis F1 Cycling
  //Tech: Games Mobile Gadget Apps Internet
  //Culture: Books Film Music Art Theatre
  //Travel: Hotels Skiing Family Budget Breaks

  //Rules:
  //* Our users spend time reading articles and spend more than 20 seconds if they are slightly interested
  // and more than 60 seconds if they are really interested.
  //* Users who also view the video accompanying each article are considered as engaged users.
  //* Occasionally, users get interested in a category they are normally not interested in.
  // The recent behavior has more relevance than past behavior.

  //• One point for each read event that lasts more than 20 seconds
  //• Three points for each read event that lasts more than 60 seconds
  //• Three points per video view

  //Rank:
  // User points = 40% * points yesterday + 20% * points 2 days ago + 10% * points 3 days ago + 30% of the average historic points

  //Implementation Steps:
  //  1. Initially process daily log les and calculate the user points for that day.
  // Store the results in a structure /YYYY/MM/DD/ so that the data is nicely partitioned across multiple executions.
  //  2. In a similar way, calculate the historic points in another pipeline.
  //  3. Once all the data is there, read the daily points of the last three days and the historic points,
  // and join the results with the available advertisements to generate the personalized ads:

  //https://github.com/ scalding-io/ProgrammingWithScalding
  //CreateMockLogData ->
  //datetime: 2014-01-01T15:18:03.867+11:00
  // user: 1
  // activity: readArticle
  // data: article/sport/cycling/7
  // session: 771aee99-d59f-44c7-a42c-1fafc046f3ef
  // location: 40.695,-74.034
  // response: 84 msec
  // device: iphone
  // error:

  //  2014-01-02T07:26:23.847+11:00
  // guoch
  // readArticle
  // article/sport/rugby/3
  // dac9349b-4c2a-42d2-b1f4-a0827100c9dd
  // 40.574000000000005,-74.087
  // 219 msec
  // pc

  val path = "src/main/resources/log-files/2017/11/"
  val mockLogSchema = List ('datetime, 'user, 'activity, 'data, 'session, 'location, 'response, 'device)
  val logs_log_tsv = Tsv(path + "log.tsv", mockLogSchema).read

  val projectedLog = logs_log_tsv.project('user,'datetime,'activity,'data, 'response)
        .groupBy('user) {group => group.sortBy('datetime)}
//        .write(Tsv("logProject.tsv"))

  val logsWithDurations = projectedLog
      .map('response -> 'response) {x:String => x.split(" ").toList.get(0)}
      .map(('activity, 'response) -> 'points) {x:(String, Int) => calculatePoint(x._1, x._2)}
      .map('data -> ('cat, 'subcat)) {x: String =>
        val categories = x.split("/")
        (categories(0), categories(1))
      }
      .groupBy('user, 'cat, 'subcat){group => group.sum[Int]('points)}
      .write(Tsv("logProjectDuration.tsv"))

  //Chapter 9: Matrix Calculation and ML Page 128

  def calculatePoint(activity:String, response:Int):Int = {
    activity match {
      case "streamVideo" => 3
      case "readArticle" =>
        if(response >=1200) 1 else if (response >=200) 3 else if (response >=100) 1 else 0
      case _ => 0
    }
  }

}
