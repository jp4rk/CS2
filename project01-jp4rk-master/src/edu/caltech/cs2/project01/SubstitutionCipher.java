package edu.caltech.cs2.project01;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class    SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();

    /**
     * Construct a SubstitutionCipher with the given cipher text and key
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;

    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        this.ciphertext = ciphertext;
        Map<Character, Character> maap = new HashMap<>();
        for(char c: CaesarCipher.ALPHABET) {
            maap.put(c, c);
        }

        SubstitutionCipher ans = new SubstitutionCipher(ciphertext, maap);
        for (int i = 0; i < 10000; i ++) {
            ans = ans.randomSwap();
        }

        this.key = ans.key;
    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        return this.ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {
        String result = "";
        for (int i = 0; i < this.ciphertext.length(); i++) {
           result += this.key.get(this.ciphertext.charAt(i));
        }
        return result;
    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {
        char a = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];
        char b = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];

        while (a == b){
            b = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];
        }

        Map<Character, Character> mapz = new HashMap<>(key);

        char c = mapz.get(a);
        char d = mapz.get(b);

        mapz.put(a, d);
        mapz.put(b, c);

        return new SubstitutionCipher(this.ciphertext, mapz);
    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods){
        double ans = 0.0;
        String quads = "";
        String str = getPlainText();
        for (int i = 0; i < str.length() - 3; i++){
            quads = str.substring(i, i + 4);
            ans += likelihoods.get(quads);

        }

        return ans;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        SubstitutionCipher cypher = new SubstitutionCipher(this.ciphertext);

        int i = 0;
        while (i < 1000) {
            SubstitutionCipher cypher2 = cypher.randomSwap();
            double A = cypher.getScore(likelihoods);
            double B = cypher2.getScore(likelihoods);
            if (A < B){
                cypher.key = cypher2.key;
                i = 0;
            }
            i++;
        }

        return cypher;
    }
}
