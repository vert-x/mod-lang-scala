import sbt._
import sbt.Keys._

object VertxScalaBuild extends Build {

  val baseSettings = Defaults.defaultSettings ++ Seq(
    organization        := "io.vertx",
    name                := "lang-scala",
    version             := "1.1.0-SNAPSHOT",
    scalaVersion        := "2.10.4",
    crossScalaVersions  := Seq("2.10.4", "2.11.2"),
    description         := "Vert.x module that provides Scala support"
  )

  lazy val project = Project (
    "project",
    file ("."),
    settings = baseSettings ++ Seq(
      copyModTask,
      zipModTask,
      libraryDependencies ++= Dependencies.compile,
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ ),
      // Fork JVM to allow Scala in-flight compilation tests to load the Scala interpreter
      fork in Test := true,
      // Vert.x tests are not designed to run in paralell
      parallelExecution in Test := false,
      // Adjust test system properties so that scripts are found
      javaOptions in Test += "-Dvertx.test.resources=src/test/scripts",
      // Adjust test modules directory
      javaOptions in Test += "-Dvertx.mods=target/mods",
      copyMod <<= copyMod dependsOn (compile in Compile),
      (test in Test) <<= (test in Test) dependsOn copyMod,
      (packageBin in Compile) <<= (packageBin in Compile) dependsOn copyMod,
      // Publishing settings
      publishMavenStyle := true,
      pomIncludeRepository := { _ => false },
      publishTo <<= version { (v: String) =>
        val sonatype = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("Sonatype Snapshots" at sonatype + "content/repositories/snapshots")
        else
          Some("Sonatype Releases"  at sonatype + "service/local/staging/deploy/maven2")
      },
      pomExtra :=
        <inceptionYear>2013</inceptionYear>
        <url>http://vertx.io</url>
        <licenses>
          <license>
            <name>Apache License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <connection>scm:git:git://github.com/vert-x/mod-lang-scala.git</connection>
          <developerConnection>scm:git:ssh://git@github.com/vert-x/mod-lang-scala.git</developerConnection>
          <url>http://github.com/vert-x/mod-lang-scala</url>
        </scm>
        <developers>
          <developer>
            <id>galderz</id>
            <name>Galder Zamarreño</name>
          </developer>
          <developer>
            <id>swilliams-vmw</id>
            <name>Stuart Williams</name>
          </developer>
          <developer>
            <id>edgarchan</id>
            <name>Edgar Chan</name>
          </developer>
          <developer>
            <id>nfmelendez</id>
            <name>Nicolas Melendez</name>
          </developer>
          <developer>
            <id>Narigo</id>
            <name>Joern Bernhardt</name>
          </developer>
          <developer>
            <id>raniejade</id>
            <name>Ranie Jade Ramiso</name>
          </developer>
        </developers>
    )
  ).settings( addArtifact(Artifact("lang-scala", "zip", "zip", "mod"), zipMod).settings : _* )

  val copyMod = TaskKey[Unit]("copy-mod", "Assemble the module into the local mods directory")
  val zipMod = TaskKey[File]("zip-mod", "Package the module .zip file")

  val copyModTask = copyMod := {
    implicit val log = streams.value.log
    val modOwner = organization.value
    val modName = name.value
    val modVersion = version.value
    val scalaMajor = scalaVersion.value.substring(0, scalaVersion.value.lastIndexOf('.'))
    val moduleName = s"$modOwner~${modName}_$scalaMajor~$modVersion"
    log.info("Create module " + moduleName)
    val moduleDir = target.value / s"mods/$moduleName"
    createDirectory(moduleDir)
    copyDirectory((classDirectory in Compile).value, moduleDir)
    copyDirectory((resourceDirectory in Compile).value, moduleDir)
    val libDir = moduleDir / "lib"
    createDirectory(libDir)
    // Get the runtime classpath to get all dependencies except provided ones
    (managedClasspath in Runtime).value foreach { classpathEntry =>
      copyClasspathFile(classpathEntry, libDir)
    }
  }

  val zipModTask = zipMod := {
    implicit val log = streams.value.log
    val modOwner = organization.value
    val modName = name.value
    val modVersion = version.value
    val scalaMajor = scalaVersion.value.substring(0, scalaVersion.value.lastIndexOf('.'))
    val moduleName = s"$modOwner~${modName}_$scalaMajor~$modVersion"
    log.info("Create ZIP module " + moduleName)
    val moduleDir = target.value / s"mods/$moduleName"
    val zipFile = target.value / s"zips/$moduleName.zip"
    IO.zip(allSubpaths(moduleDir), zipFile)
    zipFile
  }

  private def createDirectory(dir: File)(implicit log: Logger): Unit = {
    log.debug(s"Create directory $dir")
    IO.createDirectory(dir)
  }

  private def copyDirectory(source: File, target: File)(implicit log: Logger): Unit = {
    log.debug(s"Copy $source to $target")
    IO.copyDirectory(source, target, overwrite = true)
  }

  private def copyClasspathFile(cpEntry: Attributed[File], libDir: File)(implicit log: Logger): Unit = {
    val sourceFile = cpEntry.data
    val targetFile = libDir / sourceFile.getName
    log.debug(s"Copy $sourceFile to $targetFile")
    IO.copyFile(sourceFile, targetFile)
  }

}

object Dependencies {
  object Versions {
    val vertxVersion            = "2.1.2"
    val testtoolsVersion        = "2.0.3-final"
    val hamcrestVersion         = "1.3"
    val junitInterfaceVersion   = "0.10"
  }

  object Compile {
    import Versions._
    val vertxCore      = "io.vertx" % "vertx-core"     % vertxVersion % "provided"
    val vertxPlattform = "io.vertx" % "vertx-platform" % vertxVersion % "provided"
    val vertxTesttools = "io.vertx" % "testtools"      % testtoolsVersion % "provided"
  }

  object Test {
    import Versions._
    val hamcrest       = "org.hamcrest" % "hamcrest-library" % hamcrestVersion       % "test"
    val junitInterface = "com.novocode" % "junit-interface"  % junitInterfaceVersion % "test"
  }

  import Compile._

  val test = List(Test.hamcrest, Test.junitInterface)

  val compile = List(vertxCore, vertxPlattform, vertxTesttools) ::: test

}
