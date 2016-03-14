package autoComplete.tree;

/**
 * This class models a leaf in the tree.
 *
 * @author Frans
 */
public class Leaf extends Element {

    /**
     * Initializes a new Leaf.
     * The new Leaf is also directly added as child to the given parent, if possible.
     *
     * @param parent the parent of this Leaf
     */
    public Leaf(Node parent) {
        this(0, parent);
    }

    /**
     * Initializes a new Leaf.
     * The new Leaf is also directly added as child to the given parent, if possible.
     *
     * @param weight the weight of this Leaf if it is a Leaf
     * @param parent the parent of this Leaf
     */
    public Leaf(int weight, Node parent) {
        super((char) 0, weight, parent);
    }

    /**
     * Indicates that this Element is a Leaf.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return "Leaf(" + getWeight() + ")";
    }
}
