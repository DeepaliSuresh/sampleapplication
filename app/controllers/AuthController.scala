package controllers

import javax.inject._
import play.api.mvc._
import Tokenutils.JwtUtil

@Singleton
class AuthController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def login() = Action { implicit request =>
    val token = JwtUtil.generateToken()
    Ok(s"Token Generated - $token")
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

