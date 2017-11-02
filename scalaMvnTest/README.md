1. execute scala: mvn package exec:java -Dexec.mainClass=HelloWorld
   * print: Hello World


2. execute java: mvn package exec:java -Dexec.mainClass=Hello
   * print: Hello
   * difference between above two is: main method is in different main class name
   
3. https://ransilberman.com/2014/12/29/creating-scalding-project-with-maven/
   * working directory: /Users/guoch/ScalaProjects/TestScalding/scalaMvnTest
   
4. More about Scalding
   * https://twitter.github.io/scalding/resources_for_learners.html
   
5. Execute by java
   * java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool WordCountJob --local --input "src/main/resources/input.txt" ––output "output.txt" -Xmx1024m
   * java -cp target/scalaMvnTest-1.0-SNAPSHOT.jar.jar com.twitter.scalding.Tool WordCountJob --local --input input.txt ––output output.txt -Xmx1024m