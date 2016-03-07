package autoComplete.tree;

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
     *
     * @param letter the letter of this Node (or -1 for the Root)
     */
    public Node(char letter) throws IllegalArgumentException {
        this(letter, 0, null);
    }

    /**
     * Initializes a new Node.
     * The new Node is also directly added as child to the given parent, if possible.
     *
     * @param letter the letter of this Node (or -1 for the Root)
     * @param parent    the parent of this Node
     */
    public Node(char letter, Node parent) throws IllegalArgumentException {
        this(letter, 0, parent);
    }

    /**
     * Initializes a new Node.
     *
     * @param letter    the letter of this Node (or -1 for the Root)
     * @param maxWeight the max weight of all sub Nodes
     */
    public Node(char letter, float maxWeight) throws IllegalArgumentException {
        this(letter, maxWeight, null);
    }

    /**
     * Initializes a new Node.
     * The new Node is also directly added as child to the given parent, if possible.
     *
     * @param letter    the letter of this Node (or -1 for the Root)
     * @param maxWeight the max weight of all sub Nodes
     * @param parent    the parent of this Node
     */
    public Node(char letter, float maxWeight, Node parent) throws IllegalArgumentException {
        super(letter, maxWeight, parent);
        if (letter == 0) throw new IllegalArgumentException("letter may not be 0.");
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
     * Adds a new child to this node if there is not already a child with the same letter.
     * @param element the Element to add as child
     * @return <code>null</code> if the Element was added;
     * otherwise the Element with the same letter that is already a child of this Node
     */
    public Element addChild(Element element) {
        return children.putIfAbsent(element.getLetter(), element);
    }

    /**
     * Creates and adds a new child to this node if not already a child with the same letter.
     * @param letter the letter of the new Element, 0 for a Leaf.
     * @return the new added Element if there was not already a child with the same letter;
     * otherwise the Element with the same letter that is already a child of this Node
     */
    public Element addNewChild(char letter) {
        Element result = null;

        if (letter == 0) {
            result = new Leaf(this);
        } else {
            if (children.containsKey(letter)) {
                result = children.get(letter);
            } else {
                result = new Node(letter, this);
            }
        }

        return result;
    }

    /**
     * Removes the specified child Element from this Node.
     *
     * @param element the Element to remove
     * @return <code>true</code> if an Element was removed as a result of this call
     */
    public boolean removeChild(Element element) {
        return children.values().remove(element);
    }
}
