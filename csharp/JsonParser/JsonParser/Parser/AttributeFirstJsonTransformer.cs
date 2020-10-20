using System.IO;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace JsonParser.Parser
{
    public static class AttributeFirstJsonTransformer
    {
        public static Stream Transform(Stream inputStream)
        { 
            var input = new StreamReader(inputStream).ReadToEnd();
            try
            {
                var transformedToken = new AttributeFirstJsonNodeTransformer().Transform(JToken.Parse(input));
                return WrapInStream(transformedToken.ToString(Formatting.Indented));
            }
            catch (JsonReaderException)
            {
                return WrapInStream(input);
            }
        }
        
        private static Stream WrapInStream(string str) => new MemoryStream(Encoding.UTF8.GetBytes(str));
    }
}