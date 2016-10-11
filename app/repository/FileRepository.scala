package repository

import java.io.FileInputStream
import java.net.URLConnection

import com.mongodb.casbah.gridfs.{GridFS, GridFSDBFile}
import conf.connection.HashDB
import domain.FileMeta
import org.bson.types.ObjectId

/**
  * Created by hashcode on 2015/09/29.
  */
object FileRepository {
  val gridFs = GridFS(HashDB.getGridFsConnection())

  def getFileType(name: String): String = {
    URLConnection.guessContentTypeFromName(name)
  }

  def save(file: FileInputStream,meta:FileMeta):ObjectId = {
    val fileId = gridFs(file) { f =>
      f.filename = meta.fileName
      f.contentType = meta.contentType
    }

    fileId.get.asInstanceOf[ObjectId]
  }

  def getFileById(id: String): Option[GridFSDBFile] = {
    gridFs.findOne(new ObjectId(id))
  }

  def getFilesByName(fileName: String): Option[GridFSDBFile] = {
    gridFs.findOne(fileName)

  }

  def deleteFileById(id: String) = {
    gridFs.remove(new ObjectId(id))
  }

  def deleteFilesByName(fileName: String) = {
    gridFs.remove(fileName)
  }

}
