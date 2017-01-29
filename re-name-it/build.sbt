libraryDependencies ++= Vector(
  Library.scalaTest % Test,
  Library.scalaTime,
// logging
  Library.logbackClassic
)

//initialCommands := """|import net.sourcekick.renameit_|""".stripMargin

parallelExecution in Test := false

daemonUser.in(Docker) := "root"
maintainer.in(Docker) := "sourcekick"
version.in(Docker) := "0.0.1"
dockerBaseImage := "java:8-jre-alpine"
dockerExposedPorts := Vector(2552, 8000)
dockerRepository := Some("sourcekick")

