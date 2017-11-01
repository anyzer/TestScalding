1. Caused by: org.apache.hadoop.ipc.RemoteException(java.io.IOException): File /user/guoch/input.txt could only be replicated to 0 nodes instead of minReplication (=1).  
    There are 1 datanode(s) running and 1 node(s) are excluded in this operation.

   Code can upload file to HDFS, but throw this error
   
   Add following in hdfs-site.xml 
   '''
   <property>
        <name>dfs.client.use.datanode.hostname</name>
        <value>true</value>
   </property>
   '''
   Ref: https://streamsets.com/blog/quick-tip-resolving-minreplication-hadoop-fs-error/
   