package caliban.codegen

import java.io.File

import caliban.tools.{CalibanCommonSettings, Codegen}
import zio.{IO, ZIO}

import io.github.hoangmaihuy.mill.caliban.CalibanFileSettings

object CalibanSourceGenerator {

  private def transformFile(
    sourcePath: os.Path,
    destPath: os.Path,
    settings: CalibanCommonSettings
  ): os.Path => os.Path = { graphqlFile =>
    val relativePath = settings.packageName.fold(sourcePath.subRelativeTo(graphqlFile)) { pkg =>
      os.SubPath(pkg.split('.').toIndexedSeq)
    }
    destPath / relativePath / (graphqlFile.baseName.stripSuffix(".graphql") + ".scala")
  }

  def collectSettingsFor(fileSettings: Seq[CalibanFileSettings], source: os.RelPath): Seq[CalibanFileSettings] = {
    // Supply a default packageName.
    val defaults: CalibanCommonSettings = CalibanCommonSettings.empty.copy(packageName = Some("caliban"))

    val matchingSettingSets = fileSettings
      .collect {
        case needle if source == needle.file => needle.settings
      }
      .map(defaults.combine(_))

    val finalSettingSets = if (matchingSettingSets.isEmpty) List(defaults) else matchingSettingSets

    finalSettingSets.map(settings => CalibanFileSettings(file = source, settings = settings))
  }

  def generateFileSource(
    sourcePath: os.Path,
    destPath: os.Path,
    graphql: os.Path,
    settings: CalibanCommonSettings
  ): IO[Option[Throwable], List[File]] =
    for {
      generatedSource <- ZIO.succeed(
        transformFile(
          sourcePath,
          destPath,
          settings
        )(graphql)
      )
      _ <- ZIO.attemptBlockingIO(os.makeDir.all(generatedSource / os.up)).asSomeError
      opts <- ZIO.fromOption(Some(settings.toOptions(graphql.toString, generatedSource.toString)))
      files <- Codegen.generate(opts, settings.genType).asSomeError
    } yield files

}
