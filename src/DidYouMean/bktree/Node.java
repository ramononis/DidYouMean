package DidYouMean.bktree;


import util.CalculateDistance;

import java.util.HashMap;

/**
 * Created by Yannick on 10-3-2016.
 */
public class Node
{
    private String name;
    private HashMap<Integer, Node> children;
    private int score;

    public Node(String word){
        name = word;
        children = new HashMap<>();
        score = 0;
    }

    public Node(String word, int score){
        this(word);
        this.score = score;
    }

    public void addChild(Node child){
        int distance = BKTree.calculateDistance(child.getName(),name);
        if(children.get(distance)!=null){
            children.get(distance).addChild(child);
        }
        else{
            children.put(distance,child);
        }
    }

    public String getName(){
        return name;
    }

    public HashMap<Integer, Node> getChildren(){
        return children;
    }

    public void printTree(){
        for(Node n : children.values()){
            System.out.print(BKTree.calculateDistance(name, n.getName()) + " : " + n.getName() + " (" + n.getScore() + ") " + "; ");
        }
        System.out.print("\n");
        for(Node n : children.values()){
            n.printTree();
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
