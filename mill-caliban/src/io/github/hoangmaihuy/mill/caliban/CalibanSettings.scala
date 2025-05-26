package io.github.hoangmaihuy.mill.caliban

import caliban.tools.Options
import upickle.default.ReadWriter

enum CalibanGenType derives ReadWriter {
  case Schema, Client
}

object CalibanGenType {

  given Conversion[CalibanGenType, caliban.tools.Codegen.GenType] = {
    case CalibanGenType.Schema => caliban.tools.Codegen.GenType.Schema
    case CalibanGenType.Client => caliban.tools.Codegen.GenType.Client
  }

}

final case class CalibanCommonSettings(
  clientName: Option[String],
  scalafmtPath: Option[String],
  headers: Seq[(String, String)],
  packageName: Option[String],
  genView: Option[Boolean],
  scalarMappings: Seq[(String, String)],
  imports: Seq[String],
  splitFiles: Option[Boolean],
  enableFmt: Option[Boolean],
  extensibleEnums: Option[Boolean],
  genType: CalibanGenType,
  effect: Option[String],
  abstractEffectType: Option[Boolean],
  preserveInputNames: Option[Boolean],
  supportIsRepeatable: Option[Boolean],
  addDerives: Option[Boolean],
  envForDerives: Option[String],
  excludeDeprecated: Option[Boolean],
  supportDeprecatedArgs: Option[Boolean]
) derives ReadWriter {

  def withClientName(clientName: String): CalibanCommonSettings = this.copy(clientName = Some(clientName))
  def withScalafmtPath(scalafmtPath: String): CalibanCommonSettings = this.copy(scalafmtPath = Some(scalafmtPath))
  def withHeaders(headers: (String, String)*): CalibanCommonSettings = this.copy(headers = headers)
  def withPackageName(packageName: String): CalibanCommonSettings = this.copy(packageName = Some(packageName))
  def withGenView(genView: Boolean): CalibanCommonSettings = this.copy(genView = Some(genView))

  def withScalarMappings(scalarMappings: (String, String)*): CalibanCommonSettings =
    this.copy(scalarMappings = scalarMappings)

  def withImports(imports: String*): CalibanCommonSettings = this.copy(imports = imports)
  def withSplitFiles(splitFiles: Boolean): CalibanCommonSettings = this.copy(splitFiles = Some(splitFiles))
  def withEnableFmt(enableFmt: Boolean): CalibanCommonSettings = this.copy(enableFmt = Some(enableFmt))

  def withExtensibleEnums(extensibleEnums: Boolean): CalibanCommonSettings =
    this.copy(extensibleEnums = Some(extensibleEnums))

  def withGenType(genType: CalibanGenType): CalibanCommonSettings = this.copy(genType = genType)
  def withEffect(effect: String): CalibanCommonSettings = this.copy(effect = Some(effect))

  def withAbstractEffectType(abstractEffectType: Boolean): CalibanCommonSettings =
    this.copy(abstractEffectType = Some(abstractEffectType))

  def withPreserveInputNames(preserveInputNames: Boolean): CalibanCommonSettings =
    this.copy(preserveInputNames = Some(preserveInputNames))

  def withSupportIsRepeatable(supportIsRepeatable: Boolean): CalibanCommonSettings =
    this.copy(supportIsRepeatable = Some(supportIsRepeatable))

  def withAddDerives(addDerives: Boolean): CalibanCommonSettings = this.copy(addDerives = Some(addDerives))
  def withEnvForDerives(envForDerives: String): CalibanCommonSettings = this.copy(envForDerives = Some(envForDerives))

  def withExcludeDeprecated(excludeDeprecated: Boolean): CalibanCommonSettings =
    this.copy(excludeDeprecated = Some(excludeDeprecated))

  def withSupportDeprecatedArgs(supportDeprecatedArgs: Boolean): CalibanCommonSettings =
    this.copy(supportDeprecatedArgs = Some(supportDeprecatedArgs))

  def toOptions(schemaPath: String, toPath: String): Options =
    Options(
      schemaPath = schemaPath,
      toPath = toPath,
      fmtPath = scalafmtPath,
      headers = Option(headers.map((Options.Header.apply).tupled).toList).filter(_.nonEmpty),
      packageName = packageName,
      clientName = clientName,
      genView = genView,
      effect = effect,
      scalarMappings = Option(scalarMappings.toMap).filter(_.nonEmpty),
      imports = Option(imports.toList).filter(_.nonEmpty),
      abstractEffectType = abstractEffectType,
      splitFiles = splitFiles,
      enableFmt = enableFmt,
      extensibleEnums = extensibleEnums,
      preserveInputNames = preserveInputNames,
      supportIsRepeatable = supportIsRepeatable,
      addDerives = addDerives,
      envForDerives = envForDerives,
      excludeDeprecated = excludeDeprecated,
      supportDeprecatedArgs = supportDeprecatedArgs
    )

  def combine(r: => CalibanCommonSettings): CalibanCommonSettings =
    CalibanCommonSettings(
      clientName = r.clientName.orElse(this.clientName),
      scalafmtPath = r.scalafmtPath.orElse(this.scalafmtPath),
      headers = this.headers ++ r.headers,
      packageName = r.packageName.orElse(this.packageName),
      genView = r.genView.orElse(this.genView),
      scalarMappings = this.scalarMappings ++ r.scalarMappings,
      imports = this.imports ++ r.imports,
      splitFiles = r.splitFiles.orElse(this.splitFiles),
      enableFmt = r.enableFmt.orElse(this.enableFmt),
      extensibleEnums = r.extensibleEnums.orElse(this.extensibleEnums),
      genType = r.genType,
      effect = r.effect.orElse(this.effect),
      abstractEffectType = r.abstractEffectType.orElse(this.abstractEffectType),
      preserveInputNames = r.preserveInputNames.orElse(this.preserveInputNames),
      supportIsRepeatable = r.supportIsRepeatable.orElse(this.supportIsRepeatable),
      addDerives = r.addDerives.orElse(this.addDerives),
      envForDerives = r.envForDerives.orElse(this.envForDerives),
      excludeDeprecated = r.excludeDeprecated.orElse(this.excludeDeprecated),
      supportDeprecatedArgs = r.supportDeprecatedArgs.orElse(this.supportDeprecatedArgs)
    )

}

object CalibanCommonSettings {

  val empty = CalibanCommonSettings(
    clientName = None,
    scalafmtPath = None,
    headers = Seq.empty,
    packageName = None,
    genView = None,
    scalarMappings = Seq.empty,
    imports = Seq.empty,
    splitFiles = None,
    enableFmt = None,
    extensibleEnums = None,
    genType = CalibanGenType.Client,
    effect = None,
    abstractEffectType = None,
    preserveInputNames = None,
    supportIsRepeatable = None,
    addDerives = None,
    envForDerives = None,
    excludeDeprecated = None,
    supportDeprecatedArgs = None
  )

}

trait CalibanCommonRw {

  given relPathRw: upickle.default.ReadWriter[os.RelPath] =
    upickle.default.readwriter[String].bimap(_.toString, os.RelPath(_))

  given subPathRw: upickle.default.ReadWriter[os.SubPath] =
    upickle.default.readwriter[String].bimap(_.toString, os.SubPath(_))

}

final case class CalibanFileSettings(file: os.RelPath, settings: CalibanCommonSettings) derives ReadWriter {

  def withSettings(f: CalibanCommonSettings => CalibanCommonSettings): CalibanFileSettings =
    this.copy(settings = f(settings))

}

final case class CalibanUrlSettings(url: String, settings: CalibanCommonSettings) derives ReadWriter {

  def withSettings(f: CalibanCommonSettings => CalibanCommonSettings): CalibanUrlSettings =
    this.copy(settings = f(settings))

}

object CalibanFileSettings extends CalibanCommonRw {

  def forFile(relPath: os.RelPath)(settings: CalibanCommonSettings => CalibanCommonSettings) = {
    CalibanFileSettings(file = relPath, settings = settings(CalibanCommonSettings.empty))
  }

}

object CalibanUrlSettings extends CalibanCommonRw {

  def forUrl(url: String)(settings: CalibanCommonSettings => CalibanCommonSettings) = {
    CalibanUrlSettings(url = url, settings = settings(CalibanCommonSettings.empty))
  }

}
