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

    private BKTree(){}

    /**
     * Builds a new tree given a list of words and data to fill nodes with. Nodes contain
     * the scores of a word.
     * @param list The list of words the tree should contain.
     * @param data The data (word, score) the tree should contain.
     */
    public static void buildTree(List<String> list, Map<String, Integer> data){
        root = new Node(list.get(0), data.get(list.get(0)));
        for(String s : list){
            if(list.indexOf(s) != 0){
                int score = 0;
                if(data.keySet().contains(s)){
                    score = data.get(s);
                }
                root.addChild(new Node(s, score));
            }
        }
    }

    /**
     * Prints a tree's structure.
     * @param root The root of the tree.
     */
    public static void printTree(Node root){
        System.out.println(root.getName());
        root.printTree();
    }

    /**
     * Gets the minimum between 3 integers.
     * @param a The first integer.
     * @param b The second integer.
     * @param c The third integer.
     * @return The minimum of a, b and c.
     */
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * Retrieves all the words within a certain LD of a word.
     * @param root The root of the tree.
     * @param term The term that words should be compared with.
     * @param errorRange The maximum LD a word should have compared to {@code term}.
     * @return The list of words with maximum LD to term.
     */
    public static List<String> searchTree(Node root, String term, int errorRange){
        List<String> result = new ArrayList<>();
        int distance = calculateLD(term, root.getName());
        if(distance <= errorRange){
            result.add(root.getName());
        }

        for(Node n : root.getChildren().values()){
            if(Math.abs(calculateLD(n.getName(),root.getName())-distance) <= errorRange){
                result.addAll(searchTree(n,term,errorRange));
            }
        }
        return result;
    }

    /**
     * Retrieves all the Nodes within a certain lD of a word.
     * @param root The root of the tree.
     * @param term The term that Nodes should be compared with.
     * @param errorRange The maximum LD a Node should have compared to {#code term}
     * @return The list of Nodes with maximum LD to term.
     */
    public static Map<Node, Integer> searchTreeForNodes(Node root, String term, int errorRange){
        Map<Node, Integer> result = new ConcurrentHashMap<Node, Integer>();
        int distance = calculateLD(term, root.getName());
        if(distance <= errorRange){
            result.put(root, distance);
        }
        root.getChildren().values()
                .parallelStream()
                .filter(n -> Math.abs(calculateLD(n.getName(),root.getName())-distance) <= errorRange)
                .forEach(n -> result.putAll(searchTreeForNodes(n, term, errorRange)));
        return result;
    }

    /**
     * Gets a 'did-you-mean' suggestion for a word.
     * @param word The word the user searched for.
     * @return The word the user probably meant when searching for {@code word}. May be the same as {@code word}.
     */
    public static String getDYM(String word){
        word = word.toLowerCase();
        Map<Node, Integer> nodeMap = searchTreeForNodes(getRoot(), word, MAX_ERROR_RANGE);
        Node bestNode = null;
        double bestScore = -1;
        for(Node n : nodeMap.keySet()){
            int levenDis = nodeMap.get(n);
            double score = n.getScore()/Math.pow(levenDis, 6); //TODO: random formula for now, change to a good one.
            if(levenDis == 0){
                return n.getName();
            }else{

//                System.out.println("\n" + n.getName() + " (" + score + ")");
                if(score > bestScore){
                    bestNode = n;
                    bestScore = score;
                }
            }
        }
        return bestNode == null ? "" : bestNode.getName();
    }

    public static int calculateLD(String word1, String word2){
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
     * Gets the root of this tree.
     * @return The root of this tree.
     */
    public static Node getRoot(){
        return root;
    }

    public static void main(String[] args){
        System.out.println("beginn");
        ArrayList<String> tree = new ArrayList<String>();
        String[] dataPlaces = new String[]{"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};
        HashMap<String, Integer> data = new CSVControl(dataPlaces).getData();
        tree.addAll(data.keySet());
        tree.add("Test");
        tree.add("Text");
        tree.add("Telt");
        tree.add("Geld");
        tree.add("Gold");
        tree.add("Galt");
        tree.add("Tekt");
        tree.add("Accu");
        BKTree bkTree = new BKTree();
        data.put("Test", 16);
        data.put("Text", 10);
        data.put("Telt", 3);
        data.put("Geld", 10);
        data.put("Gold", 9);
        data.put("Galt", 8);
        data.put("Tekt", 18);


        long start, stop;
        start = System.currentTimeMillis();
        BKTree.buildTree(tree, data);
//            data.clear();
//        bkTree.printTree(bkTree.getRoot());
//        HashMap<Node, Integer> nodeMap = bkTree.searchTreeForNodes(bkTree.getRoot(), "Test", 4);
//        for(Node n : nodeMap.keySet()){
//            System.out.print(n.toString() + ", LD " + nodeMap.get(n) + "; ");
//        }
        long start2 = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
               BKTree.getDYM("batery");
//            System.out.println("\nDid you mean: " + BKTree.getDYM("accu 6"));
        }
        stop = System.currentTimeMillis();
        System.out.println(stop - start + "ms inclusief bouwen, " + (stop - start2) + " exclusief bouwen");

    }
}
