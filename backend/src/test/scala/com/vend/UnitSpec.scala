//package com.vend
//
//import java.time.{Instant, Period}
//
//import net.minidev.json.JSONObject
//import org.scalamock.scalatest.MixedMockFactory
//import org.scalatest.{BeforeAndAfterAll, OptionValues}
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.flatspec.AnyFlatSpec
//import org.springframework.security.oauth2.jwt.Jwt
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
//
//import scala.jdk.CollectionConverters._
//
//abstract class UnitSpec
//    extends AnyFlatSpec
//    with Matchers
//    with OptionValues
//    with MixedMockFactory
//    with BeforeAndAfterAll {
//
//  val mockLogger: LoggingPort = mock[LoggingPort]
//
//  private val id = "id"
//
//  private val email = Option("email")
//
//  private val name = Option("name")
//
//  private val orgId = "orgId"
//
//  private val platform = Option("platform")
//
//  private val pushNotificationChannel = Option(s"private-$id")
//
//  private val timesRenewed = 0
//
//  val timeCreated = Instant.now()
//
//  val testUserSession: UserSession = UserSession(
//    id,
//    email,
//    name,
//    orgId,
//    platform,
//    pushNotificationChannel,
//    timesRenewed,
//    timeCreated
//  )
//
//  val testExpiringSession: ExpiringSession = ExpiringSession(testUserSession.id)
//
//  override def beforeAll(): Unit = {
//    val claims = Map("id" -> id, "email" -> email.get, "name" -> name.get, "orgId" -> orgId, "platform" -> platform.get)
//
//    val jwt = new Jwt(
//      "tokenValue",
//      timeCreated,
//      timeCreated.plus(Period.ofDays(1)),
//      Map[String, AnyRef]("headers" -> "headers").asJava,
//      Map[String, AnyRef](NAMESPACE -> new JSONObject(claims.asJava)).asJava
//    )
//
//    val jwtAuthenticationToken = new JwtAuthenticationToken(jwt)
//
//    SecurityContextHolder.getContext.setAuthentication(jwtAuthenticationToken)
//  }
//
//}
