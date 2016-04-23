package phoswald.rspt;

import java.io.IOException;
import java.io.Reader;

public enum GrammarType {
    JAVA {
        @Override public Grammar createGrammar(Reader reader) throws IOException, SyntaxException {
            return new Grammar(reader, "char", "Object", "Parser", null);
        }
        @Override public Generator createGenerator(Grammar grammar) {
            return new GeneratorJava(grammar);
        }
    },
    CS {
        @Override public Grammar createGrammar(Reader reader) throws IOException, SyntaxException {
            return new Grammar(reader, "char", "object", "Parser", null);
        }
        @Override public Generator createGenerator(Grammar grammar) {
            return new GeneratorCSharp(grammar);
        }
    },
    CPP {
        @Override public Grammar createGrammar(Reader reader) throws IOException, SyntaxException {
            return new Grammar(reader, "TCHAR", "void*", "CParser", null);
        }
        @Override public Generator createGenerator(Grammar grammar) {
            return new GeneratorCPlusPlus(grammar);
        }
    };

    public abstract Grammar createGrammar(Reader reader) throws IOException, SyntaxException;
    public abstract Generator createGenerator(Grammar grammar);
}
