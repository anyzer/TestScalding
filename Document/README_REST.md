REST Access to HDFS

1. WebHDFS: curl -i -L "http://namenode:50070/webhdfs/v1/data/file.txt? op=OPEN"
2. Enable WebHDFS
   * Add a new configuration property to hdfs-site.xml to enable WebHDFS
        $ sudo vi /etc/hadoop/conf/hdfs-site.xml
   * Add the following property between the <configuration> and </configuration> tags
    '''
    <property>
    <name>dfs.webhdfs.enabled</name>
    <value>true</value>
    </property>
    '''
   * Restart the NameNode:
        '''
        $ sudo -u hdfs $HADOOP_HOME/sbin/hadoop-daemon.sh stop namenode
        $ sudo -u hdfs $HADOOP_HOME/sbin/hadoop-daemon.sh start namenode
        '''
   * $ curl -i \ "http://localhost:50070/webhdfs/v1/flume/events? op=GETFILESTATUS"

3. HttpFS

  HttpFS provides RESTful access via a service. The configuration of HttpFS is not as simple as WebHDFS, 
  but the solution is more scalable, supporting HA HDFS implementations and not requiring direct client 
  accessibility to DataNodes in the cluster.
  The HttpFS server acts as a proxy accepting REST requests from clients and submitting them to HDFS on the clients’ behalf

    Example:
    $ curl http://httpfs-host:14000/webhdfs/v1/user/foo/README.txt
