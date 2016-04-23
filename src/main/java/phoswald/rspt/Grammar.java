package phoswald.rspt;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Grammar {

    /**
     * The type of the input characters (i.e. character, byte, etc).
     */
    private String inputType;

    /**
     * The default type of non terminal symbols (i.e. Object, void*, etc).
     */
    private String defaultSymbolType;

    /**
     * The name of the parser class.
     */
    private String parserClass;

    /**
     * The package (or namespace) of the parser class.
     */
    private String parserPackage;

    /**
     * A list of import (or include or using) statments.
     */
    private final List<String> importStmts = new ArrayList<>();

    /**
     * A list of source code fragments to be included into the parser class.
     */
    private final List<String> codeFragments = new ArrayList<>();

    private final Map<String, SymbolNonTerm> index = new HashMap<>();

    private final List<SymbolNonTerm> exports = new ArrayList<>();

    private final List<SymbolNonTerm> symbols = new ArrayList<>();

    Grammar(Reader reader) throws IOException, SyntaxException {
        this(reader, null, null, null, null);
    }

    Grammar(Reader reader, String defaultCharacterType, String defaultSymbolType, String defaultParserClass, String defaultParserPackage) throws IOException, SyntaxException {
        this.inputType = defaultCharacterType;
        this.defaultSymbolType = defaultSymbolType;
        this.parserClass = defaultParserClass;
        this.parserPackage = defaultParserPackage;
        parse(tokenize(reader));
    }

    public String getInputType() {
        return inputType;
    }

    public String getParserClass() {
        return parserClass;
    }

    public String getParserPackage() {
        return parserPackage;
    }

    public List<String> getImportStmts() {
        return Collections.unmodifiableList(importStmts);
    }

    public List<String> getCodeFragments() {
        return Collections.unmodifiableList(codeFragments);
    }

    public List<SymbolNonTerm> getExports() {
        return Collections.unmodifiableList(exports);
    }

    public List<SymbolNonTerm> getSymbols() {
        return Collections.unmodifiableList(symbols);
    }

    private static List<String> tokenize(Reader reader) throws IOException, SyntaxException {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        Stack<Character> brace = new Stack<>();
        reader.mark(1);
        if(reader.read() != 0xFEFF) {
            reader.reset();
        }
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
            String token = tokens.get(pos);
            if(token.equals("<export>")) {
                exp = true;
            } else if(token.startsWith("<class:") && token.charAt(token.length()-1) == '>') {
                parserClass = token.substring(7, token.length()-1);
            } else if(token.startsWith("<namespace:") && token.charAt(token.length()-1) == '>') {
                parserPackage = token.substring(11, token.length()-1);
            } else if(token.startsWith("<include:") && token.charAt(token.length()-1) == '>') {
                importStmts.add(token.substring(9, token.length()-1));
            } else if(token.length() > 2 && token.charAt(0) == '{' && token.charAt(token.length()-1) == '}') {
                codeFragments.add(token.substring(1, token.length()-1));
            } else {
                SymbolNonTerm symbol = getSymbol(token);
                symbols.add(symbol);
                if(exp) {
                    exports.add(symbol);
                    exp = false;
                }
                pos++;
                if(tokens.get(pos).equals(":")) {
                   symbol.setType(tokens.get(pos+1));
                   pos+=2;
                }
                if(!tokens.get(pos).equals("=")) {
                    throw new SyntaxException(symbol.getName() + ": Unexptected token '" + tokens.get(pos) + "'. Exptected: '='.");
                }
                do {
                    token = tokens.get(++pos);
                    List<Symbol> rule = new ArrayList<>();
                    symbol.addRule(rule);
                    while(!token.equals("|") && !token.equals(";")) {
                        if(token.length() >= 2 && token.charAt(0) == '\'' && token.charAt(token.length()-1) == '\'') {
                            rule.add(new SymbolTerm(token));
                        } else if(token.length() > 2 && token.charAt(0) == '{' && token.charAt(token.length()-1) == '}') {
                            rule.add(new SymbolCode(token));
                        } else if(token.length() > 2 && token.charAt(0) == '<' && token.charAt(token.length()-1) == '>') {
                            rule.add(new SymbolInstr(token));
                        } else {
                            rule.add(getSymbol(token));
                        }
                        token = tokens.get(++pos);
                    }
                } while(!token.equals(";"));
            }
            pos++;
        }
        for(SymbolNonTerm symbol : index.values()) {
            if(symbol.getRules().size() == 0) {
                throw new SyntaxException(symbol.getName() + ": Symbol is used but not defined.");
            }
            if(symbol.getType() == null) {
                symbol.setType(defaultSymbolType);
            }
        }
    }

    private SymbolNonTerm getSymbol(String token) throws SyntaxException {
        if(!isIdent(token)) {
            throw new SyntaxException("Invalid non terminal symbol name '" + token + "'.");
        }
        SymbolNonTerm symbol = index.get(token);
        if(symbol == null) {
            symbol = new SymbolNonTerm(token);
            index.put(token, symbol);
        }
        return symbol;
    }

    private boolean isIdent(String token) {
        boolean first = true;
        for(char c : token.toCharArray()) {
            if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_') || (!first && c >= '0' && c <= '9'))) {
                return false;
            }
            first = false;
        }
        return true;
    }
}
