package parser

import io.circe.Json
import org.scalatest
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AttributeFirstJsonTransformer_TransformJsonTests extends AnyFunSuite with Matchers {
  test("When input contains an object it should be moved to the end") {
    val input = Json.obj(
      "address" -> Json.obj(
        "street" -> Json.fromString("main")
      ),
      "name" -> Json.fromString("John"),
    )
    
    val output = AttributeFirstJsonTransformer.transform(input)
    
    output shouldEqual input
    assertTermsAreInOrder(output.noSpaces, "name", "address")
  }
  
  test("When input contains an array it should be moved to the end") {
    val input = Json.obj(
      "friends" -> Json.arr(Json.fromString("mary"), Json.fromString("sue")),
      "name" -> Json.fromString("John"),
    )
    
    val output = AttributeFirstJsonTransformer.transform(input)
    
    output shouldEqual input
    assertTermsAreInOrder(output.noSpaces, "name", "friends")
  }
  
  test("When input contains an object its contents should be reordered") {
    val input = Json.obj(
      "bestFriend" -> Json.obj(
        "nick" -> Json.fromString("Anders"),
        "traits" -> Json.arr(Json.fromString("kind")),
        "age" -> Json.fromInt(45),
      ),
      "name" -> Json.fromString("John"),
    )
    
    val output = AttributeFirstJsonTransformer.transform(input)
    
    output shouldEqual input
    assertTermsAreInOrder(output.noSpaces, "age", "traits")
  }
  
  test("When input is an array, its contents shouldn't be changed") {
    val input = Json.arr(Json.obj(
      "nick" -> Json.fromString("Anders"),
      "traits" -> Json.arr(Json.fromString("kind")),
      "age" -> Json.fromInt(45),
    ), Json.fromString("bob"))
    
    val output = AttributeFirstJsonTransformer.transform(input)
    
    output shouldEqual input
    assertTermsAreInOrder(output.noSpaces, "age", "bob")
  }
  
  private def assertTermsAreInOrder(str: String, earlier: String, later: String): scalatest.Assertion = {
    str should include (earlier)
    str should include (later)
    str.indexOf(earlier) should be < str.indexOf(later)
  } 
}
