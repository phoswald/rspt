package phoswald.rspt;

/**
 * A Terminal Symbol (TS) is a sequence of input symbols.
 */
public class SymbolTerm extends Symbol {

    public SymbolTerm(String token) {
        super(token);
    }

    public String text() {
        return token.substring(1, token.length() - 1);
    }
}
