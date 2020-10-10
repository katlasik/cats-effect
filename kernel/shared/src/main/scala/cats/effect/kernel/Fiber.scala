/*
 * Copyright 2020 Typelevel
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

package cats.effect.kernel

import cats.syntax.all._

trait Fiber[F[_], E, A] {
  def cancel: F[Unit]
  def join: F[Outcome[F, E, A]]

  def joinAndEmbed(onCancel: F[A])(implicit F: MonadCancel[F, E]): F[A] =
    join.flatMap(_.embed(onCancel))

  def joinAndEmbedNever(implicit F: GenSpawn[F, E]): F[A] =
    joinAndEmbed(F.never)
}