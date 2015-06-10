package phoswald.rspt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestParserGenerator {

    @Before
    public void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    public void testCalculatorJava() throws IOException {
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
    public void testCalculatorCSharp() throws IOException {
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
    public void testCalculatorCPlusPlus() throws IOException {
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
    public void testIcbScriptCPlusPlus() throws IOException {
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
    public void testLogAnalyzerCPlusPlus() throws IOException {
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
    public void testUdmJava() throws IOException {
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
            assertEquals("Line " + (i+1) + " of " + linesExpected.size() + " does not match.", linesExpected.get(i), linesActual.get(i));
        }
        assertEquals("Number of lines do not match.", linesExpected.size(), linesActual.size());
    }
}
