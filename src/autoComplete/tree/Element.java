package autoComplete.tree;

import java.util.Set;

/**
 * Created by Frans on 2016-02-23.
 */
public abstract class Element {
    private final char letter;
    private int weight;
    private final Node parent;

    public Element(char letter, int weight, Node parent) {
        this.letter = letter;
        this.weight = weight;
        this.parent = parent;
    }

    public char getLetter() {
        return letter;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getParent() {
        return parent;
    }

    public abstract Set<Element> getChildren();

    public boolean isLeaf() {
        return letter == 0;
    }
}
