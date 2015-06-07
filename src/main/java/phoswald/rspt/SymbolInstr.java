package phoswald.rspt;

/**
 * An instruction modifies the following TS or NTS.
 */
public class SymbolInstr extends Symbol {

    public SymbolInstr(String token) {
        super(token);
    }

    public Instruction instruction() throws SyntaxException {
        if(token.startsWith("<to:")) {
            return Instruction.TO;
        }
        if(token.equals("<set>")) {
            return Instruction.SET;
        }
        if(token.equals("<range>")) {
            return Instruction.RANGE;
        }
        if(token.equals("<notset>")) {
            return Instruction.NOTSET;
        }
        throw new SyntaxException("Invalid instruction '" + token + "'.");
    }

    public String toResult() {
        return token.substring(4, token.length() - 1);
    }
}
