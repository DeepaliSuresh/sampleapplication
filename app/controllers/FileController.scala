package controllers

import javax.inject._
import play.api.mvc._
import aws.S3Helper

@Singleton
class FileController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def uploadFileToS3() = Action { implicit request =>
    val bucketName = "deepali-samplebucket"
    val folderName = "sample"

    // Upload the built.sbt file to S3
    S3Helper.uploadfile(bucketName, folderName)

    // Return a success response
    Ok("File uploaded to S3 bucket.")

  }

  def downloadFileFromS3() = Action { implicit request =>
    val bucketName = "deepali-samplebucket"
    val folderName = "sample"
    val fileName = "sample-document2" // Replace with the actual file name
    val localDestinationPath = "/Users/deepali.suresh/Documents/python/sample2" // Replace with your desired local path

    // Download the file from S3
    S3Helper.downloadfile(bucketName, folderName, fileName, localDestinationPath)

    // Return a success response or redirect to a success page
    Ok(s"File downloaded from S3 and saved to: $localDestinationPath")
  }
}
