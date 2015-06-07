package phoswald.rspt;

/**
 * A source code fragment of the target language.
 */
public class SymbolCode extends Symbol {

    public SymbolCode(String token) {
        super(token);
    }

    public String code() {
        return token.substring(1, token.length() - 1).trim();
    }
}
