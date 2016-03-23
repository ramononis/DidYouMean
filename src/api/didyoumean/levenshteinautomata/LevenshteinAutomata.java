package api.didyoumean.levenshteinautomata;


import api.tree.Element;
import api.tree.Root;
import api.utils.Pair;

import java.util.*;

/**
 * Created by Tim on 3/9/2016.
 */
public class LevenshteinAutomata {

    public static int getScore(Pair<Element, State> state, int w) {
        int score = state.getLeft().getWeight();
        int distance = state.getRight().getDistance(w);
        return score / (distance + 1);
    }

    public static List<String> intersect(Root tree, LevenshteinAutomataFactory laf, String word, int n) {
        int w = word.length();
        List<String> result = new ArrayList<>(n);
        PriorityQueue<Pair<Element, State>> queue = new PriorityQueue<>((p1, p2) -> getScore(p2, w) - getScore(p1, w));
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
