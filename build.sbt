lazy val `re-name-ti` = project
  .in(file("."))
  .enablePlugins(GitVersioning)
  .aggregate(`re-name-it`)

lazy val `re-name-it` = project
  .in(file("re-name-it"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)

name := "re-name-it"

unmanagedSourceDirectories.in(Compile) := Vector.empty
unmanagedSourceDirectories.in(Test)    := Vector.empty

publishArtifact := false

scalaVersion := "2.12.1"
