package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.IDictionary;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;

import static edu.caltech.cs2.project05.Project05TestOrdering.classSpecificTestLevel;
import static edu.caltech.cs2.project05.Project05TestOrdering.specialTestLevel;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("B")
public class MoveToFrontDictionaryTests implements IDictionaryNGramTests {
    private static String DICTIONARY_SOURCE =
            "src/edu/caltech/cs2/datastructures/MoveToFrontDictionary.java";
    public int CONSTANT_TIMEOUT_MS = 15;

    public int SINGLE_OP_TIMEOUT_MS() {
        return 100;
    }
    public int CONTAINS_VALUE_TIMEOUT_MS() {
        return 100;
    }

    @Override
    public RuntimeInstrumentation.ComplexityType getAndPutComplexity() {
        return RuntimeInstrumentation.ComplexityType.LINEAR;
    }

    // No restrictions on key type for MoveToFront
    @Override
    public IDictionary<Object, Object> newIDictionary() {
        return new MoveToFrontDictionary<>();
    }


    @Order(classSpecificTestLevel)
    @DisplayName("Does not use or import disallowed classes")
    @Test
    public void testForInvalidClasses() {
        List<String> regexps = List.of("java\\.util\\.^(?!Iterator)(?!Supplier)(?!Stream)", "java\\.lang\\.reflect", "java\\.io");
        Inspection.assertNoImportsOf(DICTIONARY_SOURCE, regexps);
        Inspection.assertNoUsageOf(DICTIONARY_SOURCE, regexps);
    }

    @Test
    @DisplayName("Sanity check that accessing keys in various locations works")
    @Order(classSpecificTestLevel)
    public void testDataLocations() {
        MoveToFrontDictionary<Integer, Integer> impl = new MoveToFrontDictionary<>();
        HashMap<Integer, Integer> ref = new HashMap<>();

        for (int i = 0; i < 10; i ++) {
            impl.put(i, i);
            ref.put(i, i);
        }

        // Check access of element at front
        assertEquals(ref.get(9), impl.get(9), "Getting an element from the front failed.");

        // Check accessing whatever element is at the back
        for (int i = 0; i < 10; i ++) {
            assertEquals(ref.get(i), impl.get(i), "Getting an element from the back returns incorrect result.");
            assertEquals(ref.get(i), impl.get(i), "Key that was just gotten is now missing.");
        }

        // Check removing element at the front
        for (int i = 9; i >= 0; i --) {
            assertEquals(ref.remove(i), impl.remove(i), "Removing an element from the front failed.");
        }

        // Repopulate to make sure that emptying it didn't bork it
        for (int i = 0; i < 10; i ++) {
            impl.put(i, i);
            ref.put(i, i);
        }

        // And repeat.
        assertEquals(ref.get(9), impl.get(9));
        for (int i = 0; i < 10; i ++) {
            assertEquals(ref.get(i), impl.get(i), "Getting an element from the back failed.");
        }
    }

    @Test
    @DisplayName("Test that referencing a key moves it to the front")
    @Order(specialTestLevel)
    public void testMoveToFrontProperty() {
        MoveToFrontDictionary<Integer, Integer> impl = new MoveToFrontDictionary<>();
        final int DICT_SIZE = 30000;
        for (int i = 0; i <= DICT_SIZE; i ++) {
            impl.put(i, i);
        }

        double totalTimeRetrieveFront = 0;
        long startTime, endTime;
        for (int i = 0; i <= DICT_SIZE; i ++) {
            // Get key from back to move to front
            impl.get(i);

            // Clock retrieval of key from front.
            startTime = System.nanoTime();
            impl.get(i);
            endTime = System.nanoTime();
            totalTimeRetrieveFront += (endTime - startTime);
        }
        assertTrue(CONSTANT_TIMEOUT_MS > totalTimeRetrieveFront / 1000000, "Retrieving keys after touching them once takes too long.");
    }

    // MoveToFrontDictionary takes quadratic time to build, so don't run standard complexity tests.
    @Override
    public void testSizeComplexity() {}
    @Override
    public void testGetComplexity() {}
    @Override
    public void testPutComplexity() {}
}
