package tree;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class models a node in the tree.
 *
 * @author Frans
 */
public class Node extends Element {
    private final ConcurrentHashMap<Character, Element> children;

    /**
     * Initializes a new Node.
     * The new Node is not added as child to the given parent.
     *
     * @param letter the letter of this Node (or -1 for a {@link Root})
     * @param parent the parent of this Node, may not be {@code null} use {@link Root} as root
     */
    Node(char letter, Node parent) {
        super(letter, parent);
        children = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Element> getChildren() {
        return new HashSet<>(children.values());
    }

    @Override
    public boolean hasChild(char letter) {
        return children.containsKey(letter);
    }

    @Override
    public Element getChild(char letter) {
        return children.get(letter);
    }

    /**
     * Adds the given (sub)keyword to this Node,
     * when already present the given weight is added to the current weight.
     *
     * @param k the keyword, must be terminated with a null char.
     * @param w the weight
     */
    void addOrIncrementWord(String k, int w) {
        Element child = addOrGetChild(k.charAt(0));
        if (child.isLeaf()) {
            child.setWeight(child.getWeight() + w);
        } else {
            ((Node) child).addOrIncrementWord(k.substring(1), w);
        }
    }

    /**
     * Creates and adds a new child to this node if not already a child with the same letter.
     *
     * @param letter the letter of the new {@link Element}, 0 for a Leaf.
     * @return the new added {@link Element} if there was not already a child with the same letter;
     * otherwise the {@link Element} with the same letter that is already a child of this Node
     */
    private Element addOrGetChild(char letter) {
        Element result;

        if (children.containsKey(letter)) {
            result = children.get(letter);
        } else {
            if (letter == 0) {
                result = new Leaf(this);
            } else {
                result = new Node(letter, this);
            }
            children.put(letter, result);
        }

        return result;
    }

//    @Override
//    public String toString() {
//        return "Node(" + getLetter() + ", " + getWeight() + ")";
//    }
}
