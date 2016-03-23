package api.didyoumean.bktree;

import api.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;


/**
 * The Class that handles all the logic behind its own api.tree. One class corresponds to one api.tree.
 * Created by Yannick on 10-3-2016.
 */
public class BKTree {
    private static Node root;

    public static final int MAX_ERROR_RANGE = 3;


    /**
     * Builds a new api.tree given a list of words and data to fill nodes with. Nodes contain
     * the scores of a word.
     *
     * @param data The data (word, score) the api.tree should contain.
     * @throws IllegalArgumentException if data is null.
     */
    public void buildTree(Map<String, Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("Tried to make a api.tree with a datamap that is null.");
        }
        Set<String> set = data.keySet();
        List<String> list = new ArrayList<>();
        list.addAll(set);
        root = new Node(list.get(0), data.get(list.get(0)));
        for (String s : list) {
            if (list.indexOf(s) != 0) {
                int score = 0;
                if (data.keySet().contains(s)) {
                    score = data.get(s);
                }
                root.addChild(new Node(s, score));
            }
        }
    }

    /**
     * Calculates the Levenshtein Distance between 2 words.
     *
     * @param word1 The first word.
     * @param word2 The second word.
     * @return The LD between word1 and word2.
     * @throws IllegalArgumentException if either {@code word1} or {@code word2} is null.
     */
    public static int calculateDistance(String word1, String word2) {
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("Null argument in BKTree.CalculateDistance.");
        }
        //based on http://rosettacode.org/wiki/Levenshtein_distance#Java

        int[] costs = new int[word2.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= word1.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= word2.length(); j++) {
                int costj = min(1 + min(costs[j], costs[j - 1]),
                        word1.charAt(i - 1) == word2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = costj;
            }
        }
        return costs[word2.length()];
    }

    /**
     * Gets a 'did-you-mean' suggestion for a word.
     *
     * @param word The word the user searched for.
     * @return The word the user probably meant when searching for {@code word}. May be the same as {@code word}.
     * @throws IllegalArgumentException if {@code word} is null
     */
    public String getDYM(String word) {
        List<String> result = getDYM_N(word, 1);
        return result.isEmpty() ? "" : result.get(0);
    }

    /**
     * Gets a list of n 'did-you-mean' suggestions for a word.
     *
     * @param word The word the user searched for.
     * @param n    The maximum number of suggestions to return.
     * @return A list with at most n words the user probably meant when searching for {@code word}.
     * Sorted from most likely to least likely. May include {@code word}, if so this will be the first one.
     * @throws IllegalArgumentException if {@code word} is null
     */
    public List<String> getDYM_N(String word, int n) {
        if (word == null) {
            throw new IllegalArgumentException("Null word in BKTree.getDYM");
        }

        //TODO: change 6, in Math.pow, to constant or better yet a variable
        return getRoot().searchTreeForNodes(word.toLowerCase(), MAX_ERROR_RANGE).entrySet().parallelStream()
                .map(e -> new Pair<>(e.getKey().getScore() / Math.pow(e.getValue(), 6), e.getKey()))
                .sorted((p1, p2) -> p2.getLeft().compareTo(p1.getLeft()))
                .limit(n)
                .map(p -> p.getRight().getName())
                .collect(Collectors.toList());
    }

    /**
     * Gets the root of this api.tree.
     *
     * @return The root of this api.tree.
     */
    public Node getRoot() {
        return root;
    }

}
