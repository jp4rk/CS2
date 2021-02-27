package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.textgenerator.NGram;
import org.junit.jupiter.params.provider.Arguments;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

// Wrapper class for all IDictionaries that will be tested using NGram keys to reduce code repetition
public interface IDictionaryNGramTests extends IDictionaryTests {

    @Override
    default Stream<Arguments> iDictionarySmokeDataSource() {
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

    @Override
    default Map<Object, Object> createReferenceMap(String[] keys, Object[] vals) {
        Map<Object, Object> ref = new HashMap<>();
        for (int i = 0; i < keys.length; i ++) {
            ref.put(NGramTests.stringToNGram(keys[i]), vals[i]);
        }
        return ref;
    }

    @Override
    default Map<Object, Object> generateRandomTestData(int size, Random rand, int maxNodeDegree, int minKeyLength, int maxKeyLength) {
        Map<Object, Object> base = new HashMap<>();
        for (int i = 0; i < size; i ++) {
            int keyLength = minKeyLength + rand.nextInt(maxKeyLength-minKeyLength);
            String[] key = new String[keyLength];
            for (int j = 0; j < keyLength; j ++) {
                key[j] = String.valueOf(rand.nextInt(maxNodeDegree));
            }
            base.put(new NGram(key), rand.nextInt());
        }
        return base;
    }

}
