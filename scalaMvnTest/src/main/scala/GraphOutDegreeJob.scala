import com.twitter.scalding._
import com.twitter.scalding.mathematics.Matrix

/**
  * Created by guoch on 8/11/17.
  */
class GraphOutDegreeJob(args: Args) extends Job(args){

  import Matrix._

  val adjacencyMatrix = Tsv( args("input"), ('user1, 'user2, 'rel) )
    .read
    .toMatrix[Long,Long,Double]('user1, 'user2, 'rel)

  adjacencyMatrix.write( Tsv( args("output") ) )

  adjacencyMatrix.sumColVectors.write( Tsv( "output_sumCol.txt" ) )
  (adjacencyMatrix * adjacencyMatrix.transpose).write( Tsv( "output_innerPro.txt" ) )

}

//mvn clean compile assembly:single
//java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool GraphOutDegreeJob --local --input src/main/resources/graph.tsv --output output_sum.txt -Xmx1024m



