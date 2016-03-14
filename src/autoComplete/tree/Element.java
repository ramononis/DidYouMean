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
     * The new Element is also directly added as child to the given parent, if possible.
     *
     * @param letter the <code>letter</code> of this Element, or 0 for a {@link Leaf} (and -1 for the {@link Root})
     * @param weight the <code>weight</code> of this Element if it is a {@link Leaf}; the max weight of all sub Elements if this is a {@link Node}
     * @param parent the <code>parent</code> of this Element
     */
    public Element(char letter, int weight, Node parent) {
        this.letter = letter;
        this.weight = weight;
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
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
     *
     * @param weight the new weight value
     */
    public void setWeight(int weight) {
        this.weight = weight;
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
     * Returns the word corresponding to the current {@link Node}.
     *
     * @return A {@link String} corresponding to this {@link Node}
     */
    public String getWord() {
        return parent.getWord() + getLetter();
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
