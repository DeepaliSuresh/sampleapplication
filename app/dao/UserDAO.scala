package dao

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}
import models.User

class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "user_info") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")

    def * = (id, username, password) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]

  def findUserByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)
}

