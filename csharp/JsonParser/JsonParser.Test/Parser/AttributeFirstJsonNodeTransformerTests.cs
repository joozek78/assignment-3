using System;
using System.Collections.Generic;
using FluentAssertions;
using JsonParser.Parser;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using NUnit.Framework;

namespace Tests
{
    public class AttributeFirstJsonNodeTransformerTests
    {
        private readonly AttributeFirstJsonNodeTransformer _subject = new AttributeFirstJsonNodeTransformer();
        
        [Test]
        public void WhenInputContainsAnObject_ItShouldBeMovedToTheEnd()
        {
            var input = ToJson(new Dictionary<string, object>()
            {
                {
                    "address", new Dictionary<string, object>
                    {
                        {"StreetName", "Gedempte Zalmhaven"}
                    }
                },
                {"name", "john"}
            });

            var output = _subject.Transform(input);

            output.Should().BeEquivalentTo(input);
            AssertTermsAreInOrder(output, "name", "address");
        }

        [Test]
        public void WhenInputContainsAnArray_ItShouldBeMovedToTheEnd()
        {
            var input = ToJson(new Dictionary<string, object>()
            {
                {
                    "friends", new List<string>()
                    {
                        "mary", "sue"
                    }
                },
                {"name", "john"}
            });

            var output = _subject.Transform(input);

            output.Should().BeEquivalentTo(input);
            AssertTermsAreInOrder(output, "name", "friends");
        }

        [Test]
        public void WhenInputContainsAnObject_ItsContentsShouldBeReordered()
        {
            var input = ToJson(new Dictionary<string, object>()
            {
                {
                    "bestFriend", new Dictionary<string, object>
                    {
                        { "traits", new List<string>() {"kind"}},
                        { "nick", "Anders" },
                    }
                },
                {"name", "john"}
            });

            var output = _subject.Transform(input);

            output.Should().BeEquivalentTo(input);
            AssertTermsAreInOrder(output, "nick", "traits");
        }

        [Test]
        public void WhenInputIsAnArray_ItsContentsShouldntBeChanged()
        {
            var input = ToJson(new List<object>()
            {
                new List<string>() {"earth", "fire", "water", "air"},
                "John"
            });

            var output = _subject.Transform(input);

            output.Should().BeEquivalentTo(input);
            AssertTermsAreInOrder(output, "earth", "John");
        }

        private JToken ToJson(object input)
        {
            using var jTokenWriter = new JTokenWriter();
            var jsonSerializer = JsonSerializer.CreateDefault();
            jsonSerializer.Serialize(jTokenWriter, input);
            return jTokenWriter.Token;
        }
        
        private void AssertTermsAreInOrder(JToken json, string earlier, string later)
        {
            var str = json.ToString();
            str.Should().Contain(earlier).And.Contain(later);
            str.IndexOf(earlier, StringComparison.Ordinal).Should().BeLessThan(str.IndexOf(later, StringComparison.Ordinal));
        }
    }
}