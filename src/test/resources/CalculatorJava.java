//
// NOTE: This file has been generated by RSPT (the Really Simple Parser Tool).
//       Do not modify the contents of this file as it will be overwritten!
//
package calculator;

import java.util.*;

public class CalculatorParser {

    private char[] _input;

    public CalculatorParser() { }

    private static final Map<String,Double> _variables = new HashMap<String,Double>();

    public boolean Parse_ROOT(String input, final Ref<String> output, final Ref_int pos) {
        _input     = input.toCharArray();
        pos.val    = 0;
        output.val = null;
        return nt_ROOT(pos, output) && pos.val == _input.length;
    }

    public boolean Parse_EXPRESSION(String input, final Ref<Double> output, final Ref_int pos) {
        _input     = input.toCharArray();
        pos.val    = 0;
        output.val = null;
        return nt_EXPRESSION(pos, output) && pos.val == _input.length;
    }

    private boolean nt_ROOT(final Ref_int pos, final Ref<String> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(ts(pos1, t_1 /* "Version" */)) {
                output.val = "Version 1.11 for Java";
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(ts(pos1, t_2 /* "About" */)) {
                output.val = "Copyright (C) 2013 Philip Oswald";
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_EXPRESSION(pos1, output1)) {
                output.val = output1.val.toString();
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_EXPRESSION(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_EXPRESSION_SET(pos1, output1)) {
                output.val = output1.val;
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_EXPRESSION_SET(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<String> output1 = new Ref<String>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_IDENT(pos1, output1)) {
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(tc(pos2, '=')) {
                    final Ref<Double> output3 = new Ref<Double>(null);
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(nt_EXPRESSION_SET(pos3, output3)) {
                        output.val = output3.val; _variables.put(output1.val, output.val);
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_EXPRESSION_ADD(pos1, output1)) {
                output.val = output1.val;
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_EXPRESSION_ADD(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_EXPRESSION_MUL(pos1, output1)) {
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_OP_ADD(pos2, output1)) {
                    output.val = output1.val;
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nt_OP_ADD(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '+')) {
                final Ref<Double> output2 = new Ref<Double>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_EXPRESSION_MUL(pos2, output2)) {
                    output.val += output2.val;
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(nt_OP_ADD(pos3, output)) {
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '-')) {
                final Ref<Double> output2 = new Ref<Double>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_EXPRESSION_MUL(pos2, output2)) {
                    output.val -= output2.val;
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(nt_OP_ADD(pos3, output)) {
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            pos.val = pos0.val;
            return true;
        }
    }

    private boolean nt_EXPRESSION_MUL(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_EXPRESSION_BRA(pos1, output1)) {
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_OP_MUL(pos2, output1)) {
                    output.val = output1.val;
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nt_OP_MUL(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '*')) {
                final Ref<Double> output2 = new Ref<Double>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_EXPRESSION_BRA(pos2, output2)) {
                    output.val *= output2.val;
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(nt_OP_MUL(pos3, output)) {
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '/')) {
                final Ref<Double> output2 = new Ref<Double>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_EXPRESSION_BRA(pos2, output2)) {
                    output.val /= output2.val;
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(nt_OP_MUL(pos3, output)) {
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            pos.val = pos0.val;
            return true;
        }
    }

    private boolean nt_EXPRESSION_BRA(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '(')) {
                final Ref<Double> output2 = new Ref<Double>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_EXPRESSION(pos2, output2)) {
                    final Ref_int pos3 = new Ref_int(pos2.val);
                    if(tc(pos3, ')')) {
                        output.val = output2.val;
                        pos.val = pos3.val;
                        return true;
                    }
                }
            }
        }
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_VALUE(pos1, output1)) {
                output.val = output1.val;
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_VALUE(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Double> output1 = new Ref<Double>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_SYMBOL(pos1, output1)) {
                output.val = output1.val;
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref<String> output1 = new Ref<String>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_CONST(pos1, output1)) {
                output.val = Double.valueOf(output1.val);
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_SYMBOL(final Ref_int pos, final Ref<Double> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(ts(pos1, t_3 /* "pi" */)) {
                output.val = Math.PI;
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, 'e')) {
                output.val = Math.E;
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref<String> output1 = new Ref<String>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_IDENT(pos1, output1)) {
                output.val = _variables.containsKey(output1.val) ? _variables.get(output1.val) : 0.0;
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_IDENT(final Ref_int pos, final Ref<String> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Object> output1 = new Ref<Object>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_IDENTCHAR_1(pos1, output1)) {
                final Ref<Object> output2 = new Ref<Object>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_IDENTCHARS_N(pos2, output2)) {
                    output.val = new String(_input, pos0.val, pos2.val-pos0.val);
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nt_IDENTCHARS_N(final Ref_int pos, final Ref<Object> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Object> output1 = new Ref<Object>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_IDENTCHAR_N(pos1, output1)) {
                final Ref<Object> output2 = new Ref<Object>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_IDENTCHARS_N(pos2, output2)) {
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        {
            pos.val = pos0.val;
            return true;
        }
    }

    private boolean nt_IDENTCHAR_1(final Ref_int pos, final Ref<Object> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, 'a', 'z')) {
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, 'A', 'Z')) {
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '_')) {
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_IDENTCHAR_N(final Ref_int pos, final Ref<Object> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, 'a', 'z')) {
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, 'A', 'Z')) {
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(tc(pos1, '_')) {
                pos.val = pos1.val;
                return true;
            }
        }
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, '0', '9')) {
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean nt_CONST(final Ref_int pos, final Ref<String> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Object> output1 = new Ref<Object>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_DIGIT(pos1, output1)) {
                final Ref<Object> output2 = new Ref<Object>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_DIGITS(pos2, output2)) {
                    output.val = new String(_input, pos0.val, pos2.val-pos0.val);
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nt_DIGITS(final Ref_int pos, final Ref<Object> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref<Object> output1 = new Ref<Object>(null);
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(nt_DIGIT(pos1, output1)) {
                final Ref<Object> output2 = new Ref<Object>(null);
                final Ref_int pos2 = new Ref_int(pos1.val);
                if(nt_DIGITS(pos2, output2)) {
                    pos.val = pos2.val;
                    return true;
                }
            }
        }
        {
            pos.val = pos0.val;
            return true;
        }
    }

    private boolean nt_DIGIT(final Ref_int pos, final Ref<Object> output) {
        final Ref_int pos0 = new Ref_int(pos.val);
        {
            final Ref_int pos1 = new Ref_int(pos0.val);
            if(trange(pos1, '0', '9')) {
                pos.val = pos1.val;
                return true;
            }
        }
        return false;
    }

    private boolean ts(final Ref_int pos, char[] s) {
        for(char c : s) {
            if(pos.val >= _input.length || _input[pos.val] != c) return false;
            pos.val++;
        }
        return true;
    }

    private boolean tc(final Ref_int pos, char c) {
        if(pos.val >= _input.length || _input[pos.val] != c) return false;
        pos.val++;
        return true;
    }

    private boolean trange(final Ref_int pos, char c1, char c2) {
        if(pos.val >= _input.length || _input[pos.val] < c1 || _input[pos.val] > c2) return false;
        pos.val++;
        return true;
    }

    private static final char[] t_1 = "Version".toCharArray();
    private static final char[] t_2 = "About".toCharArray();
    private static final char[] t_3 = "pi".toCharArray();

    public final static class Ref <T> {
        public T val;
        public Ref(T val) { this.val = val; }
    }

    public final static class Ref_int {
        public int val;
        public Ref_int(int val) { this.val = val; }
    }
}
