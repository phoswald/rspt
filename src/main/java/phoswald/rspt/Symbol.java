package phoswald.rspt;

import java.util.Objects;

/**
 * Base class for NTS and TS and other special symbols
 */
public abstract class Symbol {

    public final String token;

    protected Symbol(String token) {
        this.token = Objects.requireNonNull(token);
    }
}
