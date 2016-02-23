package autoComplete.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created By Frans on 2016-02-23.
 */
public class Node extends Element {
    private final Set<Element> children;

    public Node(char letter, int maxWeight) {
        this(letter, maxWeight, null);
    }

    public Node(char letter, int maxWeight, Node parent) {
        super(letter, maxWeight, parent);
        children = new HashSet<>();
    }

    public Set<Element> getChildren() {
        return new HashSet<>(children);
    }

    public boolean addChild(Node node) {
        return children.add(node);
    }

    public boolean removeChild(Node node) {
        return children.remove(node);
    }

}
