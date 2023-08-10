package Tokenutils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import java.util.Date
import scala.collection.mutable

object JwtUtil {
  private val secretKey = "mnbvcxzlkjhgfdsapoiuytrewq"
  private val algorithm = Algorithm.HMAC256(secretKey)
  private val revokedTokens: mutable.Set[String] = mutable.HashSet[String]()

  def generateToken(userId: Long): String = {
    val expirationTime = new Date(System.currentTimeMillis() + 300000)
    val token = JWT.create()
      .withClaim("userId", userId.toString)
      .withExpiresAt(expirationTime)
      .sign(algorithm)
    token
  }

  def validateToken(token: String): Option[Long] = {
    try {
      val verifier = JWT.require(algorithm).build()
      val decoded = verifier.verify(token)
      val userId = decoded.getClaim("userId").asString().toLong
      Some(userId)
    } catch {
      case _: Exception => None
    }
  }

  def revokeToken(token: String): Unit = {
    revokedTokens += token
  }

  def isTokenRevoked(token: String): Boolean = {
    revokedTokens.contains(token)
  }

}
