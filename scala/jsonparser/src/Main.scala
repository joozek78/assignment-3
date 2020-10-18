import java.io.ByteArrayInputStream
import java.lang.System.lineSeparator
import java.nio.charset.StandardCharsets.UTF_8

import parser.AttributeFirstJsonTransformer

import scala.io.{Source, StdIn}

object Main extends App {
  println("Paste your json content below and end with an empty line:")

  // Modify the code below to be stream safe
  val input = Source.fromInputStream(
    new ByteArrayInputStream(readLines().getBytes(UTF_8))
  )
  val output = AttributeFirstJsonTransformer.transform(input)
  println("Output:")
  output.getLines.foreach(println)

  private def readLines() =
    Iterator
      .continually(StdIn.readLine())
      .takeWhile(!_.isEmpty)
      .mkString(lineSeparator)
}
