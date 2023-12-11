import $file.plugins

import mill._
import mill.scalalib._
import io.github.hoangmaihuy.mill.caliban._
import caliban.tools.Codegen

object codegen extends RootModule with CalibanSourceGenModule {

  override def scalaVersion = "3.3.1"

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.github.ghostdogpr::caliban-client:${CalibanBuildInfo.calibanVersion}"
  )

  override def calibanFileSettings = Seq(
    // Explicitly constrain to disambiguate
    CalibanFileSettings.forFile(os.rel / "schema.graphql")(
      _.clientName("Client")
    ),
    // Another entry for the same file, which will cause another generator to run
    CalibanFileSettings.forFile(os.rel / "schema.graphql")(
      _.genType(Codegen.GenType.Schema)
        .scalarMapping("Json" -> "String")
        .effect("scala.util.Try")
        .addDerives(false)
    ),
    CalibanFileSettings.forFile(os.rel / "genview" / "schema.graphql")(
      _.clientName("Client").packageName("genview").genView(true)
    )
  )

}
