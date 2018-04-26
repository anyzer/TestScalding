(0) Gegneral introduction for Hadoop
    * https://hortonworks.com/tutorial/hadoop-tutorial-getting-started-with-hdp/section/1/

(1) Down: https://hortonworks.com/downloads/#sandbox
    * Select HDP -> Import on Docker -> Download

(2) Import to Docker: https://hortonworks.com/tutorial/sandbox-deployment-and-install-guide/section/3/
    * Set Docker with 4 Cores and 8G Memory
    * See more details on the page
    * docker load -i <sandbox-docker-image-path>
    * Check docker images

(3) Add Sandbox Hostname to your host file
    
    To be able to access HDP services on the Sandbox from our host machine’s browser, you will need to add sandbox.hortonworks.com mapped to your IP address to the list of hosts in your hosts file. The location of the hosts file is different for each OS.
    
    For mac users, enter the following command in your terminal:
    
    '''
    echo '{Host-Name} sandbox.hortonworks.com' | sudo tee -a /private/etc/hosts
    '''
    
    NOTE: In single machine, just replace {Host-Name} with 127.0.0.1
    
    '''
    echo '127.0.0.1 sandbox.hortonworks.com' | sudo tee -a /private/etc/hosts
    '''
 
(4) Start Sandbox: create executable shell, and run it
    
    '''
    #!/bin/bash
    echo "Waiting for docker daemon to start up:"
    until docker ps 2>&1| grep STATUS>/dev/null; do  sleep 1; done;  >/dev/null
    docker ps -a | grep sandbox-hdp
    if [ $? -eq 0 ]; then
     docker start sandbox-hdp
    else
    docker run --name sandbox-hdp --hostname "sandbox.hortonworks.com" --privileged -d \
    -p 1111:111 \
    -p 1000:1000 \
    -p 1100:1100 \
    -p 1220:1220 \
    -p 1988:1988 \
    -p 2049:2049 \
    -p 2100:2100 \
    -p 2181:2181 \
    -p 3000:3000 \
    -p 4040:4040 \
    -p 4200:4200 \
    -p 4242:4242 \
    -p 5007:5007 \
    -p 5011:5011 \
    -p 6001:6001 \
    -p 6003:6003 \
    -p 6008:6008 \
    -p 6080:6080 \
    -p 6188:6188 \
    -p 8000:8000 \
    -p 8005:8005 \
    -p 8020:8020 \
    -p 8032:8032 \
    -p 8040:8040 \
    -p 8042:8042 \
    -p 8080:8080 \
    -p 8082:8082 \
    -p 8086:8086 \
    -p 8088:8088 \
    -p 8090:8090 \
    -p 8091:8091 \
    -p 8188:8188 \
    -p 8443:8443 \
    -p 8744:8744 \
    -p 8765:8765 \
    -p 8886:8886 \
    -p 8888:8888 \
    -p 8889:8889 \
    -p 8983:8983 \
    -p 8993:8993 \
    -p 9000:9000 \
    -p 9995:9995 \
    -p 9996:9996 \
    -p 10000:10000 \
    -p 10001:10001 \
    -p 10015:10015 \
    -p 10016:10016 \
    -p 10500:10500 \
    -p 10502:10502 \
    -p 11000:11000 \
    -p 15000:15000 \
    -p 15002:15002 \
    -p 16000:16000 \
    -p 16010:16010 \
    -p 16020:16020 \
    -p 16030:16030 \
    -p 18080:18080 \
    -p 18081:18081 \
    -p 19888:19888 \
    -p 21000:21000 \
    -p 33553:33553 \
    -p 39419:39419 \
    -p 42111:42111 \
    -p 50070:50070 \
    -p 50075:50075 \
    -p 50079:50079 \
    -p 50095:50095 \
    -p 50111:50111 \
    -p 60000:60000 \
    -p 60080:60080 \
    -p 15500:15500 \
    -p 15501:15501 \
    -p 15502:15502 \
    -p 15503:15503 \
    -p 15504:15504 \
    -p 15505:15505 \
    -p 2222:22 \
    sandbox-hdp /usr/sbin/sshd -D
    fi
    
    docker exec -t sandbox-hdp /bin/sh -c 'chown -R mysql:mysql /var/lib/mysql'
    docker exec -d sandbox-hdp service mysqld start
    docker exec -d sandbox-hdp service postgresql start
    docker exec -t sandbox-hdp ambari-server start
    docker exec -t sandbox-hdp ambari-agent start
    docker exec -t sandbox-hdp /bin/sh -c 'rm -f /usr/hdp/current/oozie-server/libext/falcon-oozie-el-extension-0.10.0.2.6.1.0-129.jar'
    docker exec -t sandbox-hdp /bin/sh -c 'chown -R hdfs:hadoop /hadoop/hdfs'
    
    echo "Waiting for ambari agent to connect"
    docker exec -t sandbox-hdp /bin/sh -c ' until curl --silent -u admin:4o12t0n -H "X-Requested-By:ambari" -i -X GET  http://localhost:8080/api/v1/clusters/Sandbox/hosts/sandbox.hortonworks.com/host_components/ZOOKEEPER_SERVER | grep state | grep -v desired | grep INSTALLED; do sleep 5; echo -n .; done;'
    
    echo "Waiting for ambari services to start "
    docker exec -t sandbox-hdp /bin/sh -c 'until curl --silent --user admin:4o12t0n -X PUT -H "X-Requested-By: ambari"  -d "{\"RequestInfo\":{\"context\":\"_PARSE_.START.HDFS\",\"operation_level\":{\"level\":\"SERVICE\",\"cluster_name\":\"Sandbox\",\"service_name\":\"HDFS\"}},\"Body\":{\"ServiceInfo\":{\"state\":\"STARTED\"}}}" http://localhost:8080/api/v1/clusters/Sandbox/services/HDFS | grep -i accept >/dev/null; do echo -n .; sleep 5; done;'
    
    docker exec -t sandbox-hdp /bin/sh -c 'until curl --silent --user admin:4o12t0n -X PUT -H "X-Requested-By: ambari"  -d "{\"RequestInfo\":{\"context\":\"_PARSE_.START.ALL_SERVICES\",\"operation_level\":{\"level\":\"CLUSTER\",\"cluster_name\":\"Sandbox\"}},\"Body\":{\"ServiceInfo\":{\"state\":\"STARTED\"}}}" http://localhost:8080/api/v1/clusters/Sandbox/services | grep -i accept > /dev/null; do sleep 5; echo -n .; done; '
    
    docker exec -t sandbox-hdp /bin/sh -c 'until /usr/bin/curl --silent --user admin:4o12t0n -H "X-Requested-By: ambari" "http://localhost:8080/api/v1/clusters/Sandbox/requests?to=end&page_size=10&fields=Requests" | tail -n 27 | grep COMPLETED | grep COMPLETED > /dev/null; do echo -n .; sleep 1; done;'
    
    docker exec -t sandbox-hdp su - hue -c '/bin/bash /usr/lib/tutorials/tutorials_app/run/run.sh &>/dev/null'
    docker exec -t sandbox-hdp su - hue -c '/bin/bash /usr/lib/hue/tools/start_scripts/update-tutorials.sh &>/dev/null'
    docker exec -t sandbox-hdp touch /usr/hdp/current/oozie-server/oozie-server/work/Catalina/localhost/oozie/SESSIONS.ser
    docker exec -t sandbox-hdp chown oozie:hadoop /usr/hdp/current/oozie-server/oozie-server/work/Catalina/localhost/oozie/SESSIONS.ser
    docker exec -d sandbox-hdp /etc/init.d/tutorials start
    docker exec -d sandbox-hdp /etc/init.d/splash
    docker exec -d sandbox-hdp /etc/init.d/shellinaboxd start
    '''
    
(5) Connect to the welcome screen
    * Append the port number :8888 to your host address, open your browser, and access Sandbox Welcome page at http://_host_:8888/.
      
    In single machine, just replace _host_ with 127.0.0.1 (http://127.0.0.1:8888/#)
    
    Username: raj_ops
    Password:
    
    Copy file to docker: docker cp sandbox-hdp:/root/build.out ~/workspace/
    
    
    (5.1) SSH Client
        Use the SSH client of your choice:
        
        ssh root@127.0.0.1 -p 2222
        
        username/password: root / hadoop
        
        
        If no SSH client is installed, use the built-in web client:
        
        http://127.0.0.1:4200
        
        Copy file from docker: docker cp sandbox-hdp:/root/build.out ~/workspace/
        Copy file to docker:   docker cp ~/cookie.txt sandbox-hdp:/root/chen/
        
        
    (5.2) Zeppelin
        http://127.0.0.1:9995
        
        
    (5.3) Ranger
        http://127.0.0.1:6080     
    
        username & password: raj_ops
                
                
    (5.4) WORKFLOW MANAGER
        http://127.0.0.1:8080/#/main/views/WORKFLOW_MANAGER/1.0.0/workflow_manager
        
        username & password: raj_ops
                        
                        
    (5.5) Ambari
        http://127.0.0.1:8080
        
        Cluster Operator
        username & password: raj_ops
        
        Ambari Admin 
        Get instructions to set password
        
        Note: Ambari supports multiple roles


(6) Get Word Count working
    * Execute MyTest
    * Code will read <input.txt>, and count words. Write results in <output.txt>
    
(7) Hadoop -> HDFS (Storage Subsystem), YARN (Process Scheduling Subsystem)

    Data Locality: processing data locally whenever possible. Core to Hadoop, intentionally attempts to minimize the amount of data transferred across the network.
    Shared Nothing: refers to tasks  that can be executed independently on a distributed processing platform such as Hadoop that do not require synchronization or sharing of state wth one another
    Core Components of Hadoop: HDFS, YARN
    
    HDFS
        '''
        Hadoop applies Master-Slave Cluster Architecture (the master and slave process are predefined, static role for the lifetime)
        
        File, Blocks, and Metadata
        
        NameNode: is a madatory process necessary for HDFS to operate. 
            * HDFS Master Node process which manage the filesystem
            * http://127.0.0.1:50070/dfshealth.html#tab-overview  (local Hadoop UI)
            * Tip: Data Does Not Go Through the NameNode
                 It is a common misconception that data goes into HDFS via the
                 NameNode. This is not the case as it would create a bottleneck.
                 Instead, the client interacts with the NameNode to get directives on
                 which cluster node(s) to communicate with to get or put blocks that
                 pertain to the file the client is trying to read or write.
        
        SecondaryNameNode/Standby NameNode: are optional processes that expedite filesystem recovery or provide a failover process in the event of a NameNode failure.
        
        DataNode: DetaNode process is a HDFS slave node daemon that runs on one or more nodes of the HDFS clusters.
            * The DataNodes are the nodes of the cluster on which HDFS blocks are stored and managed
            * Caution: DataNodes Are Not Aware of HDFS Files and Directories
                DataNodes store and manage physical HDFS blocks only, without
                having any knowledge of how these blocks are related to files and
                directories in the HDFS filesystem. This relationship is held only in
                the NameNode’s metadata.
        '''
        
    YARN
        '''
        YARN governs the processing of data in Hadoop, which typically is data sourced from and written to HDFS. 
        ResourceManager is the master node
            * http://127.0.0.1:8088/cluster
            
        NodeManager is the slave node (can be more than one)
            * ApplicataionMaster is the first container allocated by the ResourceManager to run on a NodeManager for an application
        '''
    
    LocalJobRunner is typically used for map reduce development and unit testing.
    
    HDFS is a virtual filesystem, meaning that it appears to a client as if it is one system, but the underlying data is located in multiple different locations.
    Blocks are actually distributed.
    
    WORM: Write Once, Read Many. Immutability refers to the inabilty to update data after it is committed to the filesystem
    
    HDFS -> Files -> Block (default to 128M)
    
    NameNode Meetadata: it a is perhaps the single most critical component in the HDFS architecture. Without it the filesystem is not accessible, usable, or
                           recoverable. The NameNode’s metadata contains the critical link between blocks stored on each of the DataNodes’ respective filesystems 
                           and their context within files and directories in the Hadoop virtual filesystem.
                           
                           NameNode simply uses its metadata to direct clients where to read and write data —specifically which DataNodes to interact with. 
                           The clients then perform data operations directly with the specified DataNodes in the cluster.
                           
                           Objects in HDFS have associated ACLs, which define the owner of the object and permissions that the owner, groups, and others have to the object.
                           These ACLs are part of the metadata structure as previously shown.
    
    Apache Flume: is a Hadoop ecosystem project originally developed by Cloudera designed to capture, transform, and ingest data into HDFS using one or more agents. 
    In many cases you will need to establish interfaces to capture data produced from source systems in real time, such as web logs, or schedule batch snapshots from a relational database, 
    such as a transaction processing system.
    
    Sqoop: (which is a portmanteau for “sql-to-hadoop”) is a top-level ASF project designed to source data from a relational database and ingest this data into files
    (typically delimited files) in HDFS. Sqoop can also be used to send data from Hadoop to a relational database, useful for sending results processed in Hadoop to an operational transaction processing
    system. Sqoop integrates with Hive, which is a SQLabstraction to MapReduce and other processing technologies on the Hadoop platform. Sqoop can automatically create Hive tables from imported 
    data from a RDBMS (Relational Database Management System) table.
    
    Sqoop includes tools for the following operations:
        * Listing databases and tables on a database system
        * Importing a single table from a database system, including
        * Specifying which columns to import
        * Specifying which rows to import using a WHERE clause
        * Importing data from one or more tables using a SELECT statement
        * Incremental imports from a table on a database system (importing only what has changed since a known previous state)
        * Exporting of data from HDFS to a table on a remote database system
    
    The server-based implementation of Sqoop is called Sqoop2.
    More information on Sqoop including Sqoop2 (Sqoop-as-a-service) is available from any of the following sources:
        * Apache Sqoop (http://sqoop.apache.org/)
        * Cloudera (http://www.cloudera.com/)
        * Hortonworks (http://hortonworks.com/)
        * MapR (https://www.mapr.com/)
    
    
    
    
    
    