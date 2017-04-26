/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.free.algebra

import github4s.GithubResponses._
import github4s.free.domain.{Pagination, User}

import freestyle._

/**
 * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
@free trait UserOps {

  def getUser(username: String, accessToken: Option[String]): FS[GHResponse[User]]

  def getAuthUser(accessToken: Option[String]): FS[GHResponse[User]]

  def getUsers(
      since: Int,
      pagination: Option[Pagination],
      accessToken: Option[String]): FS[GHResponse[List[User]]]

}
