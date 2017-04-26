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

import github4s.GithubResponses.GHResponse
import github4s.free.domain.{CombinedStatus, Status}

import freestyle._

/**
 * Exposes Status operations as a Free monadic algebra that may be combined with other Coproducts
 */
@free trait StatusOps {
  def getCombinedStatus(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String]
  ): FS[GHResponse[CombinedStatus]]

  def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String]
  ): FS[GHResponse[List[Status]]]

  def createStatus(
      owner: String,
      repo: String,
      sha: String,
      state: String,
      target_url: Option[String],
      description: Option[String],
      context: Option[String],
      accessToken: Option[String]
  ): FS[GHResponse[Status]]
}
