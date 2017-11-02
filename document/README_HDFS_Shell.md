https://dzone.com/articles/top-10-hadoop-shell-commands

1. Copy from and to local. From remote should use: hadoop fs -copyFromLocal build.out hdfs://sandbox.hortonworks.com:8020/user/build.out
   * hadoop fs -copyFromLocal build.out /user/build.out
   * hadoop fs -copyToLocal /user/build_.out build_1.out

2. Makedir
   * hadoop fs -mkdir /guoch

3. List all folders and files
   * hadoop fs -ls /.
   * hadoop fs -ls file:///
   
   
   
   