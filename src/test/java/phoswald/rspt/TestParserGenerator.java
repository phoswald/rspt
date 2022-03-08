package phoswald.rspt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestParserGenerator {

    @BeforeEach
    void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    void testCalculatorJava() throws IOException, SyntaxException {
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
    void testCalculatorCSharp() throws IOException, SyntaxException {
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
    void testCalculatorCPlusPlus() throws IOException, SyntaxException {
        Files.deleteIfExists(Paths.get("target", "test-output", "CalculatorCPP.h"));

        Main.main(new String[] {
                "-gen=cpp",
                Paths.get("src", "test", "resources", "CalculatorCPP.txt").toString(),
                Paths.get("target", "test-output", "CalculatorCPP.h").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "CalculatorCPP.h"),
                Paths.get("target", "test-output", "CalculatorCPP.h"));
    }

    @Test
    void testIcbScriptCPlusPlus() throws IOException, SyntaxException {
        Files.deleteIfExists(Paths.get("target", "test-output", "IcbScript.h"));

        Main.main(new String[] {
                "-gen=cpp",
                Paths.get("src", "test", "resources", "IcbScript.txt").toString(),
                Paths.get("target", "test-output", "IcbScript.h").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "IcbScript.h"),
                Paths.get("target", "test-output", "IcbScript.h"));
    }

    @Test
    void testLogAnalyzerCPlusPlus() throws IOException, SyntaxException {
        Files.deleteIfExists(Paths.get("target", "test-output", "LogAnalyzer.h"));

        Main.main(new String[] {
                "-gen=cpp",
                Paths.get("src", "test", "resources", "LogAnalyzer.txt").toString(),
                Paths.get("target", "test-output", "LogAnalyzer.h").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "LogAnalyzer.h"),
                Paths.get("target", "test-output", "LogAnalyzer.h"));
    }

    @Test
    void testUdmJava() throws IOException, SyntaxException {
        Files.deleteIfExists(Paths.get("target", "test-output", "UDM.java"));

        Main.main(new String[] {
                "-gen=java",
                Paths.get("src", "test", "resources", "UDM.txt").toString(),
                Paths.get("target", "test-output", "UDM.java").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "UDM.java"),
                Paths.get("target", "test-output", "UDM.java"));
    }

    private void compareFiles(Path expected, Path actual) throws IOException {
        List<String> linesExpected = Files.readAllLines(expected);
        List<String> linesActual   = Files.readAllLines(actual);
        for(int i = 0; i < linesExpected.size() && i < linesActual.size(); i++) {
            assertEquals(linesExpected.get(i), linesActual.get(i), "Line " + (i+1) + " of " + linesExpected.size() + " does not match.");
        }
        assertEquals(linesExpected.size(), linesActual.size(), "Number of lines do not match.");
    }
}
