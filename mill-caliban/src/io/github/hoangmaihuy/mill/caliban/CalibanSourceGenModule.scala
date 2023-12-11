package io.github.hoangmaihuy.mill.caliban

import caliban.codegen.CalibanSourceGenerator
import mill._
import mill.scalalib._
import zio.{Unsafe, ZIO}

trait CalibanSourceGenModule extends ScalaModule {

  override def generatedSources = super.generatedSources() ++ calibanGenSources()
  def calibanSourcePath: T[PathRef] = T.source(millSourcePath / "graphql")
  def calibanFileSettings: T[Seq[CalibanFileSettings]] = T(Seq.empty[CalibanFileSettings])

  def calibanGenSources = T {
    val calibanPath = calibanSourcePath().path
    val fileSettings = calibanFileSettings()
    val calibanSources = Lib.findSourceFiles(Seq(calibanSourcePath()), Seq("graphql")).sorted
    Unsafe.unsafe { implicit u =>
      zio.Runtime.default.unsafe
        .run {
          ZIO
            .foreach(calibanSources) { source =>
              T.log.info(s"Generating caliban source for $source")
              ZIO
                .foreach(CalibanSourceGenerator.collectSettingsFor(fileSettings, source.relativeTo(calibanPath))) { s =>
                  CalibanSourceGenerator.generateFileSource(
                    calibanSourcePath().path,
                    T.dest,
                    source,
                    s.settings
                  )
                }
                .catchAll {
                  case Some(reason) =>
                    ZIO.succeed {
                      T.log.error(reason.toString)
                      T.log.error(reason.getStackTrace.mkString("\n"))
                      List.empty
                    }
                  case None => ZIO.succeed(List.empty)
                }
                .map(_.flatten)
            }
            .map(_.flatten)
        }
        .getOrThrowFiberFailure()
    }
    Seq(PathRef(T.dest))
  }

}
