# mill-caliban
[Caliban](https://github.com/ghostdogpr/caliban) codegen plugin for Mill, ported from Caliban's [codegen-sbt](https://github.com/ghostdogpr/caliban/tree/series/2.x/codegen-sbt/src).

## Installation

```scala
//| mvnDeps:
//| - io.github.hoangmaihuy::mill-caliban::<latest-version>

import mill.*
import mill.scalalib.*
import io.github.hoangmaihuy.mill.caliban.*
import caliban.tools.Codegen

object codegen extends ScalaModule with CalibanSourceGenModule {

  override def scalaVersion = "3.7.0"

  override def mvnDeps = super.mvnDeps() ++ Seq(
    mvn"com.github.ghostdogpr::caliban-client:${CalibanBuildInfo.calibanVersion}"
  )

  // Optional configurations, below examples are taken from itest build.sc
  override def calibanFileSettings = Seq(
    // Explicitly constrain to disambiguate
    CalibanFileSettings.forFile(os.rel / "schema.graphql")(
      _.withClientName("Client")
    ),
    // Another entry for the same file, which will cause another generator to run
    CalibanFileSettings.forFile(os.rel / "schema.graphql")(
      _.withGenType(Codegen.GenType.Schema)
        .withScalarMappings("Json" -> "String")
        .withEffect("scala.util.Try")
        .withAddDerives(false)
    ),
    CalibanFileSettings.forFile(os.rel / "genview" / "schema.graphql")(
      _.withClientName("Client").withPackageName("genview").withGenView(true)
    )
  )

}
```

## Licenses

This software is released under the Apache License 2.0. More information in the file LICENSE distributed with this project.
