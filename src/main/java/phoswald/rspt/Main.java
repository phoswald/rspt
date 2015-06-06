package phoswald.rspt;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("RSPT - Really Simple Parser Tool - (C) 2015 Philip Oswald");
            System.out.println("       https://github.com/phoswald/rspt");
            System.out.println("Usage: $ RSPT.exe -gen=cs   <grammar.txt> <output.cs>");
            System.out.println("                  -gen=cpp  <grammar.txt> <output.h>");
            System.out.println("                  -gen=java <grammar.txt> <output.java>");
            System.out.println("                  -par=txt  <grammar.txt> <input.txt> <output.txt>");
            System.out.println("Where:");
            System.out.println("    -gen=cs   generates a parser for the given grammar in C#");
            System.out.println("    -gen=cpp  generates a parser for the given grammar in C++");
            System.out.println("    -gen=java generates a parser for the given grammar in Java");
            System.out.println("    -par=txt  parses the input using the given grammar");
            System.out.println("Notes:");
            System.out.println("  All parsers are top down (recursive descent) parsers that");
            System.out.println("  can parse non-left recursive LL(x) grammars. Grammars contain rules,");
            System.out.println("  actions to transform machting input into output and instructions to");
            System.out.println("  customize the code generator or interpreter.");
            return;
        }
    }
}
