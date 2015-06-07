package phoswald.rspt;

import java.util.Objects;

public class Interpreter {

    private final Grammar grammar;

    public Interpreter(Grammar grammar) {
        this.grammar = Objects.requireNonNull(grammar);
    }

    public String parse(String input) {
        throw new UnsupportedOperationException(); // TODO implement
    }
}
