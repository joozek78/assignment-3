using System.IO;
using System.Text;
using JsonParser.Parser;
using NUnit.Framework;

namespace Tests
{
    public class AttributeFirstJsonTransformerTests
    {
        [TestCase("{}", "{}", TestName = "When input is empty JSON, output is empty JSON")]
        [TestCase("not-json", "not-json", TestName = "When input is not valid JSON, it is returned verbatim")]
        [TestCase(@"{""age"":1, ""address"":{""street"":""crooked"", ""houseNo"": 5, ""absolute"":false}, ""name"": ""John""}",
            @"{""age"":1,  ""name"": ""John"", ""address"":{""street"":""crooked"", ""houseNo"": 5, ""absolute"":false}}",
            TestName = "When input is JSON object, its primitive properties are moved to the top")]
        public void TransformationCases(string input, string expectedOutput)
        {
            // Arrange
            using var inputStream = new MemoryStream(Encoding.UTF8.GetBytes(input));

            // Act
            using var outputStream = AttributeFirstJsonStreamTransformer.Transform(inputStream);

            // Assert
            var output = new StreamReader(outputStream).ReadToEnd();
            Assert.AreEqual(StripWhitespace(expectedOutput),
                StripWhitespace(output));
        }

        private static string StripWhitespace(string str) => str
            .Replace(" ", "")
            .Replace("\n", "")
            .Replace("\r", "");
    }
}