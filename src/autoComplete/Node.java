package autoComplete;

import java.util.HashSet;
import java.util.Set;

/**
 * Created By Frans on 2016-02-23.
 */
public class Node {
    private final char letter;
    private final Node parent;
    private int weight;
    private int maxWeight;
    private final Set<Node> children;

    public Node(char letter, Node parent, int weight) {
        this.letter = letter;
        this.parent = parent;
        this.weight = weight;
        children = new HashSet<>();
    }

    public char getLetter() {
        return letter;
    }

    public Node getParent() {
        return parent;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Set<Node> getChildren() {
        return new HashSet<>(children);
    }

    public boolean addChild(Node node) {
        return children.add(node);
    }

    public boolean removeChild(Node node) {
        return children.remove(node);
    }

    public boolean isWord() {
        return false;
    }

}
