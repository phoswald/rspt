package phoswald.rspt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGrammarInterpeter {

    @BeforeEach
    void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    void testSyntaxHighlightHtml() throws IOException, SyntaxException {
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
            assertEquals(linesExpected.get(i), linesActual.get(i), "Line " + (i+1) + " of " + linesExpected.size() + " does not match.");
        }
        assertEquals(linesExpected.size(), linesActual.size(), "Number of lines do not match.");
    }
}
