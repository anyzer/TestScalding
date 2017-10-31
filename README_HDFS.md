1. HDFS write operation
   * The HDFS Client requests to write a file block;
   * The NameNode responds to the Client with the DataNode to which to write the block
   * The Client requests to write the block to the specified DataNode
   * The DataNode opens a block replication pipeline with another DataNode in the cluster, and this process continues until all configured replicas are written
   * A write acknowledgment is sent back through the replication pipeline
   * The Client is informed that the write operation was successful
   
2. HDFS read operation
   * The HDFS Client requests to read a file
   * The NameNode responds to the request with a list of DataNodes containing the blocks that comprise the file
   * The Client communicates directly with the DataNodes to retrieve blocks for the file
   
3. Check Hadoop Confs: hdfs getconf -confKey fs.default.name 
   * hdfs getconf -confKey fs.defaultFS
   * hdfs getconf -namenodes
   * Note: you have to ssh to Hadoop server first
   
   This setting should be consistent with #5
   
4. SSH to Hadoop Server: ssh root@127.0.0.1 -p 2222   

5. Hadoop Config file locaiton: /etc/hadoop/2.6.1.0-129/0/
   * 2.6.1.0-129 is Hadoop Version
   * core-site.xml, hdfs-site.xml, 
   * Copy files from docker: docker cp <containerName>:/home/data.txt .
 
6. Check if a file/folder exists in hadoop
   * hadoop fs -test -f hdfs://sandbox.hortonworks.com:8020/shakespeare.txt
   * hadoop fs -test -d hdfs://sandbox.hortonworks.com:8020/myData
   
   -d: f the path is a directory, return 0.
   -e: if the path exists, return 0.
   -f: if the path is a file, return 0.
   -s: if the path is not empty, return 0.
   -z: if the file is zero length, return 0.
   
   Check more commands for HDFS shell: http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/FileSystemShell.html#test
   
7. ls 
    * hadoop fs -ls hdfs://sandbox.hortonworks.com:8020/user/guoch/
    * hadoop fs -ls /user/guoch (in ssh)
   
8. Compile to executable jar, and deploy to hadoop (Ref: https://hortonworks.com/tutorial/setting-up-a-spark-development-environment-with-scala/)

   (1) Add this line of code to project/plugins.sbt: addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
   (2) Type: sbt assembly
   (3) Copy the assembly over to the sandbox: scp -P 2222 ./target/scala-2.11/sparkTutorialScala-assembly-1.0.jar root@sandbox.hortonworks.com:/root
   (4) Open a second terminal window and ssh into the sandbox: ssh -p 2222 root@sandbox.hortonworks.com
   (5) Use spark-submit to run our code. We need to specify the main class, the jar to run, and the run mode (local or cluster):
       /usr/hdp/current/spark2-client/bin/spark-submit --class "main.scala.Main"  --master local ./sparkTutorialScala-assembly-1.0.jar
   
9. Debug code (Ref: https://hortonworks.com/tutorial/setting-up-a-spark-development-environment-with-scala/)   
   
   
   
   