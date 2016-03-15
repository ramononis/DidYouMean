package didyoumean.bktree;

import database.CSVControl;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Yannick on 10-3-2016.
 */
public class BKTree {
    private Node root;
    private HashMap<String, Integer> data;

    public BKTree(){
        data = new HashMap<>();
    }

    public void buildTree(ArrayList<String> list){
        root = new Node(list.get(0));
        for(String s : list){
            if(list.indexOf(s) != 0){
                int score = 0;
                if(getData().keySet().contains(s)){
                    score = getData().get(s);
                }
                root.addChild(new Node(s, score));
            }
        }
    }

    public void addData(HashMap<String, Integer> data){
        this.data = data;
    }

    public HashMap<String, Integer> getData(){
        return data;
    }

    public static void printTree(Node root){
        System.out.println(root.getName());
        root.printTree();
    }

    public static int calculateDistance(String lhs, String rhs){
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

    public ArrayList<String> searchTree(Node root, String term, int errorRange){
        ArrayList<String> result = new ArrayList<>();
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

    public Node getRoot(){
        return root;
    }

    public static void main(String[] args){
        ArrayList<String> tree = new ArrayList<String>();
        tree.add("Test");
        tree.add("Text");
        tree.add("Telt");
        tree.add("Geld");
        tree.add("Gold");
        tree.add("Galt");
        tree.add("Tekt");
        tree.add("accu");
        BKTree bkTree = new BKTree();
        bkTree.addData(new CSVControl().getData());
        bkTree.buildTree(tree);
        bkTree.printTree(bkTree.getRoot());
        for(String s : bkTree.searchTree(bkTree.getRoot(),"Gest",4)){
            System.out.print(s + " ");
        }
    }
}
