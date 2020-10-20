using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using JsonParser.Parser;

namespace JsonParser
{
    internal static class Program
    {
        static void Main()
        {
            Console.WriteLine("Paste your json content below and end with an empty line:");

            // Modify the code below to be stream safe
            using var output = TransformIntoStream(Encoding.UTF8.GetBytes(ReadLines()));
            Console.WriteLine("Output:");
            using var streamReader = new StreamReader(output);
            Console.WriteLine(streamReader.ReadToEnd());
        }

        private static Stream TransformIntoStream(byte[] lines)
        {
            using var memoryStream = new MemoryStream(lines);
            return AttributeFirstJsonStreamTransformer.Transform(memoryStream);
        }

        static string ReadLines()
        {
            IEnumerable<string> InfiniteReadLines()
            {
                while (true) yield return Console.ReadLine();
            }

            return string.Join(Environment.NewLine, InfiniteReadLines().TakeWhile(line => !string.IsNullOrEmpty(line)));
        }
    }
}