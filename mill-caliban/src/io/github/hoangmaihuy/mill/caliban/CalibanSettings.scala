package io.github.hoangmaihuy.mill.caliban

import mill._
import caliban.tools.CalibanCommonSettings
import caliban.tools.Codegen.GenType

sealed trait CalibanSettings {
  type Self <: CalibanSettings
  def withSettings(f: CalibanCommonSettings => CalibanCommonSettings): Self

  final def clientName(value: String): Self = withSettings(_.clientName(value))
  final def scalafmtPath(path: String): Self = withSettings(_.scalafmtPath(path))
  final def packageName(name: String): Self = withSettings(_.packageName(name))
  final def genView(value: Boolean): Self = withSettings(_.genView(value))

  final def scalarMapping(mapping: (String, String)*): Self =
    withSettings(_.scalarMappings(mapping: _*))

  final def imports(values: String*): Self = withSettings(_.imports(values: _*))
  final def splitFiles(value: Boolean): Self = withSettings(_.splitFiles(value))
  final def enableFmt(value: Boolean): Self = withSettings(_.enableFmt(value))
  final def extensibleEnums(value: Boolean): Self = withSettings(_.extensibleEnums(value))
  final def genType(genType: GenType): Self = withSettings(_.genType(genType))
  final def effect(effect: String): Self = withSettings(_.effect(effect))

  final def abstractEffectType(abstractEffectType: Boolean): Self =
    withSettings(_.abstractEffectType(abstractEffectType))

  final def supportIsRepeatable(value: Boolean): Self = withSettings(_.supportIsRepeatable(value))
  final def preserveInputNames(value: Boolean): Self = withSettings(_.preserveInputNames(value))
  final def addDerives(value: Boolean): Self = withSettings(_.addDerives(value))
}

trait CalibanCommonRw {

  implicit val relPathRw: upickle.default.ReadWriter[os.RelPath] =
    upickle.default.readwriter[String].bimap(_.toString, os.RelPath(_))

  implicit val subPathRw: upickle.default.ReadWriter[os.SubPath] =
    upickle.default.readwriter[String].bimap(_.toString, os.SubPath(_))

  implicit val schemaGenTypeRw: upickle.default.ReadWriter[GenType.Schema.type] = upickle.default.macroRW
  implicit val clientGenTypeRw: upickle.default.ReadWriter[GenType.Client.type] = upickle.default.macroRW
  implicit val genTypeRw: upickle.default.ReadWriter[GenType] = upickle.default.macroRW
  implicit val commonSettingsRw: upickle.default.ReadWriter[CalibanCommonSettings] = upickle.default.macroRW
}

final case class CalibanFileSettings(file: os.RelPath, settings: CalibanCommonSettings) extends CalibanSettings {
  type Self = CalibanFileSettings

  def withSettings(f: CalibanCommonSettings => CalibanCommonSettings): Self =
    this.copy(settings = f(settings))

}

final case class CalibanUrlSettings(url: String, settings: CalibanCommonSettings) extends CalibanSettings {
  type Self = CalibanUrlSettings

  def withSettings(f: CalibanCommonSettings => CalibanCommonSettings): Self =
    this.copy(settings = f(settings))

  def headers(values: (String, String)*): CalibanUrlSettings = this.copy(settings = this.settings.headers(values: _*))
}

object CalibanFileSettings extends CalibanCommonRw {
  implicit val rw: upickle.default.ReadWriter[CalibanFileSettings] = upickle.default.macroRW

  def forFile(relPath: os.RelPath)(setting: CalibanFileSettings => CalibanFileSettings) = {
    setting.apply(CalibanFileSettings(file = relPath, settings = CalibanCommonSettings.empty))
  }

}

object CalibanUrlSettings extends CalibanCommonRw {
  implicit val rw: upickle.default.ReadWriter[CalibanUrlSettings] = upickle.default.macroRW
}
