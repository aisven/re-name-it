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

import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

import org.slf4j.{Logger, LoggerFactory}

private class ReNameItFileVisitor extends FileVisitor[Path] {

  private val log: Logger = LoggerFactory.getLogger(getClass.getName)

  override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = {
    log.warn(s"Could not visit ${file.getFileName}")
    FileVisitResult.CONTINUE
  }

  override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
    val name = file.getFileName.toString
    val parent = file.getParent.getFileName.toString
    val grandparent = file.getParent.getParent.getFileName.toString

    if (attrs.isRegularFile && name.endsWith(".wav")) {
      log.info(s"Visiting $grandparent/$parent/$name")

      val betterName =
        (grandparent + " - " + name).replaceFirst(" - [0-9]{2} ", " - ").replaceFirst(" - [0-9]-[0-9]{2} ", " - ")

      log.info(s"Renaming [$name] to [$betterName]")
      val dest = Paths.get(file.getParent.toAbsolutePath.toString + "/" + betterName)

      log.info(s"Moving to $dest")
      Files.move(file, dest)
    }

    FileVisitResult.CONTINUE
  }

  override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
    FileVisitResult.CONTINUE
  }

  override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
    FileVisitResult.CONTINUE
  }

}
