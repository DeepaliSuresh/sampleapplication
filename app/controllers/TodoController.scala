package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Task
import dao.TaskDAO
import Tokenutils.JwtUtil

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoController @Inject()(cc: ControllerComponents, taskDAO: TaskDAO)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  implicit val taskFormat = Json.format[Task]
  private def isAuthenticated[A](action: Action[A]): Action[A] = Action.async(action.parser) { request =>
    request.headers.get("Authorization") match {
      case Some(token) if JwtUtil.validateToken(token) =>
        action(request)
      case _ =>
        Future.successful(Unauthorized(Json.obj("message" -> "Unauthorized Access")))
    }
  }

  def getAllTasks: Action[AnyContent] = isAuthenticated {
    Action.async {
      taskDAO.getAllTasks.map(tasks => Ok(Json.toJson(tasks)))
    }
  }

  def getTaskById(id: Long): Action[AnyContent] = isAuthenticated {
    Action.async {
      taskDAO.getTaskById(id).map {
        case Some(task) => Ok(Json.toJson(task))
        case None => NotFound(Json.obj("message" -> "Task not found"))
      }
    }
  }

  def createTask: Action[JsValue] = isAuthenticated {
    Action.async(parse.json) { request =>
      request.body.validate[Task] match {
        case JsSuccess(task, _) =>
          val newTask = task.copy(
            Id = 0, // Set the Id to 0 as it will be generated automatically by the database
            CreatedDate = LocalDate.now(),
            UpdatedDate = LocalDate.now()
          )
          taskDAO.createTask(newTask).map { createdTask =>
            Created(Json.toJson(createdTask))
          }
        case JsError(errors) =>
          Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      }
    }
  }

    def updateTask(id: Long): Action[Task] = isAuthenticated {
      Action.async(parse.json[Task]) { request =>
        val updatedTask = request.body.copy(Id = id, UpdatedDate = LocalDate.now())
        taskDAO.updateTask(updatedTask).map { _ =>
          Ok(Json.toJson(updatedTask))
        }
      }
    }

    def deleteTask(id: Long): Action[AnyContent] = isAuthenticated {
      Action.async {

        taskDAO.deleteTask(id).map { _ =>
          Ok(Json.obj("message" -> "Task deleted"))
        }
      }
    }
  }

