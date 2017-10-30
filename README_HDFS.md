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
   
   
   
   
   
   
   
   
   