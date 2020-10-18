package parser

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class AttributeFirstJsonTransformerTests extends AnyFunSuite with Matchers {
  test("When input is empty object, then it should return empty object") {
    // Arrange
    val input =
      Source.fromInputStream(new ByteArrayInputStream("{}".getBytes(UTF_8)))

    // Act
    val output = AttributeFirstJsonTransformer.transform(input)

    // Assert
    output.mkString shouldEqual "{}"
  }
}
