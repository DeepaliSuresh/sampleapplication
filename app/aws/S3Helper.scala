package aws

import software.amazon.awssdk.services.s3.model.{DeleteObjectRequest, GetObjectRequest, PutObjectRequest, PutObjectResponse, S3Exception}

import java.io.IOException
import java.nio.file.Paths

object S3Helper {
  def uploadfile(bucketName: String, folderName: String): PutObjectResponse = {
    val key = s"$folderName/sample-document4"
    val localFilePath = "/Users/deepali.suresh/Documents/Session Tracking" // Assuming built.sbt is present in the root of your project

    val request = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    val fileToUpload = Paths.get(localFilePath)
    AwsS3Config.s3Client.putObject(request, fileToUpload)
  }

  def downloadfile(bucketName: String, folderName: String, fileName: String, localDestinationPath: String): Unit = {
    val key = s"$folderName/$fileName"

    val request = GetObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    val localFile = Paths.get(localDestinationPath)
    try {
      val getObjectResponse = AwsS3Config.s3Client.getObject(request, localFile)
      println(s"File downloaded to: $localDestinationPath")
    } catch {
      case ex: S3Exception if ex.statusCode() == 404 =>
        println(s"File '$fileName' not found in the S3 bucket.")
      case ex: IOException =>
        println(s"Error downloading file: ${ex.getMessage}")
    }
  }
def deleteFile(bucketName: String, folderName: String, fileName: String): Unit = {
  val key = s"$folderName/$fileName"

  val request = DeleteObjectRequest.builder()
    .bucket(bucketName)
    .key(key)
    .build()

  try {
    AwsS3Config.s3Client.deleteObject(request)
    println(s"File '$fileName' deleted from the S3 bucket.")
  } catch {
    case ex: S3Exception if ex.statusCode() == 404 =>
      println(s"File '$fileName' not found in the S3 bucket.")
    case ex: IOException =>
      println(s"Error deleting file: ${ex.getMessage}")
  }
}
}