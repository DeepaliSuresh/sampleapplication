package dao

import java.time.LocalDate
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import models.Task
import play.mvc.Action
import play.mvc.BodyParser.AnyContent
import play.mvc.Security.AuthenticatedAction

class TaskDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class TasksTable(tag: Tag) extends Table[Task](tag, "Task_Table") {
    def Id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def TaskId = column[String]("TaskId")
    def Owner = column[String]("Owner")
    def Assignee = column[String]("Assignee")
    def EstimatedDate = column[LocalDate]("EstimatedDate")
    def Status = column[String]("Status")
    def CreatedDate = column[LocalDate]("CreatedDate")
    def UpdatedDate = column[LocalDate]("UpdatedDate")
    def Flag = column[Boolean]("Flag")

    def * = (Id, TaskId, Owner, Assignee, EstimatedDate, Status, CreatedDate, UpdatedDate, Flag).mapTo[Task]
  }

  private val tasks = TableQuery[TasksTable]

  def getAllTasks: Future[Seq[Task]] = db.run {
    tasks.result
  }

  def getTaskById(id: Long): Future[Option[Task]] = db.run {
    tasks.filter(_.Id === id).result.headOption
  }

  def createTask(task: Task): Future[Task] = db.run {
    (tasks returning tasks.map(_.Id) into ((task, id) => task.copy(Id = id))) += task
  }

  def updateTask(updatedTask: Task): Future[Int] = db.run {
    tasks.filter(_.Id === updatedTask.Id).update(updatedTask)
  }

  def deleteTask(id: Long): Future[Int] = db.run {
    tasks.filter(_.Id === id).delete
  }
}
