package didyoumean.bktree;

import database.CSVControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Yannick on 10-3-2016.
 */
public class BKTree {
    private static Node root;

    public static final int MAX_ERROR_RANGE = 3;

    private BKTree(){}

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

    public static void printTree(Node root){
        System.out.println(root.getName());
        root.printTree();
    }

    public static int calculateDistance(String lhs, String rhs){
        lhs = lhs.toLowerCase();
        rhs = rhs.toLowerCase();
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= rhs.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

        return distance[lhs.length()][rhs.length()];
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static List<String> searchTree(Node root, String term, int errorRange){
        List<String> result = new ArrayList<>();
        int distance = calculateDistance(term, root.getName());
        if(distance <= errorRange){
            result.add(root.getName());
        }
        for(Node n : root.getChildren().values()){
            if(Math.abs(calculateDistance(n.getName(),root.getName())-distance) <= errorRange){
                result.addAll(searchTree(n,term,errorRange));
            }
        }
        return result;
    }

    public static Map<Node, Integer> searchTreeForNodes(Node root, String term, int errorRange){
        Map<Node, Integer> result = new HashMap<>();
        int distance = calculateDistance(term, root.getName());
        if(distance <= errorRange){
            result.put(root, distance);
        }
        for(Node n : root.getChildren().values()){
            if(Math.abs(calculateDistance(n.getName(),root.getName())-distance) <= errorRange){
                result.putAll(searchTreeForNodes(n,term,errorRange));
            }
        }
        return result;
    }

    public static String getDYM(String word){
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
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

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

    public static Node getRoot(){
        return root;
    }

    public static void main(String[] args){
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

        for (int i = 0; i < 1; i++) {
//                BKTree.getDYM("accu 6");
            System.out.println("\nDid you mean: " + BKTree.getDYM("accu 6"));
        }
        stop = System.currentTimeMillis();
        System.out.println(stop - start + "ms inclusief bouwen, " + (stop - start2) + " exclusief bouwen");

    }
}
