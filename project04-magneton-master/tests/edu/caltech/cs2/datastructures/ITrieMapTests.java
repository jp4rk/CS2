package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ITrieMap;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static edu.caltech.cs2.project04.Project04TestOrdering.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface ITrieMapTests extends IDictionaryTests {
    ITrieMap<Object, Iterable<Object>, Object> newITrieMap();

    static List<Object> getFirstNElements(Iterable<Object> iter, int nElem) {
        List<Object> ret = new ArrayList<>();
        if (nElem <= 0) {
            return ret;
        }
        for (Object o : iter) {
            ret.add(o);
            if (ret.size() >= nElem) {
                break;
            }
        }
        return ret;
    }

    /*
     * This is a way of working around Java's generics.
     * Basic idea is that IDictionary makes no assumptions about key type, but
     * TrieMap does. So IDictionary<Object, Object> created from IDictionary<Iterable<Object>, Object>
     * could potentially have non Iterable objects inserted, which would break it.
     *
     * In the IDictionaryTest class, however, IDictionary only obtains data from generateRandomTestData, which is
     * passed up as a required method to implement, so we place the burden on the implementer to ensure that
     * the data generated matches the expected types.
    */
    default IDictionary<Object, Object> newIDictionary() {
        return (IDictionary<Object, Object>) (IDictionary<? extends Object, Object>) newITrieMap();
    }

    default Map<Object, Object> generateRandomTestData(int size, Random rand, int maxNodeDegree, int minKeyLength, int maxKeyLength) {
        return (Map<Object, Object>)(Map<? extends Object, Object>) generateRandomTestDataIterable(size, rand, maxNodeDegree, minKeyLength, maxKeyLength);
    }

    default Map<Iterable<Object>, Object> generateRandomTestDataIterable(int size, Random rand, int maxNodeDegree, int minKeyLength, int maxKeyLength) {
        Map<Iterable<Object>, Object> base = new HashMap<>();
        for (int i = 0; i < size; i ++) {
            int keyLength = minKeyLength + rand.nextInt(maxKeyLength-minKeyLength);
            List<Object> key = new ArrayList<>();
            for (int j = 0; j < keyLength; j ++) {
                key.add(rand.nextInt(maxNodeDegree));
            }
            base.put(key, rand.nextInt());
        }
        return base;
    }
    default Map<Iterable<Object>, Object> generateRandomTestDataIterable(int size, Random rand) {
        return generateRandomTestDataIterable(size, rand, 10, 1, 20);
    }

    @Override
    default Map<Object, Object> createReferenceMap(String[] keys, Object[] vals) {
        return (Map<Object, Object>) (Map<? extends Object, Object>) createReferenceIterableMap(keys, vals);
    }

    default Map<Iterable<Object>, Object> createReferenceIterableMap(String[] keys, Object[] vals) {
        Map<Iterable<Object>, Object> ref = new HashMap<>();
        for (int i = 0; i < keys.length; i ++) {
            Character[] charArr = keys[i].chars().mapToObj(c -> (char) c).toArray(Character[]::new);
            ref.put(Arrays.asList(charArr), vals[i]);
        }
        return ref;
    }

    /* Types, man. I spent a while trying to work out how to force it, and couldn't.
     * Leaving it like this is a (probably correct) workaround
     * (Problem is that Set<Iterable<Object>> and List<List<Object>> are incompatible)
     * (And need the two types for stress and sanity tests. It's been a while since I wrote this.)
     */
    default List<Object> naiveGetCompletions(Map<Iterable<Object>, Object> map, Collection<Iterable<Object>> keys, List<Object> prefix) {
        // Gather matched keys naively
        List<Object> matchingValues = new ArrayList<>();

        for (Iterable<Object> kIter : keys) {
            List<Object> k = new ArrayList<>();
            kIter.forEach(k::add);
            if (k.size() < prefix.size()) continue;

            if (k.subList(0, prefix.size()).equals(prefix)) {
                matchingValues.add(map.get(k));
            }
        }

        return matchingValues;
    }

    default List<Object> naiveGetCompletions(Map<Iterable<Object>, Object> map, List<List<Object>> keys, List<Object> prefix) {
        // Gather matched keys naively
        List<Object> matchingValues = new ArrayList<>();

        for (List<Object> k : keys) {
            if (k.size() < prefix.size()) continue;

            if (k.subList(0, prefix.size()).equals(prefix)) {
                matchingValues.add(map.get(k));
            }
        }

        return matchingValues;
    }

    default Stream<Arguments> iDictionarySanityDataSource() {
        return Stream.of(
                Arguments.of(createReferenceMap(
                        new String[]{"a", "ab", "abc", "abcd", "abcde"},
                        new Integer[]{1, 2, 3, 4, 5})),
                Arguments.of(createReferenceMap(
                        new String[]{"abcde", "abcd", "abc", "ab", "a"},
                        new Integer[]{1, 2, 3, 4, 5})),
                Arguments.of(createReferenceMap(
                        new String[]{"a", "add", "app"},
                        new String[]{"hello", "1 + 1", "for a phone"})),
                Arguments.of(createReferenceMap(
                        new String[]{"adam", "add", "app", "bad", "bag", "bags", "beds", "bee", "cab"},
                        new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8}))
        );
    }

    default Stream<Arguments> iTrieMapSanityDataSource() {
        return Stream.of(
                Arguments.of(createReferenceIterableMap(
                        new String[]{"a", "ab", "abc", "abcd", "abcde"},
                        new Integer[]{1, 2, 3, 4, 5}),
                        new String[]{"b"}),
                Arguments.of(createReferenceIterableMap(
                        new String[]{"abcde", "abcd", "abc", "ab", "a"},
                        new Integer[]{1, 2, 3, 4, 5}),
                        new String[]{"b"}),
                Arguments.of(createReferenceIterableMap(
                        new String[]{"a", "add", "app"},
                        new String[]{"hello", "1 + 1", "for a phone"}),
                        new String[]{"adds"}),
                Arguments.of(createReferenceIterableMap(
                        new String[]{"adam", "add", "app", "bad", "bag", "bags", "beds", "bee", "cab"},
                        new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8}),
                        new String[]{"bay"})
        );
    }

    default void iTrieMapSanityTestHelper(Map<Iterable<Object>, Object> base, String[] negativePrefixesToCheck, boolean testRemove) {
        // Build ITrieMap
        ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
        for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
            impl.put(e.getKey(), e.getValue());
        }

        // Test known positive cases
        for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
            List<Object> keyAsList = new ArrayList<>();
            e.getKey().forEach(keyAsList::add);

            for (int i = 0; i < keyAsList.size(); i ++) {
                List<Object> prefix = keyAsList.subList(0, i);
                assertTrue(impl.isPrefix(prefix), "isPrefix returns false for present prefix " + prefix);

                List<Object> trueCompletions = naiveGetCompletions(base, base.keySet(), prefix);
                MatcherAssert.assertThat("getCompletions", impl.getCompletions(prefix),
                        IsIterableContainingInAnyOrder.containsInAnyOrder(trueCompletions.toArray()));
            }
        }

        // Test provided negative cases
        for (String prefixStr : negativePrefixesToCheck) {
            Character[] charArr = prefixStr.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
            List<Object> prefix = Arrays.asList(charArr);

            assertFalse(impl.isPrefix(prefix), "isPrefix returns true for missing prefix " + prefix);
            assertEquals(0, impl.getCompletions(prefix).size(), "getCompletions returns nonempty completions for missing prefix " + prefix);
        }

        // Simple test for removals
        if (testRemove) {
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                impl.remove(e.getKey());
            }
            for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
                assertFalse(impl.isPrefix(e.getKey()), "isPrefix returns true for removed key " + e.getKey());
                assertEquals(0, impl.getCompletions(e.getKey()).size(), "getCompletions returns nonempty completions for missing key " + e.getKey());
            }
        }
    }

    default void iTrieMapStressTestHelper(int seed, int size, boolean testRemove) {
        Random rand = new Random(seed);

        Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(size, rand);
        ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
        int minPrefixSize = 2;
        int maxPrefixSize = 20;

        Set<List<Object>> excludedPrefixes = new HashSet<>();
        for (Iterable<Object> k : base.keySet()) {
            if (rand.nextDouble() < 0.1) {
                int prefixSize = minPrefixSize + rand.nextInt(maxPrefixSize-minPrefixSize);
                excludedPrefixes.add(getFirstNElements(k, prefixSize));
            }
        }

        Set<List<Object>> includedKeys = new HashSet<>();
        Set<List<Object>> excludedKeys = new HashSet<>();
        for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
            // If testRemove, excluded keys will be removed later
            boolean exclude = testRemove;
            List<Object> list = new ArrayList<>();
            e.getKey().forEach(list::add);

            if (!testRemove) {
                for (int i = 0; i <= list.size() && !exclude; i ++) {
                    if (excludedPrefixes.contains(list.subList(0, i))) {
                        exclude = true;
                    }
                }
            }
            if (!exclude) {
                impl.put(e.getKey(), e.getValue());
                includedKeys.add(list);
            }
            else {
                excludedKeys.add(list);
            }
        }

        // Test isPrefix positive cases
        for (List<Object> k : includedKeys) {
            for (int i = 0; i <= k.size(); i++) {
                assertTrue(impl.isPrefix(k.subList(0, i)), "Prefix that was added returns false from isPrefix.");
            }
        }

        // Test getCompletions random cases
        int numCompletionsToCheck = 50;
        List<List<Object>> keyList = new ArrayList<>(includedKeys);
        for (int i = 0; i < numCompletionsToCheck; i ++) {
            List<Object> key = keyList.get(rand.nextInt(includedKeys.size()));
            int prefixSize = 1 + rand.nextInt(key.size());
            List<Object> prefix = key.subList(0, prefixSize);
            List<Object> matchingValues = naiveGetCompletions(base, keyList, prefix);
            MatcherAssert.assertThat("Prefix returns incorrect getCompletions", impl.getCompletions(prefix),
                    IsIterableContainingInAnyOrder.containsInAnyOrder(matchingValues.toArray()));
        }

        // Process removals if necessary
        if (testRemove) {
            for (List<Object> k : excludedKeys) {
                for (int i = 0; i <= k.size(); i++) {
                    assertTrue(impl.isPrefix(k.subList(0, i)), "Prefix that was added returns false from isPrefix.");
                }
                impl.remove(k);
            }
        }

        // Test isPrefix negative cases
        for (List<Object> p : excludedPrefixes) {
            assertFalse(impl.isPrefix(p), "Prefix that was removed still exists in the trie");
            MatcherAssert.assertThat("Prefix that doesn't exist returns non-empty array from getCompletions",
                    impl.getCompletions(p), IsIterableContainingInAnyOrder.containsInAnyOrder(new ArrayList<>()));
        }


    }

    @Tag("C")
    @Order(sanityTestLevel)
    @DisplayName("Sanity test ITrieMap getPrefix, getCompletions")
    @ParameterizedTest(name = "Test ITrieMap with data={0}")
    @MethodSource("iTrieMapSanityDataSource")
    default void sanityTestITrieMapNoRemove(Map<Iterable<Object>, Object> base, String[] prefixesToCheck) {
        iTrieMapSanityTestHelper(base, prefixesToCheck, false);
    }

    @Tag("A")
    @Order(sanityTestLevel)
    @DisplayName("Sanity test ITrieMap getPrefix, getCompletions, with remove")
    @ParameterizedTest(name = "Test ITrieMap with data={0}")
    @MethodSource("iTrieMapSanityDataSource")
    default void sanityTestITrieMapRemove(Map<Iterable<Object>, Object> base, String[] prefixesToCheck) {
        iTrieMapSanityTestHelper(base, prefixesToCheck, true);
    }

    @Tag("C")
    @Order(stressTestLevel)
    @DisplayName("Stress test ITrieMap getPrefix, getCompletions")
    @ParameterizedTest(name = "Test ITrieMap with seed={0} and size={1}")
    @CsvSource({
            "24589, 3000",
            "96206, 5000"
    })
    default void stressTestITrieMapNoRemove(int seed, int size) {
        iTrieMapStressTestHelper(seed, size, false);
    }

    @Tag("A")
    @Order(stressTestLevel)
    @DisplayName("Stress test ITrieMap getPrefix, getCompletions, with remove")
    @ParameterizedTest(name = "Test IDictionary  with seed={0} and size={1}")
    @CsvSource({
            "24589, 3000",
            "96206, 5000"
    })
    default void stressTestITrieMapRemove(int seed, int size) {
        iTrieMapStressTestHelper(seed, size, false);
    }

    default void verifyNonlazyRemoveHelper(Object trieNode) {
        Map<Object, Object> c = Reflection.getFieldValue(trieNode.getClass(), "pointers", trieNode);
        if (c.isEmpty()) {
            Object v = Reflection.getFieldValue(trieNode.getClass(), "value", trieNode);
            assertNotNull(v, "Leaf node of TrieMap has null value.");
        }
        for (Object v : c.values()) {
            verifyNonlazyRemoveHelper(v);
        }
    }

    @Tag("A")
    @Order(specialTestLevel)
    @DisplayName("Test that remove is not a lazy implementation")
    @ParameterizedTest(name = "Verify nonlazy remove for seed={0}, size={1}")
    @CsvSource({
            "24589, 3000",
            "96206, 5000"
    })
    default void verifyNonlazyRemove(int seed, int size) {
        Random rand = new Random(seed);
        int maxKeyLength = 10;
        Map<Iterable<Object>, Object> base = generateRandomTestDataIterable(size, rand, 2, 1, maxKeyLength);
        int longestKeyLength = base.keySet().stream().mapToInt(i -> (int) i.spliterator().getExactSizeIfKnown()).max().getAsInt();
        ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();

        // Populate ITrieMap, tracking the longest keys to remove the all current leaves,
        // guaranteeing that lazy deletion will leave hanging leaves.
        List<Iterable<Object>> longKeys = new ArrayList<>();
        for (Map.Entry<Iterable<Object>, Object> e : base.entrySet()) {
            impl.put(e.getKey(), e.getValue());
            if (e.getKey().spliterator().getExactSizeIfKnown() >= longestKeyLength) {
                longKeys.add(e.getKey());
            }
        }

        for (Iterable<Object> k : longKeys) {
            impl.remove(k);
            assertFalse(impl.containsKey(k), "Removed key still present in map.");
        }

        // Explore ITrieMap to completion, and verify that all leaves have values.
        Object root = Reflection.getFieldValue(TrieMap.class, "root", impl);
        verifyNonlazyRemoveHelper(root);
    }

    @Order(specialTestLevel)
    @Tag("C")
    @DisplayName("Test size() -- constant complexity")
    @Test
    default void testSizeComplexity() {
        Function<Integer, ITrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
            Random rand = new Random(34857);
            Map<Iterable<Object>, Object> o = generateRandomTestDataIterable(numElements, rand);
            ITrieMap<Object, Iterable<Object>, Object> impl = newITrieMap();
            for (Map.Entry<Iterable<Object>, Object> e : o.entrySet()) {
                impl.put(e.getKey(), e.getValue());
            }
            return impl;
        };

        Consumer<ITrieMap<Object, Iterable<Object>, Object>> size = (ITrieMap<Object, Iterable<Object>, Object> t) -> t.size();
        RuntimeInstrumentation.assertAtMost("size",
                RuntimeInstrumentation.ComplexityType.CONSTANT, provide, size, 8);
    }
}
