import cascading.pipe.joiner.{LeftJoin, OuterJoin}
import com.twitter.scalding._

import scala.util.matching.Regex

/**
  * Created by guoch on 9/11/17.
  */
class DataTypeTests(args : Args) extends Job(args) {

  val inputSchema = List('productID, 'price, 'quantity)
  val data = Csv("src/main/resources/data.csv", ",", inputSchema).read

  data.write( Tsv("outputTsv.txt") )
  data.write( Osv("outputOsv.txt") )


  // product semi-structured data
//  val pipe = TextLine ("src/main/resources/products.txt").read
//    .mapTo('line -> ('productID, 'price, 'quantity)) { x: String => (x.split(",").toList.get(0),
//                                                                     x.split(",").toList.get(1),
//                                                                     x.split(",").toList.get(2))}
//    .write(Tsv("productsTsv.tsv"))

//  val pattern = new Regex("(?<=\\[)[^]]+(?=\\])")
//  def parseLine(s : String) = {
//    (
//      pattern findFirstIn s get,
//      s.split(" ").toList.get(1),
//      s.split(" ").toList.get(2)
//    )
//  }

//  Caused by: java.lang.ArrayIndexOutOfBoundsException: 1
//  at cascading.tuple.Fields.get(Fields.java:851)
//  at DataTypeTests$$anonfun$3.apply(DataTypeTests.scala:20)
//  at DataTypeTests$$anonfun$3.apply(DataTypeTests.scala:19)
//  at com.twitter.scalding.MapFunction.operate(Operations.scala:59)
//  at cascading.flow.stream.FunctionEachStage.receive(FunctionEachStage.java:99)
//  ... 8 more


  //map to ProductwithVAT. Code add another cloumn priceWithVAT
  val myProduct = Tsv("src/main/resources/productsTsv.tsv", ('productID, 'price, 'quantity)).read
  val myProductBrand = Tsv("src/main/resources/productsTsvBrand.tsv", ('productID, 'brand)).read
    .rename('productID -> 'productID_)

  //add new column
  val myNewProduct = myProduct.insert('brand, "Mike")
    .limit(5)
    .filter('price) {p: Double => p > 12}


  myNewProduct.map('price -> 'priceWithVAT) {price: Double => (price * 1.10).round}
    .write(Tsv("productWithVAT.tsv"))

  //join
  val pipe = myProduct.joinWithSmaller('productID -> 'productID_, myProductBrand, joiner=new LeftJoin)
    .write(Tsv("joinProduct.tsv"))


  val kidsPipe = Tsv("src/main/resources/kidsPipe.tsv", ('kids, 'age, 'fruit)).read
    .groupBy('fruit){group => group.size.average('age)}
    .write(Tsv("kidPipeGroupByFruit.tsv"))

  val myProductForSort = Tsv("src/main/resources/productsTsvSort.tsv", ('productID, 'price, 'quantity)).read
  myProductForSort.groupAll(group => group.sortBy('price).take(3))
    .write(Tsv("sortProducts.tsv"))

  //Page 56

}
