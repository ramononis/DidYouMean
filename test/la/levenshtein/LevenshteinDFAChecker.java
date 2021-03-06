package la.levenshtein;


import net.automatalib.automata.transout.MealyMachine;
import org.junit.Assert;

import java.util.Collection;
import java.util.HashSet;

import static la.levenshtein.SlowLevenshtein.distance;

public class LevenshteinDFAChecker {
    private String word;
    private int distance;
    private Collection<String> alphabet;

    public LevenshteinDFAChecker(String w, int d) {
        word = w;
        distance = d;
        alphabet = new HashSet<>(w.length() + 1);
        alphabet.add("*");
        w.chars().forEach(i -> alphabet.add(""+((char) i)));
    }

    public <S> void check(MealyMachine<S, String, ?, String> result) {
        explore("", result.getInitialState(), result);
    }

    private <S> void  explore(String w, S state, MealyMachine<S, String, ?, String> dfa) {
        for (String i : alphabet) {
            String o = dfa.getOutput(state, i);
            String newW = w + i;
            int realDistance = distance(newW, word);
            if (realDistance > distance) {
                Assert.assertTrue("output: " + o + ", word: " + newW, o.equals("ok") || o.equals("fail"));
            } else if (realDistance <= distance) {
                Assert.assertTrue("output: " + o + ", word: " + newW, o.equals("success"));
            }
            if (!o.equals("fail")) {
                S nextState = dfa.getSuccessor(state, i);
                explore(newW, nextState, dfa);
            }
        }
    }
}
