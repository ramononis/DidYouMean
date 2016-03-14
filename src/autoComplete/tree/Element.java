package autoComplete.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * This abstract class models the elementary basis of the tree.
 *
 * @author Frans
 */
public abstract class Element {
    private final char letter;
    private int weight;
    private final Node parent;

    /**
     * Initializes a new Element.
     * The new Element is not added as child to the given parent.
     *
     * @param letter the <code>letter</code> of this Element, or 0 for a {@link Leaf} (and -1 for the {@link Root})
     * @param parent the <code>parent</code> of this Element, may only be <code>null</code> for {@link Root}
     */
    protected Element(char letter, Node parent) {
        if (parent == null && !(this instanceof Root)) {
            throw new IllegalArgumentException("Every element except Root must have a parent!");
        }
        if (letter == (char) 0 && !(this instanceof Leaf)) {
            throw new IllegalArgumentException("Letter may not be 0, except for leaves.");
        }
        if (letter == (char) -1 && !(this instanceof Root)) {
            throw new IllegalArgumentException("Letter may not be 0, except for the root.");
        }

        this.letter = letter;
        this.parent = parent;
    }

    /**
     * Gets the letter of this Element.
     *
     * @return the letter of this Element, or 0 for a {@link Leaf}
     * @throws RuntimeException when this Element is the root because {@link Root} had no letter
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Returns the word corresponding to the current {@link Node}.
     *
     * @return A {@link String} corresponding to this {@link Node}
     */
    public String getWord() {
        return parent.getWord() + getLetter();
    }

    /**
     * Gets the weight value of this Element.
     * For a node this is the max weight of all leafs under this node.
     *
     * @return the weight value
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight value of this Element.
     * For a {@link Node} this must be the max weight of all leafs under this node.
     * Also changes the weight of the parent if necessary.
     *
     * @param weight the new weight value
     */
    protected void setWeight(int weight) {
        this.weight = weight;
        Node parent = getParent();
        if (parent != null && parent.getWeight() < weight) {
            parent.setWeight(weight);
        }
    }

    /**
     * Gets the parent of this Element.
     *
     * @return the parent of this Element; or <code>null</code> when this Element has no parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns a {@link Set} containing all elements directly under this Element.
     * If this Element has no children a empty set will be returned, never <code>null</code>.
     *
     * @return a set containing all elements directly under this Element
     */
    public Set<Element> getChildren() {
        return new HashSet<>();
    }

    /**
     * Checks if this Element has a child with the given letter.
     *
     * @param letter the letter
     * @return <code>true</code> when this Element has a child with the given letter; otherwise <code>false</code>
     */
    public boolean hasChild(char letter) {
        return false;
    }

    /**
     * Gets the child of this Element with the given letter.
     *
     * @param letter the letter
     * @return the child of this Element with the given letter;
     * or <code>null</code> when this Element has no child with the given letter
     */
    public Element getChild(char letter) {
        return null;
    }

    /**
     * Indicates if this Element is a {@link Leaf}.
     *
     * @return <code>true</code> when this Element is a {@link Leaf}; otherwise <code>false</code>
     */
    public boolean isLeaf() {
        return false;
        //return letter == 0;
    }

    /**
     * Indicates if this Element is a {@link Root}.
     *
     * @return <code>true</code> when this Element is a {@link Root}; otherwise <code>false</code>
     */
    public boolean isRoot() {
        return false;
    }
}
