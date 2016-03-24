package api.didyoumean.levenshteinautomata;


import api.tree.Element;
import api.tree.Root;
import api.utils.Pair;

import java.util.*;

import static java.lang.Math.*;

/**
 * The Class representing a Levenshtein Automata.
 * Created by Tim on 3/9/2016.
 */
public class LevenshteinAutomata {

    /**
     * Calculates the score for this state-pair.<br>
     * The current calculation is: query weight / (Lev. distance + 1)
     * @param state the current state-pair
     * @param w the length of the input word
     * @param ldWeight The weight of the LD.
     * @return the score for this pair
     */
    public static int getScore(Pair<Element, State> state, int w, int ldWeight) {
        int score = state.getLeft().getWeight();
        int distance = state.getRight().getDistance(w);
        return (int) (score / (pow(distance, ldWeight)));
    }
    /**
     * Intersects the dictionary tree with a (simulated) Levenhstein automata.
     * Returns the best string by {@link #getScore score} that satisfies the given maximal Levenshtein
     * distance in {@code laf}.
     * @param tree the dictionary tree to search in
     * @param laf the factory for simulating Levenshtein automata
     * @param word the (possibly corrupted) input word
     * @param ldWeight The weight of the LD.
     * @return A string similar to {@code word}, or an empty string if no result could be found
     */
    public static String intersect(Root tree, LevenshteinAutomataFactory laf, String word, int weight) {
        List<String> result = intersectN(tree, laf, word, 1, weight);
        return result.size() == 0 ? "" : result.get(0);
    }

    /**
     * Intersects the dictionary tree with a (simulated) Levenhstein automata.
     * Returns the top {@code n} strings sorted by {@link #getScore score} that satisfy the given maximal Levenshtein
     * distance in {@code laf}.
     * @param tree the dictionary tree to search in
     * @param laf the factory for simulating Levenshtein automata
     * @param word the (possibly corrupted) input word
     * @param n the amount of results
     * @param ldWeight The weight of the LD.
     * @return {@code n} strings similar to {@code word}, or less if there aren't that many results.
     */
    public static List<String> intersectN(Root tree, LevenshteinAutomataFactory laf, String word, int n, int ldWeight) {
        int w = word.length();
        List<String> result = new ArrayList<>(n);
        PriorityQueue<Pair<Element, State>> queue = new PriorityQueue<>((p1, p2) -> getScore(p2, w, ldWeight) - getScore(p1, w, ldWeight));
        queue.add(new Pair<>(tree, laf.getInit()));
        Pair<Element, State> state;
        while ((state = queue.poll()) != null && result.size() < n) {
            Element e = state.getLeft();
            State s = state.getRight();
            if (e.isLeaf()) {
                if (s.isAcceptingState(w)) {
                    result.add(e.getWord().replace("\0", ""));
                }
            } else {
                for (Element child : e.getChildren()) {
                    State out = child.isLeaf() ? s : s.outState(child.getLetter(), word);
                    if (out != null) {
                        queue.add(new Pair<>(child, out));
                    }
                }
            }
        }
        return result;
    }
}
