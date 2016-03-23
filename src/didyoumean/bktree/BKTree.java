package didyoumean.bktree;

import database.CSVControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Yannick on 10-3-2016.
 */
public class BKTree {
    private static Node root;

    public static final int MAX_ERROR_RANGE = 3;


    /**
     * Builds a new tree given a list of words and data to fill nodes with. Nodes contain
     * the scores of a word.
     *
     * @param list The list of words the tree should contain.
     * @param data The data (word, score) the tree should contain.
     * @throws IllegalArgumentException if either list or data is null.
     */
    public void buildTree(List<String> list, Map<String, Integer> data) {
        if (list == null) {
            throw new IllegalArgumentException("Tried to make a tree from a list that is null.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Tried to make a tree with a datamap that is null.");
        }
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

//    /**
//     * Prints a tree's structure.
//     * @param root The root of the tree.
//     */
//    public static void printTree(Node root){
//        System.out.println(root.getName());
//        root.printTree();
//    }

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
                int costj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
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
        if (word == null) {
            throw new IllegalArgumentException("Null word in BKTree.getDYM");
        }
        word = word.toLowerCase();

        Map<Node, Integer> nodeMap = getRoot().searchTreeForNodes(word, MAX_ERROR_RANGE);
        Node bestNode = null;
        double bestScore = -1;
        for (Node n : nodeMap.keySet()) {
            int levenDis = nodeMap.get(n);
            double score = n.getScore() / Math.pow(levenDis, 6);
            if (levenDis == 0) {
                return n.getName();
            } else {

//                System.out.println("\n" + n.getName() + " (" + score + ")");
                if (score > bestScore) {
                    bestNode = n;
                    bestScore = score;
                }
            }
        }
        return bestNode == null ? "" : bestNode.getName();
    }

//    public static int calculateLD(String word1, String word2){
//        //based on http://rosettacode.org/wiki/Levenshtein_distance#Java
//
//        int[] costs = new int[word2.length() + 1];
//        for (int j = 0; j < costs.length; j++) {
//            costs[j] = j;
//        }
//        for (int i = 1; i <= word1.length(); i++) {
//            // j == 0; nw = lev(i - 1, j)
//            costs[0] = i;
//            int nw = i - 1;
//            for (int j = 1; j <= word2.length(); j++) {
//                int costj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
//                        word1.charAt(i - 1) == word2.charAt(j - 1) ? nw : nw + 1);
//                nw = costs[j];
//                costs[j] = costj;
//            }
//        }
//        return costs[word2.length()];
//    }

    /**
     * Gets the root of this tree.
     *
     * @return The root of this tree.
     */
    public static Node getRoot() {
        return root;
    }

}
