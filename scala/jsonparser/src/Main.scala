import java.io.ByteArrayInputStream
import java.lang.System.lineSeparator
import java.nio.charset.StandardCharsets.UTF_8

import parser.AttributeFirstJsonTransformer

import scala.io.{Source, StdIn}
import scala.util.Using

object Main extends App {
  println("Paste your json content below and end with an empty line:")

  // note to reviewer: this code assumes that transform function will be done with its input by the time it returns,
  // i.e. it doesn't support a case where... todo add comment to transform?
  
  Using(createInputStream) { input => AttributeFirstJsonTransformer.transform(input) }
    .flatMap(output => Using(output) { output =>
      println("Output:")
      output.getLines.foreach(println)
    })
    .get

  private def createInputStream = 
    Source.fromInputStream(new ByteArrayInputStream(readLines().getBytes(UTF_8))
  )

  private def readLines() =
    Iterator
      .continually(StdIn.readLine())
      .takeWhile(!_.isEmpty)
      .mkString(lineSeparator)
}
