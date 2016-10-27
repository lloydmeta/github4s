/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s.integration

import cats.Id
import cats.implicits._
import github4s.Github._
import github4s.implicits._
import github4s.{Github, ImplicitsJS}
import github4s.utils.TestUtils
import org.scalatest._
import fr.hmil.roshttp.HttpResponse
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GHUsersSpec extends AsyncFlatSpec with Matchers with TestUtils with ImplicitsJS {

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = Github(accessToken).users.get(validUsername).exec[Future, HttpResponse]
    response should be('right)
    response map { r ⇒
      r.toOption map { rr =>
        rr.result.login shouldBe validUsername
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

  it should "return error on Left for invalid username" in {
    val response = Github(accessToken).users.get(invalidUsername).exec[Future, HttpResponse]
    response should be('left)
  }

  "Users >> GetAuth" should "return error on Left when no accessToken is provided" in {
    val response = Github().users.getAuth.exec[Future, HttpResponse]
    response should be('left)
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = Github(accessToken).users.getUsers(validSinceInt).exec[Future, HttpResponse]
    response should be('right)

    response map { r ⇒
      r.toOption map { rr =>
        rr.result.nonEmpty shouldBe true
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response =
      Github(accessToken).users.getUsers(invalidSinceInt).exec[Future, HttpResponse]
    response should be('right)

    response map { r ⇒
      r.toOption map { rr =>
        rr.result.isEmpty shouldBe true
        rr.statusCode shouldBe okStatusCode
      } match {
        case _ => succeed
      }
    }
  }

}
