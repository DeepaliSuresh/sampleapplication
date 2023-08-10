package Tokenutils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import java.util.Date
import scala.collection.mutable

object JwtUtil {
  private val secretKey = "mnbvcxzlkjhgfdsapoiuytrewq"
  private val algorithm = Algorithm.HMAC256(secretKey)
  private val activeTokens: mutable.Set[String] = mutable.HashSet[String]()

  def generateToken(): String = {
    val expirationTime = new Date(System.currentTimeMillis() + 300000)
    val token = JWT.create()
      .withExpiresAt(expirationTime)
      .sign(algorithm)

    activeTokens += token
    token
  }

  def validateToken(token: String): Boolean = {
    if (activeTokens.contains(token)) {
      try {
        val verifier = JWT.require(algorithm).build()
        verifier.verify(token)
        true
      } catch {
        case _: Exception => false
      }
    } else {
      false
    }
  }

  def revokeToken(token: String): Unit = {
    activeTokens -= token
  }
}
