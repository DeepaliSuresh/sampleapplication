package aws

import com.typesafe.config.{Config, ConfigFactory}
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

object AwsS3Config {
  // Load the configuration from application.conf
  private val config: Config = ConfigFactory.load()

  // Read the AWS credentials and region from the config
  val region: Region = Region.of(config.getString("aws.region"))
  val accessKeyId: String = config.getString("aws.accessKeyId")
  val secretAccessKey: String = config.getString("aws.secretAccessKey")

  // Create the credentials provider and S3 client
  val credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey))
  val s3Client: S3Client = S3Client.builder()
    .region(region)
    .credentialsProvider(credentialsProvider)
    .build()
}


