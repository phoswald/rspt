package phoswald.rspt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Interpreter {

    private final Grammar grammar;

    public Interpreter(Grammar grammar) {
        this.grammar = Objects.requireNonNull(grammar);
    }

    public String parse(String input) throws SyntaxException {
        if(grammar.getExports().size() != 1) {
            throw new SyntaxException("The grammar must export exactly one NTS.");
        }

        Ref_int pos = new Ref_int(0);
        StringBuilder output = new StringBuilder();
        if(parseNT(grammar.getExports().get(0), input.toCharArray(), pos, output) && pos.val == input.length()) {
            return output.toString();
        } else {
            String next = input.length()-pos.val <= 50 ? input.substring(pos.val, input.length()) : input.substring(pos.val, pos.val + 50) + "...";
            throw new SyntaxException("Error at offset " + (pos.val+1) + ": Cannot handle '" + next + "'.");
        }
    }

    private boolean parseNT(SymbolNonTerm sym, char[] input, Ref_int pos, StringBuilder output) throws SyntaxException {
        for(List<Symbol> rule : sym.getRules()) {
            List<Integer> posAry = new ArrayList<>();
            List<StringBuilder> outputAry = new ArrayList<>();

            boolean ok = true;
            Ref_int pos2 = new Ref_int(pos.val);
            posAry.add(pos2.val);

            String  ins_to  = null;
            boolean ins_set = false;
            boolean ins_rge = false;
            for(Symbol sym2 : rule) {
                if(sym2 instanceof SymbolNonTerm) {
                    SymbolNonTerm sym2nt = (SymbolNonTerm) sym2;
                    outputAry.add(new StringBuilder());
                    if(!parseNT(sym2nt, input, pos2, outputAry.get(outputAry.size()-1) /* TODO: ins_to is not yet supported */)) {
                        ok = false;
                        break;
                    }
                    posAry.add(pos2.val);
                    ins_to = null;

                } else if(sym2 instanceof SymbolTerm) {
                    SymbolTerm sym2t = (SymbolTerm) sym2;
                    if(ins_set) {
                        if(!parseTSET(input, pos2, sym2t.getText())) {
                            ok = false;
                            break;
                        }
                    } else if(ins_rge) {
                        if(!parseTRGE(input, pos2, sym2t.getText().charAt(0), sym2t.getText().charAt(1))) {
                            ok = false;
                            break;
                        }
                    } else {
                        if(!parseTS(input, pos2, sym2t.getText())) {
                            ok = false;
                            break;
                        }
                    }
                    posAry.add(pos2.val);
                    ins_set = false;
                    ins_rge = false;

                } else if(sym2 instanceof SymbolCode) {
                    interpretCode(((SymbolCode) sym2).getCode(), input, posAry, outputAry, output);

                } else if(sym2 instanceof SymbolInstr) {
                    SymbolInstr sym2i = (SymbolInstr) sym2;
                    switch(sym2i.getInstruction()) {
                        case TO:    ins_to  = sym2i.getToTarget(); break;
                        case SET:   ins_set = true;                break;
                        case RANGE: ins_rge = true;                break;
                        default: throw new SyntaxException("Invalid instruction '" + sym2i.getToken() + "'.");
                    }
                }
            }
            if(ok) {
                pos.val = pos2.val;
                return true;
            }
        }
        return false;
    }

    private boolean parseTS(char[] input, Ref_int pos, String s) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(pos.val >= input.length || input[pos.val] != c) {
                return false;
            }
            pos.val++;
        }
        return true;
    }

    private boolean parseTSET(char[] input, Ref_int pos, String s) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(pos.val < input.length && input[pos.val] == c) {
                pos.val++;
                return true;
            }
        }
        return false;
    }

    private boolean parseTRGE(char[] input, Ref_int pos, char c1, char c2) {
        if(pos.val >= input.length || input[pos.val] < c1 || input[pos.val] > c2) {
            return false;
        }
        pos.val++;
        return true;
    }

    private void interpretCode(String code, char[] input, List<Integer> posAry, List<StringBuilder> outputAry, StringBuilder output) throws SyntaxException {
        for(int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if(c == '$') {
                if(i+2 < code.length() && code.charAt(i+1) == 'i' && code.charAt(i+2) >= '1' && code.charAt(i+2) <= '9') {
                    int i1 = posAry.get(code.charAt(i+2)-'1');
                    int i2 = posAry.get(code.charAt(i+2)-'0');
                    for(int j = i1; j < i2; j++) {
                        output.append(input[j]);
                    }
                    i+=2;
                } else if(i+1 < code.length() && code.charAt(i+1) >= '1' && code.charAt(i+1) <= '9') {
                    output.append(outputAry.get(code.charAt(i+1)-'1'));
                    i++;
                } else if(i+1 < code.length() && code.charAt(i+1) == '$') {
                    output.append('$');
                    i++;
                } else {
                    throw new SyntaxException("Invalid code '"+code+"'.");
                }
            } else {
                output.append(c);
            }
        }
    }

//    private final static class Ref <T> {
//        public T val;
//        public Ref(T val) { this.val = val; }
//    }

    private final static class Ref_int {
        public int val;
        public Ref_int(int val) { this.val = val; }
    }
}
