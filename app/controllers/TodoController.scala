package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Task
import dao.TaskDAO
import java.time.LocalDate
import scala.concurrent.ExecutionContext

@Singleton
class TodoController @Inject()(cc: ControllerComponents, taskDAO: TaskDAO)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  implicit val taskFormat = Json.format[Task]

  def getAllTasks: Action[AnyContent] = Action.async {
    taskDAO.getAllTasks.map(tasks => Ok(Json.toJson(tasks)))
  }

  def getTaskById(id: Long): Action[AnyContent] = Action.async {
    taskDAO.getTaskById(id).map {
      case Some(task) => Ok(Json.toJson(task))
      case None => NotFound(Json.obj("message" -> "Task not found"))
    }
  }

  def createTask: Action[Task] = Action.async(parse.json[Task]) { request =>
    taskDAO.createTask(request.body).map { createdTask =>
      Created(Json.toJson(createdTask))
    }
  }

  def updateTask(id: Long): Action[Task] = Action.async(parse.json[Task]) { request =>
    val updatedTask = request.body.copy(Id = id, UpdatedDate = LocalDate.now())
    taskDAO.updateTask(updatedTask).map { _ =>
      Ok(Json.toJson(updatedTask))
    }
  }

  def deleteTask(id: Long): Action[AnyContent] = Action.async {
    taskDAO.deleteTask(id).map { _ =>
      Ok(Json.obj("message" -> "Task deleted"))
    }
  }
}
