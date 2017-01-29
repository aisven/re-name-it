/*
 * Copyright 2017 Sven Ludwig
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
package net.sourcekick.renameit

import java.nio.file.{Files, Paths}

import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

private object ReNameItSpec {

  private def determineRootPath: String = {
    val url = Thread.currentThread().getContextClassLoader.getResource("testcontent.txt")

    url.getPath.replace("/testcontent.txt", "")
  }

}

class ReNameItSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  override def beforeAll = {
    cleanUp
  }

  override def afterAll = {
    cleanUp
  }

  def cleanUp = {
    val rootPath = ReNameItSpec.determineRootPath

    val path01 = Paths.get(rootPath + "/music/artist01/record01/artist01 - trackone.wav")
    val path02 = Paths.get(rootPath + "/music/artist01/record01/artist01 - tracktwo.wav")
    val path03 = Paths.get(rootPath + "/music/artist01/record02/artist01 - trackthree.wav")
    val path04 = Paths.get(rootPath + "/music/artist02/record01/artist02 - trackone.wav")

    if (Files.exists(path01)) {
      Files.move(path01, Paths.get(path01.getParent.toString + "/01 trackone.wav"))
    }
    if (Files.exists(path02)) {
      Files.move(path02, Paths.get(path02.getParent.toString + "/02 tracktwo.wav"))
    }
    if (Files.exists(path03)) {
      Files.move(path03, Paths.get(path03.getParent.toString + "/01 trackthree.wav"))
    }
    if (Files.exists(path04)) {
      Files.move(path04, Paths.get(path04.getParent.toString + "/01 trackone.wav"))
    }
  }

  "ReNameIt " must {

    "rename files correctly " in {

      val future: Future[Unit] = Future {
        ReNameIt.rename(ReNameItSpec.determineRootPath.toString)
      }

      future.map(u => {
        val rootPath = ReNameItSpec.determineRootPath

        val path01 = Paths.get(rootPath + "/music/artist01/record01/artist01 - trackone.wav")
        val path02 = Paths.get(rootPath + "/music/artist01/record01/artist01 - tracktwo.wav")
        val path03 = Paths.get(rootPath + "/music/artist01/record02/artist01 - trackthree.wav")
        val path04 = Paths.get(rootPath + "/music/artist02/record01/artist02 - trackone.wav")

        assert(Files.exists(path01))
        assert(Files.exists(path02))
        assert(Files.exists(path03))
        assert(Files.exists(path04))
      })
    }

  }

}
