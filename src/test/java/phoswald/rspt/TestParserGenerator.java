package phoswald.rspt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestParserGenerator {

    @Before
    public void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    public void testGenerateParserJava() throws IOException {
        Files.deleteIfExists(Paths.get("target", "test-output", "CalculatorJava.java"));

        Main.main(new String[] {
                "-gen=java",
                Paths.get("src", "test", "resources", "CalculatorJava.txt").toString(),
                Paths.get("target", "test-output", "CalculatorJava.java").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "CalculatorJava.java"),
                Paths.get("target", "test-output", "CalculatorJava.java"));
    }

    @Test
    public void testGenerateParserCSharp() throws IOException {
        Files.deleteIfExists(Paths.get("target", "test-output", "CalculatorCS.cs"));

        Main.main(new String[] {
                "-gen=cs",
                Paths.get("src", "test", "resources", "CalculatorCS.txt").toString(),
                Paths.get("target", "test-output", "CalculatorCS.cs").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "CalculatorCS.cs"),
                Paths.get("target", "test-output", "CalculatorCS.cs"));
    }

    @Test
    public void testGenerateParserCPlusPlus() throws IOException {
        Files.deleteIfExists(Paths.get("target", "test-output", "CalculatorCPP.h"));

        Main.main(new String[] {
                "-gen=cpp",
                Paths.get("src", "test", "resources", "CalculatorCPP.txt").toString(),
                Paths.get("target", "test-output", "CalculatorCPP.h").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "CalculatorCPP.h"),
                Paths.get("target", "test-output", "CalculatorCPP.h"));
    }

    private void compareFiles(Path expected, Path actual) throws IOException {
        List<String> linesExpected = readFile(expected);
        List<String> linesActual   = readFile(actual);
        for(int i = 0; i < linesExpected.size() && i < linesActual.size(); i++) {
            assertEquals("Line " + (i+1) + " of " + linesExpected.size() + " does not match.", linesExpected.get(i), linesActual.get(i));
        }
        assertEquals("Number of lines do not match.", linesExpected.size(), linesActual.size());
    }

    private List<String> readFile(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try(Reader reader = Files.newBufferedReader(path)) {
            int i;
            while((i = reader.read()) != -1) {
                char c = (char) i;
                switch(c) {
                    case '\r':
                        break;
                    case '\n':
                        lines.add(sb.toString());
                        sb = new StringBuilder();
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
            lines.add(sb.toString());
        }
        return lines;
    }
}
