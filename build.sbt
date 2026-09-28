enablePlugins(JavaAppPackaging)

name := "main"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-collections4" % "4.4",
  "commons-cli"        % "commons-cli"          % "1.9.0",
  "org.scalatest"     %% "scalatest"           % "3.2.19" % Test
)

Compile / mainClass := Some("edu.luc.cs.consoleapp.Main")

executableScriptName := "main"

Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value)
Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value)
