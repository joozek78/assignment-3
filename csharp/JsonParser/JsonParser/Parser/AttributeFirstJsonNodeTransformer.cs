using System.Collections.Generic;
using Newtonsoft.Json.Linq;

namespace JsonParser.Parser
{
    public class AttributeFirstJsonNodeTransformer
    {
        public JToken Transform(JToken inputToken)
        {
            return inputToken switch
            {
                JObject obj => ReorderTokens(obj),
                _ => inputToken
            };
        }

        private JToken ReorderTokens(JObject jObject)
        {
            var output = new JObject();
            var tail = new List<(string, JToken)>();
            foreach (var (key, value) in jObject)
            {
                if (value is JContainer)
                {
                    tail.Add((key, value));
                }
                else
                {
                    AddWithReorder(output, key, value);
                }
            }

            foreach (var (key, value) in tail)
            {
                AddWithReorder(output, key, value);
            }

            return output;
        }

        private void AddWithReorder(JObject target, string key, JToken value)
        {
            var valueToAdd = value is JObject obj ? ReorderTokens(obj) : value;
            target.Add(key, valueToAdd);
        }
    }
}