package phoswald.rspt;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Grammar {

    public final Map<String, SymbolNonTerm> index = new HashMap<>();

    public final List<SymbolNonTerm> nonTerms = new ArrayList<>();

    public final List<SymbolNonTerm> exports = new ArrayList<>();

    /**
     * A list of Java package import, C++ include or C# using directives.
     */
    public final List<String> includes = new ArrayList<>();

    /**
     * The package or namespace of the parser class.
     */
    public String namespace;

    /**
     * The name of the parser class.
     */
    public String clazz;

    /**
     * The type of the input symbols (i.e. character, byte, etc).
     */
    public String type;

    /**
     * A list of source code fragments of the target language.
     */
    public final List<String> codes = new ArrayList<>();

    public Grammar(Reader reader) throws IOException, SyntaxException {
        List<String> tokens = tokenize(reader);
        parse(tokens);
    }

    private static List<String> tokenize(Reader reader) throws IOException, SyntaxException {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        Stack<Character> brace = new Stack<>();
        while(true) {
            int i = reader.read();
            if(i == -1) {
                break;
            }
            char c = (char) i;
            if(brace.size() > 0 && c == brace.peek()) { // closing brace
                brace.pop();
                token.append(c);
            } else if((c == ' ' || c == '\t' || c == '\r' || c == '\n') && brace.size() == 0) { // whitespace (not inside brace)
                if(token.length() > 0) {
                    tokens.add(token.toString());
                    token = new StringBuilder();
                }
            } else if(c == '#' && brace.size() == 0) { // comment (not inside brace)
                if(token.length() > 0) {
                    tokens.add(token.toString());
                    token = new StringBuilder();
                }
                do {
                    i = reader.read();
                } while(i != -1 && i != '\r' && i != '\n');
            } else if((c == '\"' || c == '\'')) { // quoted string
                token.append(c);
                char quote = c;
                while(true) {
                    i = reader.read();
                    if(i == -1 || i == '\r' || i == '\n') {
                        throw new SyntaxException("Unexpected EOL, expected '" + quote + "'.");
                    }
                    c = (char) i;
                    if(c == quote) {
                        token.append(c);
                        break;
                    } else if(c == '\\') {
                        i = reader.read();
                        switch(i) {
                            case '\\': c = '\\'; break;
                            case '\'': c = '\''; break;
                            case '\"': c = '\"'; break;
                            case 't':  c = '\t'; break;
                            case 'r':  c = '\r'; break;
                            case 'n':  c = '\n'; break;
                            default:
                                throw new SyntaxException("Invalid escape sequence, expected one of: \\ \' \" t r n .");
                        }
                        token.append(c);
                    } else {
                        token.append(c);
                    }
                }
            } else if(c == '(') { // opening brace
                brace.push(')');
                token.append(c);
            } else if(c == '{') { // opening brace
                brace.push('}');
                token.append(c);
            } else if(c == '[') { // opening brace
                brace.push(']');
                token.append(c);
            } else if(c == '<' && brace.size() == 0) { // opening brace (only at top level)
                brace.push('>');
                token.append(c);
            } else { // normal character
                token.append(c);
            }
        }
        if(token.length() > 0) {
            tokens.add(token.toString());
        }
        if(brace.size() > 0) {
            throw new SyntaxException("Unexpected EOF, expected '" + brace.peek() + "'.");
        }
        return tokens;
    }

    private void parse(List<String> tokens) throws SyntaxException {
        int pos = 0;
        boolean exp = false;
        while(pos < tokens.size()) {
            String symbol = tokens.get(pos);
            if(symbol.equals("<export>")) {
                exp = true;
            } else if(symbol.startsWith("<include:") && symbol.charAt(symbol.length()-1) == '>') {
                includes.add(symbol.substring(9, symbol.length()-1));
            } else if(symbol.startsWith("<namespace:") && symbol.charAt(symbol.length()-1) == '>') {
                namespace = symbol.substring(11, symbol.length()-1);
            } else if(symbol.startsWith("<class:") && symbol.charAt(symbol.length()-1) == '>') {
                clazz = symbol.substring(7, symbol.length()-1);
            } else if(symbol.length() > 2 && symbol.charAt(0) == '{' && symbol.charAt(symbol.length()-1) == '}') {
                codes.add(symbol.substring(1, symbol.length()-1));
            } else {
                SymbolNonTerm sym = getNonTerm(symbol);
                if(exp) {
                    exports.add(sym);
                    exp = false;
                }
                pos++;
                if(tokens.get(pos).equals(":")) {
                   sym.type = tokens.get(pos+1);
                   pos+=2;
                }
                if(!tokens.get(pos).equals("=")) {
                    throw new SyntaxException(sym.name() + ": Unexptected token '" + tokens.get(pos) + "'. Exptected: '='.");
                }
                do {
                    symbol = tokens.get(++pos);
                    List<Symbol> rule = new ArrayList<>();
                    sym.rules.add(rule);
                    while(!symbol.equals("|") && !symbol.equals(";")) {
                        if(symbol.length() >= 2 && symbol.charAt(0) == '\'' && symbol.charAt(symbol.length()-1) == '\'') {
                            rule.add(new SymbolTerm(symbol));
                        } else if(symbol.length() > 2 && symbol.charAt(0) == '{' && symbol.charAt(symbol.length()-1) == '}') {
                            rule.add(new SymbolCode(symbol));
                        } else if(symbol.length() > 2 && symbol.charAt(0) == '<' && symbol.charAt(symbol.length()-1) == '>') {
                            rule.add(new SymbolInstr(symbol));
                        } else {
                            rule.add(getNonTerm(symbol));
                        }
                        symbol = tokens.get(++pos);
                    }
                } while(!symbol.equals(";"));
            }
            pos++;
        }
        for(SymbolNonTerm sym : nonTerms) {
            if(sym.rules.size() == 0) {
                throw new SyntaxException(sym.name() + ": Symbol is used but not defined.");
            }
        }
    }

    private SymbolNonTerm getNonTerm(String text) throws SyntaxException {
        if(!isIdent(text)) {
            throw new SyntaxException("Invalid non terminal symbol name '" + text + "'.");
        }
        SymbolNonTerm symbol = index.get(text);
        if(symbol == null) {
            symbol = new SymbolNonTerm(text);
            index.put(text, symbol);
            nonTerms.add(symbol);
        }
        return symbol;
    }

    private boolean isIdent(String text) {
        boolean first = true;
        for(char c : text.toCharArray()) {
            if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_') || (!first && c >= '0' && c <= '9'))) {
                return false;
            }
            first = false;
        }
        return true;
    }
}
