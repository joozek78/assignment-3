scalaVersion := "2.13.1"

name := "jsonparser"
version := "1.0"

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test"

// Add your library here
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "3.3.3" % Test
