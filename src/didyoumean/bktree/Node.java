package didyoumean.bktree;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yannick on 10-3-2016.
 */
public class Node
{
    private String name;
    private Map<Integer, Node> children;
    private int score;

    /**
     * Creates a new Node with a certain word. Has no children and a score of 0.
     * @param word The word corresponding to this Node.
     */
    public Node(String word){
        name = word;
        children = new ConcurrentHashMap<Integer, Node>();
        score = 0;
    }

    /**
     * Creates a new Node with a certain word and score. Has no children.
     * @param word The word corresponding to this Node.
     * @param score The score this Node should have.
     */
    public Node(String word, int score){
        this(word);
        this.score = score;
    }

    /**
     * Adds a new child to this node.
     * @param child The Node that this Node should be the parent of.
     */
    public void addChild(Node child){
        int distance = BKTree.calculateLD(child.getName(),name);
        if(children.get(distance)!=null){
            children.get(distance).addChild(child);
        }
        else{
            children.put(distance,child);
        }
    }

    /**
     * Returns the word of this Node.
     * @return The word of this Node.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the children of this node.
     * @return The children of this node.
     */
    public Map<Integer, Node> getChildren(){
        return children;
    }

    /**
     * Prints this tree in a readable fashion.
     */
    public void printTree(){
        for(Node n : children.values()){
            System.out.print(BKTree.calculateLD(name, n.getName()) + " : " + n.getName() + " (" + n.getScore() + ") " + "; ");
        }
        System.out.print("\n");
        for(Node n : children.values()){
            n.printTree();
        }
    }

    /**
     * Returns the score of this Node.
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of this Node.
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    public String toString(){
        return getName() + " (" + getScore() + ")";
    }
}
