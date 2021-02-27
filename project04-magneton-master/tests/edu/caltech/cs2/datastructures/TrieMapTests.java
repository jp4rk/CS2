package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;
import org.junit.jupiter.api.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;

import static edu.caltech.cs2.project04.Project04TestOrdering.classSpecificTestLevel;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrieMapTests implements ITrieMapTests {
    private static String STRING_SOURCE = "src/edu/caltech/cs2/datastructures/TrieMap.java";

    public ITrieMap<Object, Iterable<Object>, Object> newITrieMap() {
        Constructor c = Reflection.getConstructor(TrieMap.class, Function.class);
        Function<IDeque<Object>, Iterable<Object>> collector = (IDeque<Object> o) -> {
            List<Object> k = new ArrayList<>(o.size());
            for (Object m : o) {
                k.add(m);
            }

            return k;
        };

        return Reflection.newInstance(c, collector);
    }

    @Order(classSpecificTestLevel)
    @Tag("C")
    @DisplayName("Does not use or import disallowed classes")
    @Test
    public void testForInvalidClasses() {
        List<String> regexps = List.of("java\\.lang\\.reflect", "java\\.io");
        Inspection.assertNoImportsOf(STRING_SOURCE, regexps);
        Inspection.assertNoUsageOf(STRING_SOURCE, regexps);
    }
}
