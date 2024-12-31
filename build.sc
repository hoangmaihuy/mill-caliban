import $ivy.`com.lihaoyi::mill-contrib-buildinfo:`
import $ivy.`de.tototec::de.tobiasroeser.mill.integrationtest::0.7.1`
import $ivy.`io.chris-kipp::mill-ci-release::0.1.10`

import mill._
import mill.scalalib._
import mill.scalalib.publish._
import mill.contrib.buildinfo._
import mill.scalalib.api.ZincWorkerUtil.scalaNativeBinaryVersion

import de.tobiasroeser.mill.integrationtest._
import io.kipp.mill.ci.release.CiReleaseModule
import io.kipp.mill.ci.release.SonatypeHost

def millVersionFile = T.source(PathRef(os.pwd / ".mill-version"))

def millVersion = T {
  os.read(millVersionFile().path).trim
}

object Versions {
  lazy val scala = "2.13.15"
  lazy val caliban = "2.9.1"
}

object `mill-caliban` extends ScalaModule with CiReleaseModule with BuildInfo {

  override def scalaVersion = Versions.scala

  override def sonatypeHost = Some(SonatypeHost.s01)

  override def versionScheme: T[Option[VersionScheme]] = T(Option(VersionScheme.EarlySemVer))

  override def pomSettings = PomSettings(
    description = "Caliban codegen plugin for Mill",
    organization = "io.github.hoangmaihuy",
    url = "https://github.com/hoangmaihuy/mill-caliban",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github(owner = "hoangmaihuy", repo = "mill-caliban"),
    developers = Seq(Developer("hoangmaihuy", "Hoang Mai", "https://github.com/hoangmaihuy"))
  )

  override def artifactName = "mill-caliban"

  override def artifactSuffix =
    "_mill" + scalaNativeBinaryVersion(millVersion()) +
      super.artifactSuffix()

  override def scalacOptions = Seq("-Ywarn-unused", "-deprecation")

  override def compileIvyDeps = super.compileIvyDeps() ++ Agg(
    ivy"com.lihaoyi::mill-scalalib:${millVersion()}"
  )

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.github.ghostdogpr::caliban-tools:${Versions.caliban}"
  )

  override def buildInfoMembers = Seq(
    BuildInfo.Value("calibanVersion", Versions.caliban)
  )

  override def buildInfoObjectName = "CalibanBuildInfo"
  override def buildInfoPackageName = "io.github.hoangmaihuy.mill.caliban"

}

object itest extends MillIntegrationTestModule {

  override def millTestVersion = millVersion

  override def pluginsUnderTest = Seq(`mill-caliban`)

  def testBase = millSourcePath / "src"

  override def testInvocations = Seq(
    PathRef(testBase / "codegen") -> Seq(
      TestInvocation.Targets(Seq("compile"))
    )
  )

}
