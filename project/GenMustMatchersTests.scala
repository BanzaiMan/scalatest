/*
* Copyright 2001-2014 Artima, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import collection.mutable.ListBuffer
import io.Source
import java.io.{File, FileWriter, BufferedWriter}

trait GenMustMatchersTestsBase {

  def translateShouldToMustInTests(shouldLine: String): String = {
    val temp1 = shouldLine.replaceAll("<code>must</code>", "<code>I_WAS_must_ORIGINALLY</code>")
    val temp2 = temp1.replaceAll("<!-- PRESERVE -->should", " I_MUST_STAY_SHOULD")
    val temp3 = temp2.replaceAll(
      "<a href=\"MustMatchers.html\"><code>MustMatchers</code></a>",
      "<a href=\"I_WAS_Must_ORIGINALLYMatchers.html\"><code>I_WAS_Must_ORIGINALLYMatchers</code></a>"
    )
    val temp4 = temp3.replaceAll("should", "must")
    val temp5 = temp4.replaceAll("Should", "Must")
    val temp6 = temp5.replaceAll("trait Matchers", "trait MustMatchers")
    val temp7 = temp6.replaceAll("object Matchers extends Matchers", "object MustMatchers extends MustMatchers")
    val temp8 = temp7.replaceAll("I_WAS_must_ORIGINALLY", "should")
    val temp9 = temp8.replaceAll(" I_MUST_STAY_SHOULD", "should")
    val temp10 = temp9.replaceAll("import Matchers._", "import MustMatchers._")
    val temp11 = temp10.replaceAll("with Matchers", "with MustMatchers")
    val temp12 = temp11.replaceAll("Matchers.scala", "MustMatchers.scala")
    temp12.replaceAll("I_WAS_Must_ORIGINALLY", "Should")
  }

  def genTest(targetBaseDir: File, version: String, scalaVersion: String) {
    val sourceBaseDir = new File("src/test/scala/org/scalatest")
    val matchersDir = new File(targetBaseDir, "matchers")
    matchersDir.mkdirs()

    def transformFile(shouldFile: File, mustFile: File) {
      val writer = new BufferedWriter(new FileWriter(mustFile))
      try {
        val shouldLines = Source.fromFile(shouldFile).getLines().toList // for 2.8
        for (shouldLine <- shouldLines) {
          val mustLine = translateShouldToMustInTests(shouldLine)
          writer.write(mustLine.toString)
          writer.newLine() // add for 2.8
        }
      }
      finally {
        writer.close()
        println("Generated " + mustFile.getAbsolutePath)
      }
    }

    // For those under org.scalatest
    for (shouldFile <- sourceBaseDir.listFiles) {
      if (includeFile(shouldFile)) {
        val shouldFileName = shouldFile.getName

        val mustFileName = shouldFileName.replace("Should", "Must")

        val mustFile = new File(targetBaseDir, mustFileName)
        transformFile(new File(sourceBaseDir, shouldFileName), mustFile)
      }
    }

    // For those under org.scalatest.matchers
    val matchersSourceDir = new File(sourceBaseDir, "matchers")
    for (shouldFile <- matchersSourceDir.listFiles) {
      if (includeFile(shouldFile)) {
        val shouldFileName = shouldFile.getName

        val mustFileName = shouldFileName.replace("Should", "Must")

        val mustMatchersFile = new File(matchersDir, mustFileName)
        transformFile(new File(matchersSourceDir, shouldFileName), mustMatchersFile)
      }
    }
  }

  def includeFile(file: File): Boolean

}

object GenMustMatchersTests1 extends GenMustMatchersTestsBase {

  def includeFile(file: File): Boolean =
    file.isFile &&
    (file.getName.startsWith("Should") || file.getName.startsWith("ListShould") || file.getName.startsWith("EveryShould") || file.getName.startsWith("OptionShould")) &&
    (file.getName.hashCode.abs % 4 == 0)

}

object GenMustMatchersTests2 extends GenMustMatchersTestsBase {

  def includeFile(file: File): Boolean = {
    file.isFile &&
    (file.getName.startsWith("Should") || file.getName.startsWith("ListShould") || file.getName.startsWith("EveryShould") || file.getName.startsWith("OptionShould")) &&
    (file.getName.hashCode.abs % 4 == 1)
  }

}

object GenMustMatchersTests3 extends GenMustMatchersTestsBase {

  def includeFile(file: File): Boolean = {
    file.isFile &&
      (file.getName.startsWith("Should") || file.getName.startsWith("ListShould") || file.getName.startsWith("EveryShould") || file.getName.startsWith("OptionShould")) &&
      (file.getName.hashCode.abs % 4 == 2)
  }

}

object GenMustMatchersTests4 extends GenMustMatchersTestsBase {

  def includeFile(file: File): Boolean = {
    file.isFile &&
      (file.getName.startsWith("Should") || file.getName.startsWith("ListShould") || file.getName.startsWith("EveryShould") || file.getName.startsWith("OptionShould")) &&
      (file.getName.hashCode.abs % 4 == 3)
  }

}