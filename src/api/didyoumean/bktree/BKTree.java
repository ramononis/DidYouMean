package api.didyoumean.bktree;

import api.didyoumean.DidYouMean;
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
     * @param word     The word the user searched for.
     * @param ldWeight The weight of the LD.
     * @return The word the user probably meant when searching for {@code word}. May be the same as {@code word}.
     * @throws IllegalArgumentException if {@code word} is null
     */
    public String getDYM(String word, int ldWeight) {
        List<String> result = getDYM_N(word, 1, ldWeight);
        return result.isEmpty() ? "" : result.get(0);
    }

    /**
     * Gets a list of n 'did-you-mean' suggestions for a word.
     *
     * @param word The word the user searched for.
     * @param n    The maximum number of suggestions to return.
     * @param ldWeight The weight of the LD.
     * @return A list with at most n words the user probably meant when searching for {@code word}.
     * Sorted from most likely to least likely. May include {@code word}, if so this will be the first one.
     * @throws IllegalArgumentException if {@code word} is null
     */
    public List<String> getDYM_N(String word, int n, int ldWeight) {
        if (word == null) {
            throw new IllegalArgumentException("Null word in BKTree.getDYM");
        }
        return getRoot().searchTreeForNodes(word.toLowerCase(), DidYouMean.MAX_DISTANCE).entrySet().parallelStream()
                .map(e -> new Pair<>(e.getKey().getScore() / Math.pow(e.getValue(), ldWeight), e.getKey()))
                .sorted((p1, p2) -> p2.getLeft().compareTo(p1.getLeft()))
                .limit(n)
                .map(p -> p.getRight().getName())
                .collect(Collectors.toList());
    }

    /**
     * If the current tree does not contain this word: Adds a new {@code Node} to this tree with word {@Code word}
     * and weight {@code weight}. If the current tree does contain a word, changes the weight to the given {@code word}.
     * @param word The word which should be added or changed.
     * @param weight The weight corresponding to the word
     * @throws IllegalArgumentException if {@code word} is {@code null}, or {@code weight} is negative.
     */
    public synchronized void addOrSet(String word, int weight){
        if(word == null){
            throw new IllegalArgumentException("Parameter word is null in BKTree.addOrSet.");
        }
        if(weight < 0){
            throw new IllegalArgumentException("Parameter weight is negative in BKTree.addOrSet.");
        }
        Node wordNode = getRoot().getWordInChildren(word);
        if(wordNode != null){
            wordNode.setScore(weight);
        }else{
            getRoot().addChild(new Node(word, weight));
        }
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
