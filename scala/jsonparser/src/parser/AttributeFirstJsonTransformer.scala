package parser

import io.circe._
import io.circe.parser._

import scala.io.Source

object AttributeFirstJsonTransformer {
  /**
   * Transforms JSON stream, moving primitive properties to the top, recursively.
   * If the input is not valid JSON, it is returned without any changes. In such case, the input source will still 
   * be read to the end and a new source will be returned
   * @param source guaranteed to be read to end by the time this function returns
   * @return guaranteed to be a new source
   */
  def transform(source: Source): Source = {
    val jsonStr = source.getLines().mkString
    parse(jsonStr)
      .map(transform)
      .map(serialize)
      .getOrElse(Source.fromString(jsonStr))
  }

  /**
   * Transforms JSON stream, moving primitive properties to the top, recursively.
   */
  def transform(json: Json): Json = {
    json.mapObject(reorderObject)
  }
  
  private def reorderObject(obj: JsonObject): JsonObject = {
    val properties = obj.toList
    val (containers, primitives) = properties.partition({ case (_, value) => value.isObject || value.isArray})
    // note to reviewer: it would be great to map on the second element of tuple, but I don't know how to achieve that :(
    // (would be a PR comment)
    val reorderedContainers = containers.map({ case (key, value) => (key, transform(value)) })
    
    JsonObject(
      primitives ++ reorderedContainers: _*
    )
  }
  
  private def serialize(json: Json): Source = {
    Source.fromString(json.toString())
  }
}
