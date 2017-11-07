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
   * mvn clean compile assembly:single
   * java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool WordCountJob --local -Xmx1024m
   * have some problem to pass parameters in command line
   * java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool WordCountJob --local --input input.txt --output output.txt -Xmx1024m
   * java -cp target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool WordCountJob --local --input src/main/resources/input.txt --output output.txt -Xmx1024m

6. Copy Code to Hadoop Server (NOT the Hadoop Cluster)
   * scp -P 2222 ./target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar root@sandbox.hortonworks.com:/root
   * Password may be A123890a 
   * Login to Hadoop server
   * ssh root@127.0.0.1 -p 2222
   
7. Create input.txt in Hadoop Server and Upload to Hadoop Cluster
   * $ echo "This is a happy day. A day to remember" > input.txt
     $ hadoop fs -mkdir -p hdfs:///data/input hdfs:///data/output
     $ hadoop fs -put input.txt hdfs:///data/input/
   * Can verify folder input and output, file input.txt in http://127.0.0.1:8080/#/main/view/FILES/auto_files_instance (Horntomas UI)
   
8. Execute WordCount code in Hadoop Cluster
   * hadoop jar target/scalaMvnTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool WordCountJob --hdfs --input hdfs:///data/input --output hdfs:///data/output
   * Exception in thread "main" java.lang.NoSuchMethodException: WordCountJob.main([Ljava.lang.String;)