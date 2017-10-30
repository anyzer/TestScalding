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
   
3.    
   
   