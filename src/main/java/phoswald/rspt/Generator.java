package phoswald.rspt;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public abstract class Generator {

    protected final Grammar grammar;

    protected final StringBuilder indent = new StringBuilder();

    protected Generator(Grammar grammar) {
        this.grammar = Objects.requireNonNull(grammar);
    }

    protected void indent(int i) {
        if(i > 0) {
            while(i-- > 0) {
                indent.append(' ');
            }
        } else if(i < 0) {
            i = -i;
            indent.delete(indent.length() - i, indent.length());
        }
    }

    protected String quote(String text) {
        StringBuilder sb = new StringBuilder();
        for(char c : text.toCharArray()) {
            switch(c) {
                case '\\': sb.append("\\\\"); break;
                case '\"': sb.append("\\\""); break;
                case '\'': sb.append("\\\'"); break;
                case '\r': sb.append("\\r");  break;
                case '\n': sb.append("\\n");  break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);      break;
            }
        }
        return sb.toString();
    }

    protected String quoteComment(String text) {
        return quote(text.replace("/", " / "));
    }

    protected void writeLine(Writer writer, String line) throws IOException {
        writer.write(line);
        writer.write(System.lineSeparator()); // Note: MONO under Linux will generated '\n' instead of '\r\n' as well
    }

    public abstract void generate(Writer writer) throws IOException, SyntaxException;
}
