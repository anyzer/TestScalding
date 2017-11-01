*Hadoop -> Flume*

Apache Flume: is a Hadoop ecosystem project originally developed by Cloudera designed to capture, transform, and ingest 
data into HDFS using one or more agents. Flume is written in Java and is completely open source and
intentionally extensible, so if you cannot find a built-in source or sink to do what you need it to do, you can always 
write your own!

In many cases you will need to establish interfaces to capture data produced from source systems in real time, such as 
web logs, or schedule batch snapshots from a relational database, such as a transaction processing system.
    
    1. Flume Agent: consists of Source, Channel and Sink
       * Workflow: WebServer => Source => Channel => Sink => HDFS Cluster
                                -------------------------
                                      Flume Agent
    
    2. Source: A flume agent source instructs the agent where the data is to be received from
       * HTTP - Used to consume data from RESTful service using POST and GET methods
       * Syslog - Log protocal to capture system events
       * JMS - Java Message Service
       * Kafka - Popular open source messaging platform
       * Avro - Open source, cross platform data serialization framework for Hadoop
       * Twitter - Flume source that connects to Twitter's Streaming API to continuously download tweets
    
        Configuration Example:
            agent1.sources = source1
            agent1.sources.source1.type = exec
            agent1.sources.source1.command = tail -F /tmp/events
    
    3. Sink: A flume agent sink tells the agent where to send data. Often the destination is HDFS. However, the
        destination could be another agent that will do some further in-flight processing.
        * HDFS
        * Hive
        * HBase - NoSQL data store built on HDFS
        * Kafka
        
         Configuration Example:
            agent1.sinks = sink1
            agent1.sinks.sink1.type = hdfs
            agent1.sinks.sink1.hdfs.path = /flume/events
            agent1.sinks.sink1.hdfs.filePrefix = events
            agent1.sinks.sink1.hdfs.round = true
            agent1.sinks.sink1.hdfs.roundValue = 10
            agent1.sinks.sink1.hdfs.roundUnit = minute
            
    4. Channel: the flume agent channel is a queue between the agent's source and sink. Flume implements a transactional 
        architecture for added reliability. This enables rollback and retry operations if required.        
            
        The channel or queue used to transfer the data and manage transactions can be implemented in one of the 
         following configurations:
         * In-memory
         * Durable
        
        Durable channels use persistent storage (disk) to maintain state (transactional integrity), meaning that these 
         channels are guaranteed not to lose data in the event of a power failure. Examples of durable channel 
         implementations include:
         * File Channel
         * JDBC Channel
         * Kafka Channel    
            
         Configuration Example:
            agent1.channels = channel1
            agent1.channels.channel1.type = memory
            agent1.channels.channel1.capacity = 100
            agent1.channels.channel1.transactionCapacity = 100
            agent1.channels.channel1.byteCapacityBufferPercentage = 20
            
    5. Deploy Apache Flume
       * Download Flume: wget http://MIRROR_PATH/apache-flume-1.6.0-bin.tar.gz
       * Unpack and install Flume:
         $ tar -xvf apache-flume-1.6.0-bin.tar.gz
         $ mv apache-flume-1.6.0-bin flume
         $ sudo mv flume/ /usr/share/
       * Create a Flume agent configuration, by creating a flumeconf.properties file in /usr/share/flume/conf and
         copying above configuration in sequence
         $ vi /usr/share/flume/conf/flume-conf.properties   
           ...
           agent1.sources.source1.channels = channel1
           agent1.sinks.sink1.channel = channel1
       * We will now set up a test script to generate log messages using random dictionary words.
         $ sudo yum install words
         This will create a file with approximately 450,000 to 500,000 English words (or words derived from English 
         words) in the /usr/share/dict directory.
       * Create a Python script to generate random events using words from /usr/share/dict /words by creating a file 
         in your home directory named gen_events.py and copying the following code into this file:
         '''
         from random import randint
         from datetime import datetime
         word_file = "/usr/share/dict/words"
         WORDS = open(word_file).read().splitlines()
         while True:
         currtime = datetime.now()
         words = ""
         for i in range(0,9):
         words = words + WORDS[randint(0,400000)] + " "
         print currtime.strftime('%Y/%m/%d %H:%M:%S') + "\t" +
         words
         '''
       * Make a directory in HDFS to ingest data from Flume (use your particular user in the chown statement):     
          $ sudo -u hdfs $HADOOP_HOME/bin/hdfs dfs -mkdir -p \ /flume/events
          $ sudo -u hdfs $HADOOP_HOME/bin/hdfs dfs -chown -R \ ec2-user /flume  
       * Open three separate terminal windows.
       * In one window, start the Flume agent using the configuration you created.
         $ /usr/share/flume/bin/flume-ng agent \
         --conf /etc/hadoop/conf/ \
         --conf-file /usr/share/flume/conf/flume-conf.properties
         Download from finelybook www.finelybook.com
         \
         --name agent1   
       * In a second window, run the Python script to generate random log messages:
          $ python gen_events.py >> /tmp/events     
       * In a third window, inspect the HDFS directory configured to capture the log events (/flume/events):     
          $ hadoop fs -ls /flume/events     
            
            
            
            
            
            
            
            
            