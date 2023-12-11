# mill-caliban
[Caliban](https://github.com/ghostdogpr/caliban) codegen plugin for Mill, ported from Caliban's [codegen-sbt](https://github.com/ghostdogpr/caliban/tree/series/2.x/codegen-sbt/src).

## Installation

```scala
import $ivy.`io.github.hoangmaihuy::mill-caliban::<latest-version>`

import mill._
import mill.scalalib._
import io.github.hoangmaihuy.mill.caliban._
import caliban.tools.Codegen

object codegen extends ScalaModule with CalibanSourceGenModule {

  override def scalaVersion = "3.3.1"

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.github.ghostdogpr::caliban-client:${CalibanBuildInfo.calibanVersion}"
  )

  // Optional configurations, below examples are taken from itest build.sc
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
```

## Licenses

This software is released under the Apache License 2.0. More information in the file LICENSE distributed with this project.
