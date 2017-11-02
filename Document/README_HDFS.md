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
   * Hortonworks: hadoop
   * Normally change to A123890a

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
   (3) Copy the assembly over to the sandbox: scp -P 2222 ./target/scala-2.11/TestScalding-assembly-1.0.jar root@sandbox.hortonworks.com:/root
   (4) Open a second terminal window and ssh into the sandbox: ssh -p 2222 root@sandbox.hortonworks.com
   (5) Use spark-submit to run our code. We need to specify the main class, the jar to run, and the run mode (local or cluster):
       spark-submit --class "main.scala.Main"  --master local ./sparkTutorialScala-assembly-1.0.jar

   Execute Spark Words Count Result:
   
   [root@sandbox ~]# spark-submit --class "Main" --master yarn --deploy-mode client ./TestScalding-assembly-1.0.jar
   SPARK_MAJOR_VERSION is set to 2, using Spark2
   17/10/31 08:45:40 INFO SparkContext: Running Spark version 2.1.1.2.6.1.0-129
   17/10/31 08:45:40 INFO SecurityManager: Changing view acls to: root
   17/10/31 08:45:40 INFO SecurityManager: Changing modify acls to: root
   17/10/31 08:45:40 INFO SecurityManager: Changing view acls groups to: 
   17/10/31 08:45:40 INFO SecurityManager: Changing modify acls groups to: 
   17/10/31 08:45:40 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users  with view permissions: Set(root); groups with view permissions: Set(); users  with modify permissions: Set(root); groups with modify permissions: Set()
   17/10/31 08:45:41 INFO Utils: Successfully started service 'sparkDriver' on port 35767.
   17/10/31 08:45:41 INFO SparkEnv: Registering MapOutputTracker
   17/10/31 08:45:41 INFO SparkEnv: Registering BlockManagerMaster
   17/10/31 08:45:41 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
   17/10/31 08:45:41 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
   17/10/31 08:45:41 INFO DiskBlockManager: Created local directory at /tmp/blockmgr-3a09e303-6add-4a35-8248-b5b4ddd690c6
   17/10/31 08:45:41 INFO MemoryStore: MemoryStore started with capacity 366.3 MB
   17/10/31 08:45:41 INFO SparkEnv: Registering OutputCommitCoordinator
   17/10/31 08:45:41 INFO log: Logging initialized @3362ms
   17/10/31 08:45:42 INFO Server: jetty-9.2.z-SNAPSHOT
   17/10/31 08:45:42 INFO Server: Started @3492ms
   17/10/31 08:45:42 WARN AbstractLifeCycle: FAILED ServerConnector@1bdf8190{HTTP/1.1}{0.0.0.0:4040}: java.net.BindException: Address already in use
   java.net.BindException: Address already in use
   	at sun.nio.ch.Net.bind0(Native Method)
   	at sun.nio.ch.Net.bind(Net.java:433)
   	at sun.nio.ch.Net.bind(Net.java:425)
   	at sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:223)
   	at sun.nio.ch.ServerSocketAdaptor.bind(ServerSocketAdaptor.java:74)
   	at org.spark_project.jetty.server.ServerConnector.open(ServerConnector.java:321)
   	at org.spark_project.jetty.server.AbstractNetworkConnector.doStart(AbstractNetworkConnector.java:80)
   	at org.spark_project.jetty.server.ServerConnector.doStart(ServerConnector.java:236)
   	at org.spark_project.jetty.util.component.AbstractLifeCycle.start(AbstractLifeCycle.java:68)
   	at org.apache.spark.ui.JettyUtils$.org$apache$spark$ui$JettyUtils$$newConnector$1(JettyUtils.scala:321)
   	at org.apache.spark.ui.JettyUtils$.org$apache$spark$ui$JettyUtils$$httpConnect$1(JettyUtils.scala:353)
   	at org.apache.spark.ui.JettyUtils$$anonfun$7.apply(JettyUtils.scala:356)
   	at org.apache.spark.ui.JettyUtils$$anonfun$7.apply(JettyUtils.scala:356)
   	at org.apache.spark.util.Utils$$anonfun$startServiceOnPort$1.apply$mcVI$sp(Utils.scala:2220)
   	at scala.collection.immutable.Range.foreach$mVc$sp(Range.scala:160)
   	at org.apache.spark.util.Utils$.startServiceOnPort(Utils.scala:2212)
   	at org.apache.spark.ui.JettyUtils$.startJettyServer(JettyUtils.scala:356)
   	at org.apache.spark.ui.WebUI.bind(WebUI.scala:130)
   	at org.apache.spark.SparkContext$$anonfun$10.apply(SparkContext.scala:460)
   	at org.apache.spark.SparkContext$$anonfun$10.apply(SparkContext.scala:460)
   	at scala.Option.foreach(Option.scala:257)
   	at org.apache.spark.SparkContext.<init>(SparkContext.scala:460)
   	at Main$.main(Main.scala:13)
   	at Main.main(Main.scala)
   	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
   	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
   	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
   	at java.lang.reflect.Method.invoke(Method.java:498)
   	at org.apache.spark.deploy.SparkSubmit$.org$apache$spark$deploy$SparkSubmit$$runMain(SparkSubmit.scala:750)
   	at org.apache.spark.deploy.SparkSubmit$.doRunMain$1(SparkSubmit.scala:187)
   	at org.apache.spark.deploy.SparkSubmit$.submit(SparkSubmit.scala:212)
   	at org.apache.spark.deploy.SparkSubmit$.main(SparkSubmit.scala:126)
   	at org.apache.spark.deploy.SparkSubmit.main(SparkSubmit.scala)
   17/10/31 08:45:42 WARN Utils: Service 'SparkUI' could not bind on port 4040. Attempting port 4041.
   17/10/31 08:45:42 INFO ServerConnector: Started ServerConnector@24da7dd4{HTTP/1.1}{0.0.0.0:4041}
   17/10/31 08:45:42 INFO Utils: Successfully started service 'SparkUI' on port 4041.
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@4a9cc6cb{/jobs,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@7caa550{/jobs/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@72b16078{/jobs/job,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@1b0a7baf{/jobs/job/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@32057e6{/stages,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@6ea1bcdc{/stages/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@64712be{/stages/stage,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@46c670a6{/stages/stage/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@5ae81e1{/stages/pool,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@5ae76500{/stages/pool/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@1133ec6e{/storage,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@54709809{/storage/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@24f360b2{/storage/rdd,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@302fec27{/storage/rdd/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@48c40605{/environment,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@1b11ef33{/environment/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@6cea706c{/executors,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@2f2bf0e2{/executors/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@21ec5d87{/executors/threadDump,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@552518c3{/executors/threadDump/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@59aa20b3{/static,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@4a9e6faf{/,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@4e4efc1b{/api,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@2a76b80a{/jobs/job/kill,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@2f4854d6{/stages/stage/kill,null,AVAILABLE,@Spark}
   17/10/31 08:45:42 INFO SparkUI: Bound SparkUI to 0.0.0.0, and started at http://172.17.0.2:4041
   17/10/31 08:45:42 INFO SparkContext: Added JAR file:/root/./TestScalding-assembly-1.0.jar at spark://172.17.0.2:35767/jars/TestScalding-assembly-1.0.jar with timestamp 1509439542238
   17/10/31 08:45:42 INFO Executor: Starting executor ID driver on host localhost
   17/10/31 08:45:42 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 43337.
   17/10/31 08:45:42 INFO NettyBlockTransferService: Server created on 172.17.0.2:43337
   17/10/31 08:45:42 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
   17/10/31 08:45:42 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, 172.17.0.2, 43337, None)
   17/10/31 08:45:42 INFO BlockManagerMasterEndpoint: Registering block manager 172.17.0.2:43337 with 366.3 MB RAM, BlockManagerId(driver, 172.17.0.2, 43337, None)
   17/10/31 08:45:42 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, 172.17.0.2, 43337, None)
   17/10/31 08:45:42 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, 172.17.0.2, 43337, None)
   17/10/31 08:45:42 INFO ContextHandler: Started o.s.j.s.ServletContextHandler@56f2bbea{/metrics/json,null,AVAILABLE,@Spark}
   17/10/31 08:45:44 INFO EventLoggingListener: Logging events to hdfs:///spark2-history/local-1509439542311
   17/10/31 08:45:45 INFO MemoryStore: Block broadcast_0 stored as values in memory (estimated size 352.4 KB, free 366.0 MB)
   17/10/31 08:45:45 INFO MemoryStore: Block broadcast_0_piece0 stored as bytes in memory (estimated size 31.4 KB, free 365.9 MB)
   17/10/31 08:45:45 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on 172.17.0.2:43337 (size: 31.4 KB, free: 366.3 MB)
   17/10/31 08:45:45 INFO SparkContext: Created broadcast 0 from textFile at Main.scala:15
   17/10/31 08:45:45 INFO FileInputFormat: Total input paths to process : 1
   17/10/31 08:45:45 INFO SparkContext: Starting job: foreach at Main.scala:21
   17/10/31 08:45:45 INFO DAGScheduler: Registering RDD 3 (map at Main.scala:18)
   17/10/31 08:45:45 INFO DAGScheduler: Got job 0 (foreach at Main.scala:21) with 1 output partitions
   17/10/31 08:45:45 INFO DAGScheduler: Final stage: ResultStage 1 (foreach at Main.scala:21)
   17/10/31 08:45:45 INFO DAGScheduler: Parents of final stage: List(ShuffleMapStage 0)
   17/10/31 08:45:45 INFO DAGScheduler: Missing parents: List(ShuffleMapStage 0)
   17/10/31 08:45:45 INFO DAGScheduler: Submitting ShuffleMapStage 0 (MapPartitionsRDD[3] at map at Main.scala:18), which has no missing parents
   17/10/31 08:45:45 INFO MemoryStore: Block broadcast_1 stored as values in memory (estimated size 4.7 KB, free 365.9 MB)
   17/10/31 08:45:45 INFO MemoryStore: Block broadcast_1_piece0 stored as bytes in memory (estimated size 2.7 KB, free 365.9 MB)
   17/10/31 08:45:45 INFO BlockManagerInfo: Added broadcast_1_piece0 in memory on 172.17.0.2:43337 (size: 2.7 KB, free: 366.3 MB)
   17/10/31 08:45:45 INFO SparkContext: Created broadcast 1 from broadcast at DAGScheduler.scala:996
   17/10/31 08:45:45 INFO DAGScheduler: Submitting 1 missing tasks from ShuffleMapStage 0 (MapPartitionsRDD[3] at map at Main.scala:18)
   17/10/31 08:45:45 INFO TaskSchedulerImpl: Adding task set 0.0 with 1 tasks
   17/10/31 08:45:46 INFO TaskSetManager: Starting task 0.0 in stage 0.0 (TID 0, localhost, executor driver, partition 0, ANY, 6061 bytes)
   17/10/31 08:45:46 INFO Executor: Running task 0.0 in stage 0.0 (TID 0)
   17/10/31 08:45:46 INFO Executor: Fetching spark://172.17.0.2:35767/jars/TestScalding-assembly-1.0.jar with timestamp 1509439542238
   17/10/31 08:45:46 INFO TransportClientFactory: Successfully created connection to /172.17.0.2:35767 after 48 ms (0 ms spent in bootstraps)
   17/10/31 08:45:46 INFO Utils: Fetching spark://172.17.0.2:35767/jars/TestScalding-assembly-1.0.jar to /tmp/spark-c714cd6c-6ce6-4d3f-88c0-ee5628be97fd/userFiles-a654ff71-151a-4b0c-8559-6e71ac5a5e99/fetchFileTemp184379979702920627.tmp
   17/10/31 08:45:46 INFO Executor: Adding file:/tmp/spark-c714cd6c-6ce6-4d3f-88c0-ee5628be97fd/userFiles-a654ff71-151a-4b0c-8559-6e71ac5a5e99/TestScalding-assembly-1.0.jar to class loader
   17/10/31 08:45:46 INFO HadoopRDD: Input split: hdfs://sandbox.hortonworks.com:8020/tmp/shakespeare.txt:0+5447172
   17/10/31 08:45:46 INFO deprecation: mapred.tip.id is deprecated. Instead, use mapreduce.task.id
   17/10/31 08:45:46 INFO deprecation: mapred.task.id is deprecated. Instead, use mapreduce.task.attempt.id
   17/10/31 08:45:46 INFO deprecation: mapred.task.is.map is deprecated. Instead, use mapreduce.task.ismap
   17/10/31 08:45:46 INFO deprecation: mapred.task.partition is deprecated. Instead, use mapreduce.task.partition
   17/10/31 08:45:46 INFO deprecation: mapred.job.id is deprecated. Instead, use mapreduce.job.id
   17/10/31 08:45:48 INFO Executor: Finished task 0.0 in stage 0.0 (TID 0). 1815 bytes result sent to driver
   17/10/31 08:45:48 INFO TaskSetManager: Finished task 0.0 in stage 0.0 (TID 0) in 2139 ms on localhost (executor driver) (1/1)
   17/10/31 08:45:48 INFO TaskSchedulerImpl: Removed TaskSet 0.0, whose tasks have all completed, from pool 
   17/10/31 08:45:48 INFO DAGScheduler: ShuffleMapStage 0 (map at Main.scala:18) finished in 2.180 s
   17/10/31 08:45:48 INFO DAGScheduler: looking for newly runnable stages
   17/10/31 08:45:48 INFO DAGScheduler: running: Set()
   17/10/31 08:45:48 INFO DAGScheduler: waiting: Set(ResultStage 1)
   17/10/31 08:45:48 INFO DAGScheduler: failed: Set()
   17/10/31 08:45:48 INFO DAGScheduler: Submitting ResultStage 1 (ShuffledRDD[4] at reduceByKey at Main.scala:19), which has no missing parents
   17/10/31 08:45:48 INFO MemoryStore: Block broadcast_2 stored as values in memory (estimated size 3.1 KB, free 365.9 MB)
   17/10/31 08:45:48 INFO MemoryStore: Block broadcast_2_piece0 stored as bytes in memory (estimated size 1943.0 B, free 365.9 MB)
   17/10/31 08:45:48 INFO BlockManagerInfo: Added broadcast_2_piece0 in memory on 172.17.0.2:43337 (size: 1943.0 B, free: 366.3 MB)
   17/10/31 08:45:48 INFO SparkContext: Created broadcast 2 from broadcast at DAGScheduler.scala:996
   17/10/31 08:45:48 INFO DAGScheduler: Submitting 1 missing tasks from ResultStage 1 (ShuffledRDD[4] at reduceByKey at Main.scala:19)
   17/10/31 08:45:48 INFO TaskSchedulerImpl: Adding task set 1.0 with 1 tasks
   17/10/31 08:45:48 INFO TaskSetManager: Starting task 0.0 in stage 1.0 (TID 1, localhost, executor driver, partition 0, ANY, 5819 bytes)
   17/10/31 08:45:48 INFO Executor: Running task 0.0 in stage 1.0 (TID 1)
   17/10/31 08:45:48 INFO ShuffleBlockFetcherIterator: Getting 1 non-empty blocks out of 1 blocks
   17/10/31 08:45:48 INFO ShuffleBlockFetcherIterator: Started 0 remote fetches in 14 ms
   (hack'd.,1)
   (Ah!,3)
   (Worthy;,1)
   (bone,7)
   (vailing,1)
   (afternoon?,2)
   (wounds-,1)
   (LAFEU],1)
   (signal.,1)
   (vocation';,1)
   (fartuous,1)
   (hem,1)
   (meets,,1)
   (toll,,1)
   (melody?,1)
   (it!-,3)
   (tough,6)
   (briefly,,7)
   (Beaufort,,4)
   (Ferdinand.,1)
   (imprisoning,1)
   (Sought,2)
   (rotten;,1)
   (soon;,1)
   (musician,,2)
   (discased,,1)
   (Bastard.,3)
   (branches-it,1)
   (Bequeathing,1)
   (fowl,7)
   (coat;,3)
   (Dorothy.,1)
   (execute,12)
   (relieves,1)
   (afterward,5)
   (fight;,9)
   (unfelt,3)
   (harlot's,2)
   (abroad-anon,1)
   (Dearly,,1)
   (Porpentine;,3)
   (wicked?,4)
   (speak:,4)
   (Man-ent'red,1)
   (insurrection,3)
   ([Climbs,2)
   (burghers,2)
   (Thunder.,6)
   (Bar.,2)
   (feats;,1)
   (robin,1)
   (pavilion'd,1)
   (regina,1)
   (pains;,7)
   (savages,,1)
   (Exactly,1)
   (housewifery,,1)
   (Enow,1)
   (dangers,13)
   (ulcerous,2)
   (bloody.,7)
   (adultery,2)
   (gasp.,2)
   (constant,,7)
   (PUCK,12)
   (depart!,1)
   (city],4)
   (Pernicious,2)
   (praetors,,1)
   (goodliest,2)
   (Mercuries.,1)
   (Caithness,,2)
   (ways-,1)
   (maiden.,1)
   (bold'ned,1)
   (forsaken,,3)
   (Warwick.,7)
   (Sycorax,4)
   (goods.,2)
   (used,,1)
   (Smil'd,1)
   (Tib's,1)
   (arch,,1)
   (shepherdess,,2)
   (tyrant,,6)
   (well!,18)
   (Inform'd,1)
   (lustful,4)
   (deficient,,1)
   (colour's,1)
   (matron,1)
   ('gins,2)
   (into't,2)
   (Blue,,1)
   (defiance,5)
   (scrap'd,3)
   (Horatio.],2)
   (confirmer,1)
   (weepingly,1)
   (Hyperion.,1)
   (Rest,8)
   (DULL],1)
   (dropt,1)
   (geese,6)
   (soars!,1)
   (profaneness,1)
   (effeminate,,1)
   (majestical,4)
   (Abusing,1)
   (miching,1)
   (Reward,2)
   (light-wing'd,1)
   (mista'en,1)
   (Gaunt!,1)
   (bo-peep,1)
   (descension!,1)
   (Gardiner.,1)
   (embrac'd,,2)
   (parentage;,1)
   (lie:,2)
   (cities,7)
   (dolphin,2)
   (strut,4)
   (consent,,24)
   (cord;,1)
   (surgeon!,2)
   (Octavia.,7)
   (groves;,1)
   (sequence,4)
   ([Music.,1)
   (visible,,1)
   (goldsmith's,1)
   (giddiness,1)
   (fig!,1)
   (pardon't,,1)
   (scuffles,1)
   (fleering,1)
   (fortunes;,5)
   (castle.,24)
   (right,192)
   (tunes,2)
   (Fetch't,,1)
   (vant,,1)
   (tongue-tied,9)
   (rose-cheek'd,1)
   (rejoice?,1)
   (crowns;,11)
   (face-royal,,1)
   (garden,,5)
   (yellowing,1)
   (tann'd,1)
   ('Amen.',2)
   (Banquo's,6)
   (bite,,2)
   (over-full,1)
   (captain's,6)
   (warrior!,1)
   (Knight.,5)
   ((fore,1)
   (Cock-a-diddle-dow.,1)
   (strangers!,1)
   (Deposing,1)
   (undergo-,2)
   (plod.,1)
   (condemn'd:,1)
   (Knowledge,1)
   (impute,2)
   ([MIRANDA,1)
   (dungeon,,4)
   (dat,12)
   (incomparable,2)
   (shoeing,1)
   (counterfeit!,1)
   (seeing,31)
   (Wish,4)
   (taffeta.,1)
   (Leon.,120)
   (east,16)
   (strangely,15)
   (birth-,2)
   (Tooth'd,1)
   (see:,9)
   (protested,,1)
   (lines,24)
   (to-day;,11)
   (fails,3)
   (Troy;,3)
   (souls?,2)
   (frights,5)
   (boots!,1)
   (grandsire!,1)
   (Laws,1)
   (check?,1)
   (blacker!,1)
   (annex'd,1)
   (tired,,2)
   (beloved,22)
   (How,1092)
   (hitherwards,1)
   (god-fathers;,1)
   (Hautboys,4)
   (sing],1)
   (burn'd';,1)
   (praised,,1)
   (Masham.,1)
   (pain;,2)
   (Ajax',1)
   (boy's,11)
   (advancement?,1)
   (Kates,,1)
   (solicitation;,1)
   (prov'd,,2)
   (Train],1)
   (inviting,4)
   (hour,184)
   (afore't.,1)
   (petticoats,,1)
   ('my,1)
   (twentieth,2)
   (mortal,83)
   (Tapster?,2)
   (conquest;,2)
   (Seducing,1)
   (courses;,1)
   (potting.,1)
   (GOBBO,2)
   (greatness.',1)
   (feasting,6)
   (obedience,,14)
   (withstanding.,1)
   (integritas,,1)
   (firm.,1)
    ...
    ...
    ...
   (between-,1)
   (bellow,1)
   (call?,4)
   (moan;,1)
   (stoup,3)
   (surfeit-swell'd,,1)
   (delays.,1)
   (rip,2)
   (Tully;,1)
   (heart-sore,2)
   (Calaber,,1)
   17/10/31 08:45:49 INFO Executor: Finished task 0.0 in stage 1.0 (TID 1). 1809 bytes result sent to driver
   17/10/31 08:45:49 INFO TaskSetManager: Finished task 0.0 in stage 1.0 (TID 1) in 1523 ms on localhost (executor driver) (1/1)
   17/10/31 08:45:49 INFO TaskSchedulerImpl: Removed TaskSet 1.0, whose tasks have all completed, from pool 
   17/10/31 08:45:49 INFO DAGScheduler: ResultStage 1 (foreach at Main.scala:21) finished in 1.524 s
   17/10/31 08:45:49 INFO DAGScheduler: Job 0 finished: foreach at Main.scala:21, took 4.214712 s
   17/10/31 08:45:49 INFO SparkContext: Starting job: count at Main.scala:22
   17/10/31 08:45:49 INFO MapOutputTrackerMaster: Size of output statuses for shuffle 0 is 146 bytes
   17/10/31 08:45:49 INFO DAGScheduler: Got job 1 (count at Main.scala:22) with 1 output partitions
   17/10/31 08:45:49 INFO DAGScheduler: Final stage: ResultStage 3 (count at Main.scala:22)
   17/10/31 08:45:49 INFO DAGScheduler: Parents of final stage: List(ShuffleMapStage 2)
   17/10/31 08:45:49 INFO DAGScheduler: Missing parents: List()
   17/10/31 08:45:49 INFO DAGScheduler: Submitting ResultStage 3 (ShuffledRDD[4] at reduceByKey at Main.scala:19), which has no missing parents
   17/10/31 08:45:49 INFO MemoryStore: Block broadcast_3 stored as values in memory (estimated size 3.0 KB, free 365.9 MB)
   17/10/31 08:45:49 INFO MemoryStore: Block broadcast_3_piece0 stored as bytes in memory (estimated size 1906.0 B, free 365.9 MB)
   17/10/31 08:45:49 INFO BlockManagerInfo: Added broadcast_3_piece0 in memory on 172.17.0.2:43337 (size: 1906.0 B, free: 366.3 MB)
   17/10/31 08:45:49 INFO SparkContext: Created broadcast 3 from broadcast at DAGScheduler.scala:996
   17/10/31 08:45:49 INFO DAGScheduler: Submitting 1 missing tasks from ResultStage 3 (ShuffledRDD[4] at reduceByKey at Main.scala:19)
   17/10/31 08:45:49 INFO TaskSchedulerImpl: Adding task set 3.0 with 1 tasks
   17/10/31 08:45:49 INFO TaskSetManager: Starting task 0.0 in stage 3.0 (TID 2, localhost, executor driver, partition 0, ANY, 5735 bytes)
   17/10/31 08:45:49 INFO Executor: Running task 0.0 in stage 3.0 (TID 2)
   17/10/31 08:45:49 INFO ShuffleBlockFetcherIterator: Getting 1 non-empty blocks out of 1 blocks
   17/10/31 08:45:49 INFO ShuffleBlockFetcherIterator: Started 0 remote fetches in 0 ms
   17/10/31 08:45:49 INFO Executor: Finished task 0.0 in stage 3.0 (TID 2). 1847 bytes result sent to driver
   17/10/31 08:45:49 INFO TaskSetManager: Finished task 0.0 in stage 3.0 (TID 2) in 99 ms on localhost (executor driver) (1/1)
   17/10/31 08:45:49 INFO TaskSchedulerImpl: Removed TaskSet 3.0, whose tasks have all completed, from pool 
   17/10/31 08:45:49 INFO DAGScheduler: ResultStage 3 (count at Main.scala:22) finished in 0.100 s
   17/10/31 08:45:49 INFO DAGScheduler: Job 1 finished: count at Main.scala:22, took 0.181425 s
   Total Words: 67109
   17/10/31 08:45:49 INFO FileOutputCommitter: File Output Committer Algorithm version is 1
   17/10/31 08:45:49 INFO FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
   17/10/31 08:45:50 INFO SparkContext: Starting job: saveAsTextFile at Main.scala:23
   17/10/31 08:45:50 INFO DAGScheduler: Got job 2 (saveAsTextFile at Main.scala:23) with 1 output partitions
   17/10/31 08:45:50 INFO DAGScheduler: Final stage: ResultStage 5 (saveAsTextFile at Main.scala:23)
   17/10/31 08:45:50 INFO DAGScheduler: Parents of final stage: List(ShuffleMapStage 4)
   17/10/31 08:45:50 INFO DAGScheduler: Missing parents: List()
   17/10/31 08:45:50 INFO DAGScheduler: Submitting ResultStage 5 (MapPartitionsRDD[5] at saveAsTextFile at Main.scala:23), which has no missing parents
   17/10/31 08:45:50 INFO MemoryStore: Block broadcast_4 stored as values in memory (estimated size 91.5 KB, free 365.8 MB)
   17/10/31 08:45:50 INFO MemoryStore: Block broadcast_4_piece0 stored as bytes in memory (estimated size 34.6 KB, free 365.8 MB)
   17/10/31 08:45:50 INFO BlockManagerInfo: Added broadcast_4_piece0 in memory on 172.17.0.2:43337 (size: 34.6 KB, free: 366.2 MB)
   17/10/31 08:45:50 INFO SparkContext: Created broadcast 4 from broadcast at DAGScheduler.scala:996
   17/10/31 08:45:50 INFO DAGScheduler: Submitting 1 missing tasks from ResultStage 5 (MapPartitionsRDD[5] at saveAsTextFile at Main.scala:23)
   17/10/31 08:45:50 INFO TaskSchedulerImpl: Adding task set 5.0 with 1 tasks
   17/10/31 08:45:50 INFO TaskSetManager: Starting task 0.0 in stage 5.0 (TID 3, localhost, executor driver, partition 0, ANY, 5827 bytes)
   17/10/31 08:45:50 INFO Executor: Running task 0.0 in stage 5.0 (TID 3)
   17/10/31 08:45:50 INFO ShuffleBlockFetcherIterator: Getting 1 non-empty blocks out of 1 blocks
   17/10/31 08:45:50 INFO ShuffleBlockFetcherIterator: Started 0 remote fetches in 1 ms
   17/10/31 08:45:51 INFO FileOutputCommitter: File Output Committer Algorithm version is 1
   17/10/31 08:45:51 INFO FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
   17/10/31 08:45:51 INFO BlockManagerInfo: Removed broadcast_2_piece0 on 172.17.0.2:43337 in memory (size: 1943.0 B, free: 366.2 MB)
   17/10/31 08:45:51 INFO BlockManagerInfo: Removed broadcast_3_piece0 on 172.17.0.2:43337 in memory (size: 1906.0 B, free: 366.2 MB)
   17/10/31 08:45:51 INFO BlockManagerInfo: Removed broadcast_1_piece0 on 172.17.0.2:43337 in memory (size: 2.7 KB, free: 366.2 MB)
   17/10/31 08:45:51 INFO FileOutputCommitter: Saved output of task 'attempt_20171031084549_0005_m_000000_3' to hdfs://sandbox.hortonworks.com:8020/tmp/shakespeareWordCount/_temporary/0/task_20171031084549_0005_m_000000
   17/10/31 08:45:51 INFO SparkHadoopMapRedUtil: attempt_20171031084549_0005_m_000000_3: Committed
   17/10/31 08:45:51 INFO Executor: Finished task 0.0 in stage 5.0 (TID 3). 1963 bytes result sent to driver
   17/10/31 08:45:51 INFO TaskSetManager: Finished task 0.0 in stage 5.0 (TID 3) in 1201 ms on localhost (executor driver) (1/1)
   17/10/31 08:45:51 INFO TaskSchedulerImpl: Removed TaskSet 5.0, whose tasks have all completed, from pool 
   17/10/31 08:45:51 INFO DAGScheduler: ResultStage 5 (saveAsTextFile at Main.scala:23) finished in 1.202 s
   17/10/31 08:45:51 INFO DAGScheduler: Job 2 finished: saveAsTextFile at Main.scala:23, took 1.322061 s
   17/10/31 08:45:51 INFO SparkContext: Invoking stop() from shutdown hook
   17/10/31 08:45:51 INFO ServerConnector: Stopped Spark@24da7dd4{HTTP/1.1}{0.0.0.0:4041}
   17/10/31 08:45:51 INFO SparkUI: Stopped Spark web UI at http://172.17.0.2:4041
   17/10/31 08:45:51 INFO MapOutputTrackerMasterEndpoint: MapOutputTrackerMasterEndpoint stopped!
   17/10/31 08:45:51 INFO MemoryStore: MemoryStore cleared
   17/10/31 08:45:51 INFO BlockManager: BlockManager stopped
   17/10/31 08:45:51 INFO BlockManagerMaster: BlockManagerMaster stopped
   17/10/31 08:45:51 INFO OutputCommitCoordinator$OutputCommitCoordinatorEndpoint: OutputCommitCoordinator stopped!
   17/10/31 08:45:51 INFO SparkContext: Successfully stopped SparkContext
   17/10/31 08:45:51 INFO ShutdownHookManager: Shutdown hook called
   17/10/31 08:45:51 INFO ShutdownHookManager: Deleting directory /tmp/spark-c714cd6c-6ce6-4d3f-88c0-ee5628be97fd
   [root@sandbox ~]# 
   

   
   