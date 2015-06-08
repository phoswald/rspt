package phoswald.rspt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Non-Terminal Symbol (NTS) is defined by a set of rules.
 */
public class SymbolNonTerm extends Symbol {

    /**
     * The type or class name the NTS evaluates to.
     */
    private String type;

    /**
     * The list of rules that defines this NTS.
     */
    private final List<List<Symbol>> rules = new ArrayList<>();

    public SymbolNonTerm(String token) {
        super(token);
    }

    public String getName() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<Symbol>> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public void addRule(List<Symbol> rule) {
        rules.add(rule);
    }
}
