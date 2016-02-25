package autoComplete.tree;

/**
 * This class models a leaf in the tree.
 * @author Frans
 */
public class Leaf extends Element {

    /**
     * Initializes a new Leaf.
     * @param weight the weight of this Leaf if it is a Leaf
     * @param parent the parent of this Leaf
     */
    public Leaf(float weight, Node parent) {
        super((char)0, weight, parent);
    }

    /**
     * Indicates that this Element is a Leaf.
     * @return <code>true</code>
     */
    @Override
    public boolean isLeaf() {
        return true;
    }
}
