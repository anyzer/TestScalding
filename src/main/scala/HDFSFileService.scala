/**
  * Created by guoch on 30/10/17.
  * http://bleibinha.us/blog/2013/09/accessing-the-hadoop-distributed-filesystem-hdfs-with-scala
  */
import java.io.{FileSystem => _, _}
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

object HDFSFileService {
  private val conf = new Configuration()
  private val hdfsCoreSitePath = new Path("conf/core-site.xml")
  private val hdfsHDFSSitePath = new Path("conf/hdfs-site.xml")

  conf.addResource(hdfsCoreSitePath)
  conf.addResource(hdfsHDFSSitePath)
  private val fileSystem = FileSystem.get(conf)

  System.setProperty("HADOOP_USER_NAME", "cheng")

  private val testfilePath = "src/main/resources/"

  /**
    * Upload to Hadoop Server
    * @param source
    * @param dest
    * @return
    */
  def write(source: String, dest: String): Boolean = {
    // Get the filename out of the file path
    val filename = source.substring(source.lastIndexOf('/') + 1, source.length())

    // Create the destination path including the filename.
    var destination = ""
    if (dest.charAt(dest.length() - 1) != '/') {
      destination = dest + "/" + filename;
    } else {
      destination = dest + filename;
    }

    println("Source: " + source + "; Destination: " + destination)
    println("FileName: " + filename)

    val path = new Path(destination)
    if(fileSystem.exists(path)){
      println("File " + destination + " already exists")
      return true;
    }

    try {

      val out = fileSystem.create(path); //FSDataOutputStream
      val in = new BufferedInputStream(new FileInputStream(new File(source))) //InputStream

      println("Haha")

      val byte = new Array[Byte](1024)
      var numBytes = in.read(byte)

      while (numBytes > 0) {
        out.write(byte, 0, numBytes)
      }

      println("Hehe")

      in.close()
      out.close()
      fileSystem.close()

      return true

    }catch {
      case e: Exception => println("Cannot write to Hadoop Server: \n" + e.getMessage)
      return false
    }
  }


  /**
    * Download file from HDFS Server
    * @param file
    * @param dest
    * @return
    */
  def readFile(file: String, dest: String): Boolean = {
    val path = new Path(file);
    if (!fileSystem.exists(path)) {
      System.out.println("File " + file + " does not exists");
      return true;
    }

    val in = fileSystem.open(path) // FSDataInputStream
    val filename = file.substring(file.lastIndexOf('/') + 1, file.length());

    var out = new BufferedOutputStream(new FileOutputStream(new File(filename))) //OutputStream

    val byte = new Array[Byte](1024)
    var numBytes = in.read(byte)

    while (numBytes > 0) {
      out.write(byte, 0, numBytes);
    }

    in.close()
    out.close()
    fileSystem.close()

    return true
  }

  /**
    * Delete File in Hadoop Server
    * @param file file name and path
    * @return
    */
  def deleteFile(file: String): Boolean = {
    val path = new Path(file);
    if (!fileSystem.exists(path)) {
      System.out.println("File " + file + " does not exists");
      return false
    }

    fileSystem.delete(new Path(file), true)
    fileSystem.close()

    return true
  }

  def mkdir(dir: String): Boolean = {
    val path = new Path(dir);
    if (fileSystem.exists(path)) {
      System.out.println("Dir " + dir + " already not exists");
      return true;
    }

    fileSystem.mkdirs(path);
    fileSystem.close();

    return true
  }


//  def saveFile(fileName: String): Unit = {
//    System.setProperty("HADOOP_USER_NAME", "cheng")
//
//    val file = new File(testfilePath + fileName)
//    val out = fileSystem.create(new Path(file.getName))
//    val in = new BufferedInputStream(new FileInputStream(file))
//    var b = new Array[Byte](1024)
//    var numBytes = in.read(b)
//    while (numBytes > 0) {
//      out.write(b, 0, numBytes)
//      numBytes = in.read(b)
//    }
//    in.close()
//    out.close()
//  }
//
//  def removeFile(filename: String): Boolean = {
//    val path = new Path(filename)
//    fileSystem.delete(path, true)
//  }
//
//  def getFile(filename: String): InputStream = {
//    val path = new Path(filename)
//    fileSystem.open(path)
//  }
//
//  def createFolder(folderPath: String): Unit = {
//    val path = new Path(folderPath)
//    if (!fileSystem.exists(path)) {
//      fileSystem.mkdirs(path)
//    }
//  }
}