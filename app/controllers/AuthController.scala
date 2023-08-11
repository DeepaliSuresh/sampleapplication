package controllers

import Tokenutils.JwtUtil
import dao.UserDAO
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents, userDAO: UserDAO)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /*
  // without username and password
  def login() = Action { implicit request =>
    val token = JwtUtil.generateToken()
    Ok(s"Token Generated - $token")
  }
   */


  def login(): Action[AnyContent] = Action.async { implicit request =>
    val jsonBody = request.body.asJson.getOrElse(Json.obj())
    val usernameOpt = (jsonBody \ "username").asOpt[String]
    val passwordOpt = (jsonBody \ "password").asOpt[String]

    (usernameOpt, passwordOpt) match {
      case (Some(username), Some(password)) =>
        userDAO.findUserByUsername(username).map {
          case Some(user) if user.password == password =>
            val token = JwtUtil.generateToken()
            Ok(Json.obj("token" -> token))
          case _ =>
            Unauthorized(Json.obj("error" -> "Invalid credentials"))
        }
      case _ =>
        Future.successful(BadRequest(Json.obj("error" -> "Missing or invalid input")))
    }
  }


  def refreshToken(token: String) = Action { implicit request =>
    JwtUtil.validateToken(token) match {
      case true =>
        val newToken = JwtUtil.generateToken()
        Ok(s"New Token generated - $newToken")
      case false =>
        Unauthorized("Invalid token")
    }
  }

  def validateToken(token: String) = Action { implicit request =>
    if (JwtUtil.validateToken(token)) {
      Ok("Valid token")
    } else {
      Unauthorized("Invalid token")
    }
  }

  def logout(token: String) = Action { implicit request =>
    JwtUtil.revokeToken(token)
    Ok("Token revoked")
  }
}
