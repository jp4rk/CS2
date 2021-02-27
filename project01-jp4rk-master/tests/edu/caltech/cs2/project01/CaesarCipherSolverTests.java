package edu.caltech.cs2.project01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import edu.caltech.cs2.helpers.Inspection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CaesarCipherSolverTests {
    String STRING_SOURCE = "src/edu/caltech/cs2/project01/CaesarCipherSolver.java";

    @Order(0)
    @Tag("C")
    @DisplayName("Test no usage of split(), join(), contains()")
    @Test
    public void testNoUsageOfBannedMethods() {
        List<String> regexps = List.of("\\.split\\(.*?\\)", "\\.join\\(.*?\\)", "\\.contains\\(.*?\\)");
        Inspection.assertNoUsageOf(STRING_SOURCE, regexps);
    }

    @Order(4)
    @Tag("C")
    @DisplayName("Test rot([], 5)")
    @Test
    public void testEmptyRotArrayList() {
        List<String> output = new ArrayList<>();
        List<String> input = new ArrayList<>();
        CaesarCipherSolver.rot(input, 5);
        assertIterableEquals(output, input);
    }

    @Order(5)
    @Tag("C")
    @DisplayName("Test rot([\"ABCD\"], 1)")
    @Test
    public void testSingleRotArrayList() {
        List<String> output = new ArrayList<>();
        output.add("BCDE");
        List<String> input = new ArrayList<>();
        input.add("ABCD");
        CaesarCipherSolver.rot(input, 1);
        assertIterableEquals(output, input);
    }

    @Order(6)
    @Tag("C")
    @DisplayName("Test rot([\"ABCD\", \"DDDD\"], 1)")
    @Test
    public void testDoubleRotArrayList() {
        List<String> output = new ArrayList<>();
        output.add("BCDE");
        output.add("EEEE");
        List<String> input = new ArrayList<>();
        input.add("ABCD");
        input.add("DDDD");
        CaesarCipherSolver.rot(input, 1);
        assertIterableEquals(output, input);
    }

    @Order(7)
    @Tag("C")
    @ParameterizedTest
    @DisplayName("Test splitBySpaces(String)")
    @ValueSource(strings = { "", "HI", "HI THERE", "A B C D E F GGG" })
    public void testSplitBySpaces(String toSplit) {
        List<String> list = CaesarCipherSolver.splitBySpaces(toSplit);
        assertEquals(toSplit.trim(), String.join(" ", list));
    }

    @Order(8)
    @Tag("C")
    @DisplayName("Test putTogetherWithSpaces([\"ABCD\"])")
    @Test
    public void testPutTogetherWithSpacesSingleton() {
        List<String> input = new ArrayList<>();
        input.add("ABCD");
        Assertions.assertEquals("ABCD", CaesarCipherSolver.putTogetherWithSpaces(input));
    }
    
    @Order(9)
    @Tag("C")
    @DisplayName("Test putTogetherWithSpaces([\"ABCD\", \"DDDD\"])")
    @Test
    public void testPutTogetherWithSpaces() {
        List<String> input = new ArrayList<>();
        input.add("ABCD");
        input.add("DDDD");
        Assertions.assertEquals("ABCD DDDD", CaesarCipherSolver.putTogetherWithSpaces(input));
    }

    @Order(10)
    @Tag("C")
    @DisplayName("Test howManyWordsIn([\"ABCD\", \"DDDD\"], [])")
    @Test
    public void testHowManyWordsInEmpty() {
        List<String> dictionary = new ArrayList<>();
        List<String> sentence = new ArrayList<>();
        sentence.add(new String("ABCD"));
        sentence.add(new String("DDDD"));
        Assertions.assertEquals(0, CaesarCipherSolver.howManyWordsIn(sentence, dictionary));
    }

    @Order(11)
    @Tag("C")
    @DisplayName("Test howManyWordsIn([\"ABCD\", \"DDDD\"], [\"ABCD\"])")
    @Test
    public void testHowManyWordsInSingleton() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add(new String("ABCD"));
        List<String> sentence = new ArrayList<>();
        sentence.add(new String("ABCD"));
        sentence.add(new String("DDDD"));
        Assertions.assertEquals(1, CaesarCipherSolver.howManyWordsIn(sentence, dictionary));
    }
    
    @Order(12)
    @Tag("C")
    @DisplayName("Test howManyWordsIn([\"ABCD\", \"ABCD\", \"NOT\"], [\"ABCD\", \"EFGH\"])")
    @Test
    public void testHowManyWordsWithDuplicates() {
        List<String> dictionary = new ArrayList<>();
        dictionary.add(new String("ABCD"));
        dictionary.add(new String("EFGH"));
        List<String> sentence = new ArrayList<>();
        sentence.add(new String("ABCD"));
        sentence.add(new String("ABCD"));
        sentence.add(new String("NOT"));
        Assertions.assertEquals(2, CaesarCipherSolver.howManyWordsIn(sentence, dictionary));
    }

    @Order(13)
    @Tag("C")
    @ParameterizedTest
    @DisplayName("Test main()")
    @CsvSource({
            "HELLO, HELLO",
            "FYYFHP FY IFBS, ATTACK AT DAWN",
            "AI NYUG, GO TEAM"
    })
    public void testMain(String cipher, String expected) {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		
        byte[] input = cipher.getBytes();
        InputStream fakeIn = new ByteArrayInputStream(input);
        System.setIn(fakeIn);
        CaesarCipherSolver.main(null);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        String output = outputStream.toString();
        System.out.print(output);
        assertEquals("Type a sentence to decrypt: " + expected + "\n", output.replaceAll("\r\n", "\n"));
    }
}