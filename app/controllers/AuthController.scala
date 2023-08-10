package controllers

import javax.inject._
import play.api.mvc._
import Tokenutils.JwtUtil

@Singleton
class AuthController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def login(userId: Long) = Action { implicit request =>
    val token = JwtUtil.generateToken(userId)
    Ok(s"Token Generated - $token")
  }

  def refreshToken(token: String) = Action { implicit request =>
    JwtUtil.validateToken(token) match {
      case Some(userId) =>
        val newToken = JwtUtil.generateToken(userId)
        Ok(s"New Token generated - $newToken")
      case None =>
        Unauthorized("Invalid token")
    }
  }

  def validateToken(token: String) = Action { implicit request =>
    if (JwtUtil.isTokenRevoked(token)) {
      Unauthorized("Revoked token")
    } else {
      JwtUtil.validateToken(token) match {
        case Some(userId) => Ok("Valid token")
        case None => Unauthorized("Invalid token")
      }
    }
  }

  def logout(token: String) = Action { implicit request =>
    JwtUtil.revokeToken(token)
    Ok("Token revoked")
  }
}
