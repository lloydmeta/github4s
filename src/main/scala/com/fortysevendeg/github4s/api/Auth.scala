package com.fortysevendeg.github4s.api

import java.util.UUID

import cats.data.Xor
import com.fortysevendeg.github4s.GithubResponses.{ GHResult, GHResponse }
import com.fortysevendeg.github4s.free.domain._
import com.fortysevendeg.github4s.HttpClient
import io.circe.generic.auto._
import io.circe.syntax._
import scalaj.http.HttpConstants._

/** Factory to encapsulate calls related to Auth operations  */
object Auth {

  protected val httpClient = new HttpClient()

  val authorizeUrl = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"
  val accessTokenUrl = "https://github.com/login/oauth/access_token"

  /**
    * Call to request a new authorization given a basic authentication, the returned object Authorization includes an
    * access token
    *
    * @param username the username of the user
    * @param password the password of the user
    * @param scopes attached to the token
    * @param note to remind you what the OAuth token is for
    * @param client_id the 20 character OAuth app client key for which to create the token
    * @param client_secret the 40 character OAuth app client secret for which to create the token
    * @return GHResponse[Authorization] new authorization with access_token
    */
  def newAuth(
    username: String,
    password: String,
    scopes: List[String],
    note: String,
    client_id: String,
    client_secret: String
  ): GHResponse[Authorization] =
    httpClient.postAuth[Authorization](
      method  = "authorizations",
      headers = Map("Authorization" → s"Basic ${base64(s"$username:$password")}"),
      data    = NewAuthRequest(scopes, note, client_id, client_secret).asJson.noSpaces
    )

  /**
    * Generates the authorize url with a random state, both are returned within Authorize object
    *
    * @param client_id the 20 character OAuth app client key for which to create the token
    * @param redirect_uri the URL in your app where users will be sent after authorization
    * @param scopes attached to the token
    * @return GHResponse[Authorize] new state: first step oAuth
    */
  def authorizeUrl(
    client_id: String,
    redirect_uri: String,
    scopes: List[String]
  ): GHResponse[Authorize] = {
    val state = UUID.randomUUID().toString
    Xor.Right(
      GHResult(
        value      = Authorize(authorizeUrl.format(client_id, redirect_uri, scopes.mkString(","), state), state),
        statusCode = 200,
        headers    = Map.empty
      )
    )
  }

  /**
    * Requests an access token based on the code retrieved in the first step of the oAuth process
    *
    * @param client_id the 20 character OAuth app client key for which to create the token
    * @param client_secret the 40 character OAuth app client secret for which to create the token
    * @param code the code you received as a response to Step 1
    * @param redirect_uri the URL in your app where users will be sent after authorization
    * @param state the unguessable random string you optionally provided in Step 1
    * @return GHResponse[OAuthToken] new access_token: second step oAuth
    */
  def getAccessToken(
    client_id: String,
    client_secret: String,
    code: String,
    redirect_uri: String,
    state: String
  ): GHResponse[OAuthToken] = httpClient.postOAuth[OAuthToken](
    url  = accessTokenUrl,
    data = NewOAuthRequest(client_id, client_secret, code, redirect_uri, state).asJson.noSpaces
  )

}
