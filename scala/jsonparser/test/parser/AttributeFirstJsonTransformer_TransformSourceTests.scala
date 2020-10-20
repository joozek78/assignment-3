package parser

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8

import org.scalactic.{AbstractStringUniformity, Uniformity}
import org.scalactic.StringNormalizations.lowerCased
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.io.Source
import scala.util.Using

class AttributeFirstJsonTransformer_TransformSourceTests extends AnyFunSuite with Matchers {
  test("When input is empty object, then it should return empty object") {
    assertTransformation("{}", "{}")
  }
  test("When input is not valid JSON, then it is returned verbatim") {
    assertTransformation("not-json", "not-json")
  }
  
  test("When input is JSON, properties are reordered to put primitives first") {
    assertTransformation("""{"name":"john", "address":{"nested":{"key":"val"}, "street":"some"}, "age":1}""",
      """{"name":"john","age":1, "address":{"street":"some","nested":{"key":"val"}}}""")
  }
  
  private def assertTransformation(input: String, expectedOutput: String) = {
    // Arrange
    val inputSource =
      Source.fromInputStream(new ByteArrayInputStream(input.getBytes(UTF_8)))

    // Act
    val output = for(
      os <- Using(inputSource) { is => AttributeFirstJsonTransformer.transform(is) };
      output <- Using(os) { os => os.mkString })
      yield output
    
    // Assert
    (output.get shouldEqual expectedOutput) (after being lowerCased and strippedOfWhitespace)
  }

  private val strippedOfWhitespace: Uniformity[String] =
    new AbstractStringUniformity {

      def normalized(s: String): String = s.replace(" ", "").replace("\n", "")

      override def toString: String = "strippedOfWhitespace"
    }
}
