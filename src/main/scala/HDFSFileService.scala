/**
  * Created by guoch on 30/10/17.
  * http://bleibinha.us/blog/2013/09/accessing-the-hadoop-distributed-filesystem-hdfs-with-scala
  */
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

object HDFSFileService {
  private val conf = new Configuration()
  private val hdfsCoreSitePath = new Path("conf/core-site.xml")
  private val hdfsHDFSSitePath = new Path("conf/hdfs-site.xml")

  conf.addResource(hdfsCoreSitePath)
  conf.addResource(hdfsHDFSSitePath)

  private val fileSystem = FileSystem.get(conf)

  def saveFile(filepath: String): Unit = {
    val file = new File(filepath)
    val out = fileSystem.create(new Path(file.getName))
    val in = new BufferedInputStream(new FileInputStream(file))
    var b = new Array[Byte](1024)
    var numBytes = in.read(b)
    while (numBytes > 0) {
      out.write(b, 0, numBytes)
      numBytes = in.read(b)
    }
    in.close()
    out.close()
  }

  def removeFile(filename: String): Boolean = {
    val path = new Path(filename)
    fileSystem.delete(path, true)
  }

  def getFile(filename: String): InputStream = {
    val path = new Path(filename)
    fileSystem.open(path)
  }

  def createFolder(folderPath: String): Unit = {
    val path = new Path(folderPath)
    if (!fileSystem.exists(path)) {
      fileSystem.mkdirs(path)
    }
  }
}