package io.github.hoangmaihuy.mill.caliban

import mill.testkit.IntegrationTester
import utest.*

import scala.runtime.stdLibPatches.Predef.assert

object IntegrationTests extends TestSuite {

  def tests: Tests = Tests {
    println("initializing mill-caliban.IntegrationTest.tests")

    test("integration") {
      val resourceFolder = os.Path(sys.env("MILL_TEST_RESOURCE_DIR"))
      val tester = new IntegrationTester(
        daemonMode = true,
        workspaceSourcePath = resourceFolder / "integration",
        millExecutable = os.Path(sys.env("MILL_EXECUTABLE_PATH"))
      )

      val res = tester.eval("codegen.compile")
      println(res.err)
      assert(res.isSuccess)
      assert(res.err.contains("Generating caliban source"))
    }
  }

}
