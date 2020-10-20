using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json.Linq;

namespace JsonParser.Parser
{
    public class AttributeFirstJsonNodeTransformer
    {
        public JToken Transform(JToken inputToken)
        {
            return inputToken switch
            {
                JObject obj => CreateJObject(ReorderTokens(obj)),
                _ => inputToken
            };
        }

        private JObject CreateJObject(IEnumerable<(string, JToken)> contents)
        {
            var jObject = new JObject();
            foreach (var (key, value) in contents)
            {
                jObject.Add(key, value);
            }
            
            return jObject;
        }

        private IEnumerable<(string, JToken)> ReorderTokens(JObject jObject)
        {
            var tail = new List<(string, JToken)>();
            foreach (var (key, value) in jObject)
            {
                if (value is JContainer)
                {
                    tail.Add((key, value));
                }
                else
                {
                    yield return (key, Transform(value));
                }
            }
            
            foreach (var (key, value) in tail)
            {
                yield return (key, Transform(value));
            }
        }
    }
}