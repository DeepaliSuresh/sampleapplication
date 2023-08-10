package controllers

import javax.inject._
import play.api.mvc._
import aws.S3Helper

@Singleton
class FileController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def uploadFileToS3() = Action { implicit request =>
    val bucketName = "deepali-samplebucket"
    val folderName = "sample"


    S3Helper.uploadfile(bucketName, folderName)

    
    Ok("File uploaded to S3 bucket.")

  }

  def downloadFileFromS3() = Action { implicit request =>
    val bucketName = "deepali-samplebucket"
    val folderName = "sample"
    val fileName = "sample-document2" 
    val localDestinationPath = "/Users/deepali.suresh/Documents/python/sample3" 

    
    S3Helper.downloadfile(bucketName, folderName, fileName, localDestinationPath)

    
    Ok(s"File downloaded from S3 and saved to: $localDestinationPath")
  }

def deleteFileFromS3() = Action { implicit request =>
  val bucketName = "deepali-samplebucket"
  val folderName = "sample"
  val fileName = "sample-document2" 

  
  S3Helper.deleteFile(bucketName, folderName, fileName)

  
  Ok(s"File '$fileName' deleted from S3 bucket.")
}
}

