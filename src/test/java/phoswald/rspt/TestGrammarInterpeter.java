package phoswald.rspt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestGrammarInterpeter {

    @Before
    public void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    public void testSyntaxHighlightHtml() throws IOException {
        Files.deleteIfExists(Paths.get("target", "test-output", "SyntaxHighlight.html"));

        Main.main(new String[] {
                "-par=txt",
                Paths.get("src", "test", "resources", "SyntaxHighlight.txt").toString(),
                Paths.get("src", "test", "resources", "SyntaxHighlight.cs").toString(),
                Paths.get("target", "test-output", "SyntaxHighlight.html").toString() });

        compareFiles(
                Paths.get("src", "test", "resources", "SyntaxHighlight.html"),
                Paths.get("target", "test-output", "SyntaxHighlight.html"));
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
