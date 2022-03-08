package phoswald.rspt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGrammar {

    private static final Charset cs = StandardCharsets.UTF_8;

    @BeforeEach
    void prepare() throws IOException {
        Files.createDirectories(Paths.get("target", "test-output"));
    }

    @Test
    void testCompilerSettings() {
        assertEquals('€', (char) 0x20AC);
        assertEquals("€uro", "\u20ACuro");
    }

    @Test
    void testUtf8() throws IOException, SyntaxException {
        byte[] bytes = " <export> ROOT = '\u20ACuro' | 'bar' ;".getBytes(cs);
        Grammar grammar = new Grammar(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), cs)));
        assertEquals("'€uro'", grammar.getExports().get(0).getRules().get(0).get(0).token);
    }

    @Test
    void testUtf8Bom() throws IOException, SyntaxException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        bytes.write(" <export> ROOT = '\u20ACuro' | 'bar' ;".getBytes(cs));
        Grammar grammar = new Grammar(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes.toByteArray()), cs)));
        assertEquals("'€uro'", grammar.getExports().get(0).getRules().get(0).get(0).token);
    }

    @Test
    void testUtf8BomFile() throws IOException, SyntaxException {
        Path file = Paths.get("target", "test-output", "test-utf8-bom.txt");
        try(OutputStream bytes = Files.newOutputStream(file)) {
            bytes.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
            bytes.write(" <export> ROOT = '\u20ACuro' | 'bar' ;\n".getBytes(cs));
        }
        try(Reader stream = Files.newBufferedReader(file)) {
            Grammar grammar = new Grammar(stream);
            assertEquals("'€uro'", grammar.getExports().get(0).getRules().get(0).get(0).token);
        }
    }

    @Test
    void testUtf16Bom() throws IOException, SyntaxException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(new byte[] { (byte) 0xFF, (byte) 0xFE });
        bytes.write(" <export> ROOT = '\u20ACuro' | 'bar' ;".getBytes(StandardCharsets.UTF_16LE));
        assertThrows(SyntaxException.class, () -> new Grammar(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes.toByteArray()), cs))));
    }
}
