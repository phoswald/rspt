package phoswald.rspt;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("RSPT - Really Simple Parser Tool - (C) 2015 Philip Oswald");
            System.out.println("       https://github.com/phoswald/rspt");
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
            System.out.println("  All parsers are top down (recursive descent) parsers that");
            System.out.println("  can parse non-left recursive LL(x) grammars. Grammars contain rules,");
            System.out.println("  actions to transform machting input into output and instructions to");
            System.out.println("  customize the code generator or interpreter.");
            return;
        }

        int i = 0;
        while(i < args.length) {
            if(args[i].startsWith("-gen=")) {
                if(i+2 >= args.length) {
                    System.out.println("ERROR: Not enough arguments for -gen=...");
                    break;
                }
                generate(args[i+1], args[i+2], args[i].substring(5));
                i += 3;

            } else if(args[i] == "-par=txt") {
                if(i+3 >= args.length) {
                    System.out.println("ERROR: Not enough arguments for -par=...");
                    break;
                }
                parse(args[i+1], args[i+2], args[i+3]);
                i += 4;

            } else {
                System.out.println("ERROR: Invalid argument '" + args[i] + "'.");
                break;
            }
        }
    }

    private static void generate(String grammarFile, String parserFile, String type) {
        System.out.println("Generating parser '" + parserFile + "' from grammar '" + grammarFile + "'.");

        try {
            try(Reader grammarStream = Files.newBufferedReader(Paths.get(grammarFile))) {
                Grammar grammar = new Grammar(grammarStream);
                try(Writer parserStream = Files.newBufferedWriter(Paths.get(parserFile))) {
                    Generator generator;
                    if(type.equals("java")) {
                        generator = new GeneratorJava(grammar);
                    } else if(type.equals("cs")) {
                        generator = new GeneratorCSharp(grammar);
                    } else if(type.equals("cpp")) {
                        generator = new GeneratorCPlusPlus(grammar);
                    } else {
                        throw new SyntaxException("Invalid generator type '" + type + "'.");
                    }
                    generator.generate(parserStream);
                }
            }
        } catch(SyntaxException | IOException ex) {
            System.out.println("ERROR: Failed to generate parser: " + ex.getMessage() + "\r\n\r\n");
            ex.printStackTrace(System.out);
            return;
        }
    }

    private static void parse(String grammarFile, String inputFile, String outputFile) {
        System.out.println("Parsing input '" + inputFile + "' using grammar '" + grammarFile + "' into '" + outputFile + "'.");

        try {
            try(Reader grammarStream = Files.newBufferedReader(Paths.get(grammarFile))) {
                Grammar grammar = new Grammar(grammarStream);
                try(Reader inputStream  = Files.newBufferedReader(Paths.get(inputFile))) {
                    try(Writer outputStream = Files.newBufferedWriter(Paths.get(outputFile))) {
                        Interpreter parser = new Interpreter(grammar);
                        String input  = readToEnd(inputStream);
                        String output = parser.parse(input);
                        if(output != null) {
                            outputStream.write(output.toCharArray());
                        }
                    }
                }
            }
        } catch(SyntaxException | IOException ex) {
            System.out.println("ERROR: Failed to parse: " + ex.getMessage() + "\r\n\r\n");
            ex.printStackTrace(System.out);
            return;
        }
    }

    private static String readToEnd(Reader stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while((c = stream.read()) != -1) {
            builder.append((char) c);
        }
        return builder.toString();
    }
}
