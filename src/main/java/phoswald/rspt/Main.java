package phoswald.rspt;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, SyntaxException {
        if(args.length == 0) {
            System.out.println("RSPT - Really Simple Parser Tool");
            System.out.println("See: https://github.com/phoswald/rspt - (C) 2016 Philip Oswald");
            System.out.println("Usage: $ RSPT.exe -gen=java <grammar.txt> <output.java>");
            System.out.println("                  -gen=cs   <grammar.txt> <output.cs>");
            System.out.println("                  -gen=cpp  <grammar.txt> <output.h>");
            System.out.println("                  -par=txt  <grammar.txt> <input.txt> <output.txt>");
            System.out.println("Where:");
            System.out.println("    -gen=java generates a parser for the given grammar in Java");
            System.out.println("    -gen=cs   generates a parser for the given grammar in C#");
            System.out.println("    -gen=cpp  generates a parser for the given grammar in C++");
            System.out.println("    -par=txt  parses the input using the given grammar");
            System.out.println("Notes:");
            System.out.println("    All parsers are top down (recursive descent) parsers that");
            System.out.println("    can parse non-left recursive LL(x) grammars. Grammars contain rules,");
            System.out.println("    actions to transform machting input into output and instructions to");
            System.out.println("    customize the code generator or interpreter.");
            return;
        }

        int i = 0;
        while(i < args.length) {
            if(args[i].startsWith("-gen=")) {
                if(i+2 >= args.length) {
                    throw new IllegalArgumentException("Not enough arguments for -gen=...");
                }
                generate(Paths.get(args[i+1]), Paths.get(args[i+2]), args[i].substring(5));
                i += 3;
            } else if(args[i] == "-par=txt") {
                if(i+3 >= args.length) {
                    throw new IllegalArgumentException("Not enough arguments for -par=...");
                }
                parse(Paths.get(args[i+1]), Paths.get(args[i+2]), Paths.get(args[i+3]));
                i += 4;
            } else {
                throw new IllegalArgumentException("Invalid argument '" + args[i] + "'.");
            }
        }
    }

    private static void generate(Path grammarFile, Path parserFile, String type) throws IOException, SyntaxException {
        System.out.println("Generating parser '" + parserFile + "' from grammar '" + grammarFile + "'.");
        try(Reader grammarStream = Files.newBufferedReader(grammarFile);
                Writer parserStream = Files.newBufferedWriter(parserFile)) {
            GrammarType grammarType = GrammarType.valueOf(type.toUpperCase());
            Grammar grammar = grammarType.createGrammar(grammarStream);
            Generator generator = grammarType.createGenerator(grammar);
            generator.generate(parserStream);
        }
    }

    private static void parse(Path grammarFile, Path inputFile, Path outputFile) throws IOException, SyntaxException {
        System.out.println("Parsing input '" + inputFile + "' using grammar '" + grammarFile + "' into '" + outputFile + "'.");
        try(Reader grammarStream = Files.newBufferedReader(grammarFile);
                Reader inputStream  = Files.newBufferedReader(inputFile);
                Writer outputStream = Files.newBufferedWriter(outputFile)) {
            Grammar grammar = new Grammar(grammarStream);
            Interpreter parser = new Interpreter(grammar);
            String input  = readToEnd(inputStream);
            String output = parser.parse(input);
            outputStream.write(output);
        }
    }

    private static String readToEnd(Reader stream) throws IOException {
        stream.mark(1);
        if(stream.read() != 0xFEFF) {
            stream.reset();
        }
        StringBuilder builder = new StringBuilder();
        int c;
        while((c = stream.read()) != -1) {
            builder.append((char) c);
        }
        return builder.toString();
    }
}
