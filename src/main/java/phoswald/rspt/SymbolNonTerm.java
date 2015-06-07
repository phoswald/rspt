package phoswald.rspt;

import java.util.ArrayList;
import java.util.List;

/**
 * A Non-Terminal Symbol (NTS) is defined by a set of rules.
 */
public class SymbolNonTerm extends Symbol {

    public final List<List<Symbol>> rules = new ArrayList<>();

    /**
     * The type or class of the target language.
     */
    public String type;

    public SymbolNonTerm(String token) {
        super(token);
    }

    public String name() {
        return token;
    }
}
